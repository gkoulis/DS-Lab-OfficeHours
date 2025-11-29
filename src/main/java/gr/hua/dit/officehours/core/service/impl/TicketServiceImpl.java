package gr.hua.dit.officehours.core.service.impl;

import gr.hua.dit.officehours.core.model.Person;
import gr.hua.dit.officehours.core.model.PersonType;
import gr.hua.dit.officehours.core.model.Ticket;
import gr.hua.dit.officehours.core.model.TicketStatus;
import gr.hua.dit.officehours.core.port.SmsNotificationPort;
import gr.hua.dit.officehours.core.repository.PersonRepository;
import gr.hua.dit.officehours.core.repository.TicketRepository;
import gr.hua.dit.officehours.core.security.CurrentUser;
import gr.hua.dit.officehours.core.security.CurrentUserProvider;
import gr.hua.dit.officehours.core.service.TicketService;

import gr.hua.dit.officehours.core.service.mapper.TicketMapper;
import gr.hua.dit.officehours.core.service.model.CompleteTicketRequest;
import gr.hua.dit.officehours.core.service.model.OpenTicketRequest;
import gr.hua.dit.officehours.core.service.model.StartTicketRequest;
import gr.hua.dit.officehours.core.service.model.TicketView;

import jakarta.persistence.EntityNotFoundException;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Default implementation of {@link TicketService}.
 *
 * TODO some parts can be reused (e.g., security checks)
 */
@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);

    private static final Set<TicketStatus> ACTIVE = Set.of(TicketStatus.QUEUED, TicketStatus.IN_PROGRESS);

    private final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;
    private final PersonRepository personRepository;
    private final CurrentUserProvider currentUserProvider;
    private final SmsNotificationPort smsNotificationPort;

    public TicketServiceImpl(final TicketMapper ticketMapper,
                             final TicketRepository ticketRepository,
                             final PersonRepository personRepository,
                             final CurrentUserProvider currentUserProvider,
                             final SmsNotificationPort smsNotificationPort) {
        if (ticketMapper == null) throw new NullPointerException();
        if (ticketRepository == null) throw new NullPointerException();
        if (personRepository == null) throw new NullPointerException();
        if (currentUserProvider == null) throw new NullPointerException();
        if (smsNotificationPort == null) throw new NullPointerException();

        this.ticketMapper = ticketMapper;
        this.ticketRepository = ticketRepository;
        this.personRepository = personRepository;
        this.currentUserProvider = currentUserProvider;
        this.smsNotificationPort = smsNotificationPort;
    }

    private void notifyPerson(final TicketView ticketView, final PersonType type) {
        final String e164;
        if (type == PersonType.TEACHER) {
            e164 = ticketView.teacher().mobilePhoneNumber();
        } else if (type == PersonType.STUDENT) {
            e164 = ticketView.student().mobilePhoneNumber();
        } else {
            throw new RuntimeException("Unreachable");
        }
        final String content = String.format("Ticket %s new status: %s", ticketView.id(), ticketView.status().name());
        final boolean sent = this.smsNotificationPort.sendSms(e164, content);
        if (!sent) {
            LOGGER.warn("SMS send to {} failed", e164);
        }
    }

    @Override
    public Optional<TicketView> getTicket(final Long id) {
        if (id == null) throw new NullPointerException();
        if (id <= 0) throw new IllegalArgumentException();

        // --------------------------------------------------

        final CurrentUser currentUser = this.currentUserProvider.requireCurrentUser();

        // --------------------------------------------------

        final Ticket ticket;
        try {
            ticket = this.ticketRepository.getReferenceById(id);
        } catch (EntityNotFoundException ignored) {
            return Optional.empty();
        }

        // User MUST have access to Ticket.
        // --------------------------------------------------

        final long ticketPersonId;
        if (currentUser.type() == PersonType.TEACHER) {
            ticketPersonId = ticket.getTeacher().getId();
        } else if (currentUser.type() == PersonType.STUDENT) {
            ticketPersonId = ticket.getStudent().getId();
        } else {
            throw new SecurityException("unsupported PersonType");
        }
        if (currentUser.id() != ticketPersonId) {
            return Optional.empty(); // this Ticket does not exist for this user.
        }

        // --------------------------------------------------

        final TicketView ticketView = this.ticketMapper.convertTicketToTicketView(ticket);

        // --------------------------------------------------

        return Optional.of(ticketView);
    }

    @Override
    public List<TicketView> getTickets() {
        final CurrentUser currentUser = this.currentUserProvider.requireCurrentUser();
        final List<Ticket> ticketList;
        if (currentUser.type() == PersonType.TEACHER) {
            ticketList = this.ticketRepository.findAllByTeacherId(currentUser.id());
        } else if (currentUser.type() == PersonType.STUDENT) {
            ticketList = this.ticketRepository.findAllByStudentId(currentUser.id());
        } else {
            throw new SecurityException("unsupported PersonType");
        }
        return ticketList.stream()
            .map(this.ticketMapper::convertTicketToTicketView)
            .toList();
    }

    @Override
    public TicketView openTicket(@Valid final OpenTicketRequest openTicketRequest, final boolean notify) {
        if (openTicketRequest == null) throw new NullPointerException();

        // Unpack.
        // --------------------------------------------------

        final long studentId = openTicketRequest.studentId();
        final long teacherId = openTicketRequest.teacherId();
        final String subject = openTicketRequest.subject();
        final String studentContent = openTicketRequest.studentContent();

        // --------------------------------------------------

        final Person student = this.personRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("student not found"));
        final Person teacher = this.personRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("teacher not found"));

        // --------------------------------------------------

        if (student.getType() != PersonType.STUDENT) {
            throw new IllegalArgumentException("studentId must refer to a STUDENT");
        }
        if (teacher.getType() != PersonType.TEACHER) {
            throw new IllegalArgumentException("teacherId must refer to a TEACHER");
        }

        // Security
        // --------------------------------------------------

        final CurrentUser currentUser = this.currentUserProvider.requireCurrentUser();
        if (currentUser.type() != PersonType.STUDENT) {
            throw new SecurityException("Student type/role required");
        }
        if (currentUser.id() != studentId) {
            throw new SecurityException("Authenticated student does not match the ticket's studentId");
        }

        // Rules
        // --------------------------------------------------

        // Rule 1: student may have at most one active ticket with this teacher.
        if (this.ticketRepository.existsByStudentIdAndTeacherIdAndStatusIn(studentId, teacherId, ACTIVE)) {
            throw new RuntimeException("Student already has an active ticket with this teacher");
        }

        // Rule 2: student can open max 4 active tickets in total.
        final long activeCount = this.ticketRepository.countByStudentIdAndStatusIn(studentId, ACTIVE);
        if (activeCount >= 4) {
            throw new RuntimeException("Student has reached the limit of 4 active tickets");
        }

        // --------------------------------------------------

        Ticket ticket = new Ticket();
        // ticket.setId(); // auto-generated
        ticket.setStudent(student);
        ticket.setTeacher(teacher);
        ticket.setStatus(TicketStatus.QUEUED);
        ticket.setSubject(subject);
        ticket.setStudentContent(studentContent);
        ticket.setQueuedAt(Instant.now());
        ticket = this.ticketRepository.save(ticket);

        // --------------------------------------------------

        final TicketView ticketView = this.ticketMapper.convertTicketToTicketView(ticket);

        // --------------------------------------------------

        if (notify) {
            this.notifyPerson(ticketView, PersonType.TEACHER);
        }

        // --------------------------------------------------

        return ticketView;
    }

    @Override
    public TicketView startTicket(@Valid final StartTicketRequest startTicketRequest) {
        if (startTicketRequest == null) throw new NullPointerException();

        // Unpack.
        // --------------------------------------------------

        final long ticketId = startTicketRequest.id();

        // --------------------------------------------------

        final Ticket ticket = this.ticketRepository.findById(ticketId)
            .orElseThrow(() -> new IllegalArgumentException("Ticket does not exist"));

        // Security.
        // --------------------------------------------------

        final long teacherId = ticket.getTeacher().getId();
        final CurrentUser currentUser = this.currentUserProvider.requireCurrentUser();
        if (currentUser.type() != PersonType.TEACHER) {
            throw new SecurityException("Teacher type/role required");
        }
        if (currentUser.id() != teacherId) {
            throw new SecurityException("Authenticated teacher does not match the ticket's teacherId");
        }

        // Rules.
        // --------------------------------------------------

        if (ticket.getStatus() != TicketStatus.QUEUED) {
            throw new IllegalArgumentException("Only QUEUED tickets can be started");
        }

        // --------------------------------------------------

        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setInProgressAt(Instant.now());

        // --------------------------------------------------

        final Ticket savedTicket = this.ticketRepository.save(ticket);

        // --------------------------------------------------

        final TicketView ticketView = this.ticketMapper.convertTicketToTicketView(savedTicket);

        // --------------------------------------------------

        this.notifyPerson(ticketView, PersonType.STUDENT);

        // --------------------------------------------------

        return ticketView;
    }

    @Override
    public TicketView completeTicket(@Valid final CompleteTicketRequest completeTicketRequest) {
        if (completeTicketRequest == null) throw new NullPointerException();

        // Unpack.
        // --------------------------------------------------

        final long ticketId = completeTicketRequest.id();
        final String teacherContent = completeTicketRequest.teacherContent();

        // --------------------------------------------------

        final Ticket ticket = this.ticketRepository.findById(ticketId)
            .orElseThrow(() -> new IllegalArgumentException("Ticket does not exist"));

        // Security
        // --------------------------------------------------

        final long teacherId = ticket.getTeacher().getId();
        final CurrentUser currentUser = this.currentUserProvider.requireCurrentUser();
        if (currentUser.type() != PersonType.TEACHER) {
            throw new SecurityException("Teacher role/type required");
        }
        if (currentUser.id() != teacherId) {
            throw new SecurityException("Authenticated teacher does not match the ticket's teacherId");
        }

        // Rules
        // --------------------------------------------------

        if (ticket.getStatus() != TicketStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Only IN_PROGRESS tickets can be completed");
        }

        // --------------------------------------------------

        ticket.setStatus(TicketStatus.COMPLETED);
        ticket.setTeacherContent(teacherContent);
        ticket.setCompletedAt(Instant.now());

        // --------------------------------------------------

        final Ticket savedTicket = this.ticketRepository.save(ticket);

        // --------------------------------------------------

        final TicketView ticketView = this.ticketMapper.convertTicketToTicketView(savedTicket);

        // --------------------------------------------------

        this.notifyPerson(ticketView, PersonType.STUDENT);

        // --------------------------------------------------

        return ticketView;
    }
}
