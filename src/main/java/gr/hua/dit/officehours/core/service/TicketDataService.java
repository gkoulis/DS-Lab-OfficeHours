package gr.hua.dit.officehours.core.service;

import gr.hua.dit.officehours.core.service.model.TicketView;

import java.util.List;

/**
 * Service for managing {@code Ticket} for data analytics purposes.
 */
public interface TicketDataService {

    List<TicketView> getAllTickets();
}
