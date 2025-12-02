package gr.hua.dit.officehours.core.service.model;

import gr.hua.dit.officehours.core.model.TicketStatus;
import gr.hua.dit.officehours.core.service.TicketBusinessLogicService;

import java.time.Instant;

/**
 * General view of {@link gr.hua.dit.officehours.core.model.Ticket} entity.
 *
 * @see gr.hua.dit.officehours.core.model.Ticket
 * @see TicketBusinessLogicService
 */
public record TicketView(
    long id,
    PersonView student,
    PersonView teacher,
    TicketStatus status,
    String subject,
    String studentContent,
    String teacherContent,
    Instant queuedAt,
    Instant inProgressAt,
    Instant completedAt
) {}
