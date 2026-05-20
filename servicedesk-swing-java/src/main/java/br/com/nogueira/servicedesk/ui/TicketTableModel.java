package br.com.nogueira.servicedesk.ui;

import br.com.nogueira.servicedesk.model.Ticket;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TicketTableModel extends AbstractTableModel {
    private final String[] columns = {"ID", "Status", "Prioridade", "Solicitante", "Departamento", "Titulo"};
    private List<Ticket> tickets = new ArrayList<>();

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
        fireTableDataChanged();
    }

    public Ticket getTicketAt(int row) {
        return tickets.get(row);
    }

    @Override
    public int getRowCount() {
        return tickets.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ticket ticket = tickets.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> ticket.getId();
            case 1 -> ticket.getStatus().getLabel();
            case 2 -> ticket.getPriority().getLabel();
            case 3 -> ticket.getCustomer().getName();
            case 4 -> ticket.getCustomer().getDepartment();
            case 5 -> ticket.getTitle();
            default -> "";
        };
    }
}
