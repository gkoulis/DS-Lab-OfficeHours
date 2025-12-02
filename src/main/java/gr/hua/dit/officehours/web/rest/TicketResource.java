package gr.hua.dit.officehours.web.rest;

import gr.hua.dit.officehours.core.service.TicketDataService;
import gr.hua.dit.officehours.core.service.model.TicketView;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing {@code Ticket} resource.
 */
@RestController
@RequestMapping("/api/v1/ticket")
public class TicketResource {

    private final TicketDataService ticketDataService;

    public TicketResource(final TicketDataService ticketDataService) {
        if (ticketDataService == null) throw new NullPointerException();
        this.ticketDataService = ticketDataService;
    }

    @GetMapping("")
    public List<TicketView> tickets() {
        final List<TicketView> ticketViewList = this.ticketDataService.getAllTickets();
        return ticketViewList;
    }
}
