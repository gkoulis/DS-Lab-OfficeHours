package gr.hua.dit.officehours.web.ui;

import gr.hua.dit.officehours.core.security.CurrentUserProvider;
import gr.hua.dit.officehours.core.service.TicketService;
import gr.hua.dit.officehours.core.service.model.OpenTicketRequest;
import gr.hua.dit.officehours.core.service.model.StartTicketRequest;
import gr.hua.dit.officehours.core.service.model.TicketView;

import gr.hua.dit.officehours.web.ui.model.OpenTicketForm;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * UI controller for managing tickets.
 */
@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final CurrentUserProvider currentUserProvider;
    private final TicketService ticketService;

    public TicketController(final CurrentUserProvider currentUserProvider,
                            final TicketService ticketService) {
        if (currentUserProvider == null) throw new NullPointerException();
        if (ticketService == null) throw new NullPointerException();

        this.currentUserProvider = currentUserProvider;
        this.ticketService = ticketService;
    }

    @GetMapping("")
    public String list(final Model model) {
        final List<TicketView> ticketViewList = this.ticketService.getTickets();
        model.addAttribute("tickets", ticketViewList);
        return "tickets";
    }

    @GetMapping("/{ticketId}")
    public String detail(@PathVariable final Long ticketId, final Model model) {
        final TicketView ticketView = this.ticketService.getTicket(ticketId).orElse(null);
        if (ticketId == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Ticket not found");
        }
        model.addAttribute("ticket", ticketView);
        model.addAttribute("completeTicketForm", null); // TODO Set.
        return "ticket";
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/new")
    public String showOpenForm(final Model model) {
        // form initial data
        final OpenTicketForm openTicketForm = new OpenTicketForm(null, "", "");
        model.addAttribute("form", openTicketForm);
        return "new_ticket";
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/new")
    public String handleOpenForm(
        @ModelAttribute("form") @Valid final OpenTicketForm openTicketForm,
        final BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "new_ticket";
        }
        final OpenTicketRequest openTicketRequest = new OpenTicketRequest(
            this.currentUserProvider.requiredStudentId(), // The current user must be Student. We need their ID.
            openTicketForm.teacherId(),
            openTicketForm.subject(),
            openTicketForm.studentContent()
        );
        final TicketView ticketView = this.ticketService.openTicket(openTicketRequest);
        return "redirect:/tickets/" + ticketView.id();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/{ticketId}/start")
    public String handleStartForm(@PathVariable final Long ticketId) {
        final StartTicketRequest startTicketRequest = new StartTicketRequest(ticketId);
        final TicketView ticketView = this.ticketService.startTicket(startTicketRequest);
        return "redirect:/tickets/" + ticketView.id();
    }
}
