package br.com.nogueira.servicedesk.repository;

import br.com.nogueira.servicedesk.database.ConnectionFactory;
import br.com.nogueira.servicedesk.exception.DatabaseException;
import br.com.nogueira.servicedesk.model.Customer;
import br.com.nogueira.servicedesk.model.Priority;
import br.com.nogueira.servicedesk.model.Ticket;
import br.com.nogueira.servicedesk.model.TicketStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketRepository {
    private final ConnectionFactory connectionFactory;

    public TicketRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Ticket save(Ticket ticket) {
        String sql = "INSERT INTO tickets (title, description, priority, status, customer_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, ticket.getTitle());
            statement.setString(2, ticket.getDescription());
            statement.setString(3, ticket.getPriority().name());
            statement.setString(4, ticket.getStatus().name());
            statement.setInt(5, ticket.getCustomer().getId());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    ticket.setId(keys.getInt(1));
                }
            }
            return ticket;
        } catch (SQLException exception) {
            throw new DatabaseException("Nao foi possivel salvar o chamado.", exception);
        }
    }

    public List<Ticket> findAll() {
        String sql = """
                SELECT t.id, t.title, t.description, t.priority, t.status, t.created_at, t.updated_at,
                       c.id AS customer_id, c.name, c.email, c.department
                FROM tickets t
                INNER JOIN customers c ON c.id = t.customer_id
                ORDER BY t.created_at DESC, t.id DESC
                """;
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Ticket> tickets = new ArrayList<>();
            while (resultSet.next()) {
                tickets.add(mapTicket(resultSet));
            }
            return tickets;
        } catch (SQLException exception) {
            throw new DatabaseException("Nao foi possivel listar chamados.", exception);
        }
    }

    public Optional<Ticket> findById(int id) {
        String sql = """
                SELECT t.id, t.title, t.description, t.priority, t.status, t.created_at, t.updated_at,
                       c.id AS customer_id, c.name, c.email, c.department
                FROM tickets t
                INNER JOIN customers c ON c.id = t.customer_id
                WHERE t.id = ?
                """;
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapTicket(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException exception) {
            throw new DatabaseException("Nao foi possivel buscar chamado.", exception);
        }
    }

    public void updateStatus(int ticketId, TicketStatus status) {
        String sql = "UPDATE tickets SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, ticketId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DatabaseException("Nao foi possivel atualizar status.", exception);
        }
    }

    public long countByStatus(TicketStatus status) {
        String sql = "SELECT COUNT(*) AS total FROM tickets WHERE status = ?";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getLong("total") : 0;
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Nao foi possivel contar chamados.", exception);
        }
    }

    private Ticket mapTicket(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer(
                resultSet.getInt("customer_id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("department")
        );
        return new Ticket(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                Priority.valueOf(resultSet.getString("priority")),
                TicketStatus.valueOf(resultSet.getString("status")),
                customer,
                parseDate(resultSet.getString("created_at")),
                parseDate(resultSet.getString("updated_at"))
        );
    }

    private LocalDateTime parseDate(String value) {
        return LocalDateTime.parse(value.replace(" ", "T"));
    }
}
