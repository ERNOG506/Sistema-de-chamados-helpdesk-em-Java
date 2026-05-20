package br.com.nogueira.servicedesk.database;

import br.com.nogueira.servicedesk.exception.DatabaseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    private final ConnectionFactory connectionFactory;

    public DatabaseInitializer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void initialize() {
        String schema = loadSchema();
        try (Connection connection = connectionFactory.getConnection(); Statement statement = connection.createStatement()) {
            for (String command : schema.split(";")) {
                if (!command.isBlank()) {
                    statement.execute(command);
                }
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Nao foi possivel inicializar o banco de dados.", exception);
        }
    }

    private String loadSchema() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("schema.sql")),
                StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception exception) {
            throw new DatabaseException("Arquivo schema.sql nao encontrado.", exception);
        }
    }
}
