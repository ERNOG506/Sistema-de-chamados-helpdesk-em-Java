package br.com.nogueira.servicedesk.model;

import java.time.LocalDateTime;

public class Ticket {
    private Integer id;
    private final String title;
    private final String description;
    private final Priority priority;
    private TicketStatus status;
    private final Customer customer;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Ticket(Integer id, String title, String description, Priority priority, TicketStatus status,
                  Customer customer, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.customer = customer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
