package br.com.nogueira.servicedesk.service;

import br.com.nogueira.servicedesk.exception.BusinessException;
import br.com.nogueira.servicedesk.model.Customer;
import br.com.nogueira.servicedesk.model.Priority;
import br.com.nogueira.servicedesk.model.Ticket;
import br.com.nogueira.servicedesk.model.TicketStatus;
import br.com.nogueira.servicedesk.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TicketService {
    private final TicketRepository ticketRepository;
    private final CustomerService customerService;
    private final ValidationService validationService;

    public TicketService(TicketRepository ticketRepository, CustomerService customerService, ValidationService validationService) {
        this.ticketRepository = ticketRepository;
        this.customerService = customerService;
        this.validationService = validationService;
    }

    public Ticket openTicket(String customerName, String customerEmail, String department,
                             String title, String description, Priority priority) {
        validationService.requireText("Titulo", title, 5);
        validationService.requireText("Descricao", description, 10);
        Customer customer = customerService.createOrFind(customerName, customerEmail, department);
        Ticket ticket = new Ticket(null, title.trim(), description.trim(), priority, TicketStatus.OPEN,
                customer, LocalDateTime.now(), LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    public List<Ticket> listTickets() {
        return ticketRepository.findAll();
    }

    public Ticket findById(int id) {
        validationService.requirePositiveId(id, "ID do chamado");
        return ticketRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Chamado nao encontrado."));
    }

    public void changeStatus(int ticketId, TicketStatus status) {
        findById(ticketId);
        ticketRepository.updateStatus(ticketId, status);
    }

    public long countByStatus(TicketStatus status) {
        return ticketRepository.countByStatus(status);
    }
}
