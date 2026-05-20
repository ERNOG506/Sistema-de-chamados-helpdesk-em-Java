package br.com.nogueira.servicedesk;

import br.com.nogueira.servicedesk.database.ConnectionFactory;
import br.com.nogueira.servicedesk.database.DatabaseInitializer;
import br.com.nogueira.servicedesk.repository.CustomerRepository;
import br.com.nogueira.servicedesk.repository.TicketRepository;
import br.com.nogueira.servicedesk.service.CustomerService;
import br.com.nogueira.servicedesk.service.TicketService;
import br.com.nogueira.servicedesk.service.ValidationService;
import br.com.nogueira.servicedesk.ui.MainFrame;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        Files.createDirectories(Path.of("data"));

        ConnectionFactory connectionFactory = new ConnectionFactory();
        new DatabaseInitializer(connectionFactory).initialize();

        ValidationService validationService = new ValidationService();
        CustomerRepository customerRepository = new CustomerRepository(connectionFactory);
        TicketRepository ticketRepository = new TicketRepository(connectionFactory);
        CustomerService customerService = new CustomerService(customerRepository, validationService);
        TicketService ticketService = new TicketService(ticketRepository, customerService, validationService);

        new MainFrame(ticketService).showWindow();
    }
}
