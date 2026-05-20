package br.com.nogueira.servicedesk.model;

public enum Priority {
    LOW("Baixa"),
    MEDIUM("Media"),
    HIGH("Alta"),
    CRITICAL("Critica");

    private final String label;

    Priority(String label) {
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
