package gr.hua.dit.officehours.core.service.mapper;

import gr.hua.dit.officehours.core.model.Person;
import gr.hua.dit.officehours.core.model.Ticket;
import gr.hua.dit.officehours.core.service.model.PersonView;

import gr.hua.dit.officehours.core.service.model.TicketView;

import org.springframework.stereotype.Component;

/**
 * Mapper to convert {@link Ticket} to {@link TicketView}.
 */
@Component
public class TicketMapper {

    private final PersonMapper personMapper;

    public TicketMapper(final PersonMapper personMapper) {
        if (personMapper == null) throw new NullPointerException();
        this.personMapper = personMapper;
    }

    public TicketView convertTicketToTicketView(final Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        return new TicketView(
            ticket.getId(),
            this.personMapper.convertPersonToPersonView(ticket.getStudent()),
            this.personMapper.convertPersonToPersonView(ticket.getTeacher()),
            ticket.getStatus(),
            ticket.getSubject(),
            ticket.getStudentContent(),
            ticket.getTeacherContent(),
            ticket.getQueuedAt(),
            ticket.getInProgressAt(),
            ticket.getCompletedAt()
        );
    }
}
