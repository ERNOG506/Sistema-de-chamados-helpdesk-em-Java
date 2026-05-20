package br.com.nogueira.servicedesk.model;

public enum TicketStatus {
    OPEN("Aberto"),
    IN_PROGRESS("Em andamento"),
    RESOLVED("Resolvido"),
    CANCELED("Cancelado");

    private final String label;

    TicketStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
