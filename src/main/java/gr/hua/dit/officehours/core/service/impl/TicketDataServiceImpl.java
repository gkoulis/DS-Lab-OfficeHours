package gr.hua.dit.officehours.core.service.impl;

import gr.hua.dit.officehours.core.model.Ticket;
import gr.hua.dit.officehours.core.repository.TicketRepository;
import gr.hua.dit.officehours.core.service.TicketDataService;
import gr.hua.dit.officehours.core.service.mapper.TicketMapper;
import gr.hua.dit.officehours.core.service.model.TicketView;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default implementation of {@link TicketDataService}.
 */
@Service
public class TicketDataServiceImpl implements TicketDataService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketDataServiceImpl(final TicketRepository ticketRepository,
                                 final TicketMapper ticketMapper) {
        if (ticketRepository == null) throw new NullPointerException();
        if (ticketMapper == null) throw new NullPointerException();
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    @Override
    public List<TicketView> getAllTickets() {
        final List<Ticket> ticketList = this.ticketRepository.findAll();
        final List<TicketView> ticketViewList = ticketList
            .stream()
            .map(this.ticketMapper::convertTicketToTicketView)
            .toList();
        return ticketViewList;
    }
}
