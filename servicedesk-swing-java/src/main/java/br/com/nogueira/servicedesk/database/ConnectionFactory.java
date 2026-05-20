package br.com.nogueira.servicedesk.database;

import br.com.nogueira.servicedesk.config.AppConfig;
import br.com.nogueira.servicedesk.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(AppConfig.DATABASE_URL);
        } catch (SQLException exception) {
            throw new DatabaseException("Nao foi possivel conectar ao banco de dados.", exception);
        }
    }
}
