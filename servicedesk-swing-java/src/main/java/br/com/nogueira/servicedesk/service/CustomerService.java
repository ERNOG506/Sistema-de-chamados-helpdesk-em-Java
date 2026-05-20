package br.com.nogueira.servicedesk.service;

import br.com.nogueira.servicedesk.model.Customer;
import br.com.nogueira.servicedesk.repository.CustomerRepository;

public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationService validationService;

    public CustomerService(CustomerRepository customerRepository, ValidationService validationService) {
        this.customerRepository = customerRepository;
        this.validationService = validationService;
    }

    public Customer createOrFind(String name, String email, String department) {
        validationService.requireText("Nome", name, 3);
        validationService.validateEmail(email);
        validationService.requireText("Departamento", department, 2);

        String normalizedEmail = email.trim().toLowerCase();
        return customerRepository.findByEmail(normalizedEmail)
                .orElseGet(() -> customerRepository.save(new Customer(null, name.trim(), normalizedEmail, department.trim())));
    }
}
