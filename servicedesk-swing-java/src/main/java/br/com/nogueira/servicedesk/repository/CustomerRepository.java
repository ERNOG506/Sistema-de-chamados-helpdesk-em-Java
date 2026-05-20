package br.com.nogueira.servicedesk.repository;

import br.com.nogueira.servicedesk.database.ConnectionFactory;
import br.com.nogueira.servicedesk.exception.DatabaseException;
import br.com.nogueira.servicedesk.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class CustomerRepository {
    private final ConnectionFactory connectionFactory;

    public CustomerRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Customer save(Customer customer) {
        String sql = "INSERT INTO customers (name, email, department) VALUES (?, ?, ?)";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getDepartment());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    customer.setId(keys.getInt(1));
                }
            }
            return customer;
        } catch (SQLException exception) {
            throw new DatabaseException("Nao foi possivel salvar o solicitante.", exception);
        }
    }

    public Optional<Customer> findByEmail(String email) {
        String sql = "SELECT id, name, email, department FROM customers WHERE email = ?";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapCustomer(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException exception) {
            throw new DatabaseException("Nao foi possivel buscar o solicitante.", exception);
        }
    }

    private Customer mapCustomer(ResultSet resultSet) throws SQLException {
        return new Customer(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("department")
        );
    }
}
