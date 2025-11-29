package gr.hua.dit.officehours.core.service;

import gr.hua.dit.officehours.core.model.Ticket;
import gr.hua.dit.officehours.core.security.CurrentUser;
import gr.hua.dit.officehours.core.service.model.CompleteTicketRequest;
import gr.hua.dit.officehours.core.service.model.OpenTicketRequest;
import gr.hua.dit.officehours.core.service.model.StartTicketRequest;
import gr.hua.dit.officehours.core.service.model.TicketView;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing {@link Ticket}.
 *
 * <p><strong>All methods MUST be {@link CurrentUser}-aware.</strong></p>
 */
public interface TicketService {

    Optional<TicketView> getTicket(final Long id);

    List<TicketView> getTickets();

    TicketView openTicket(final OpenTicketRequest openTicketRequest, final boolean notify);

    default TicketView openTicket(final OpenTicketRequest openTicketRequest) {
        return this.openTicket(openTicketRequest, true);
    }

    TicketView startTicket(final StartTicketRequest startTicketRequest);

    TicketView completeTicket(final CompleteTicketRequest completeTicketRequest);
}
