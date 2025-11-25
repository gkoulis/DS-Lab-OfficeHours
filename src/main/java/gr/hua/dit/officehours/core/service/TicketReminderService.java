package gr.hua.dit.officehours.core.service;

import gr.hua.dit.officehours.core.model.Ticket;
import gr.hua.dit.officehours.core.model.TicketStatus;
import gr.hua.dit.officehours.core.port.SmsNotificationPort;
import gr.hua.dit.officehours.core.repository.TicketRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service for managing ticket reminders.
 */
@Service
public class TicketReminderService {

    private final TicketRepository ticketRepository;
    private final SmsNotificationPort smsNotificationPort;

    public TicketReminderService(final TicketRepository ticketRepository,
                                 final SmsNotificationPort smsNotificationPort) {
        if (ticketRepository == null) throw new NullPointerException();
        if (smsNotificationPort == null) throw new NullPointerException();

        this.ticketRepository = ticketRepository;
        this.smsNotificationPort = smsNotificationPort;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void remindTeacherOfStaleQueuedTickets() {
        Instant cutoff = Instant.now().minus(1, ChronoUnit.DAYS);
        final List<Ticket> ticketList = this.ticketRepository.findByStatusAndQueuedAtBefore(TicketStatus.QUEUED, cutoff);
        for (final Ticket ticket : ticketList) {
            final String e164 = ticket.getTeacher().getMobilePhoneNumber();
            final String content = String.format("Reminder: Ticket %s QUEUED", ticket.getId());
            this.smsNotificationPort.sendSms(e164, content);
        }
    }
}
