package com.github.perscholas;

import com.github.perscholas.utils.ConnectionBuilder;
import com.github.perscholas.utils.IOConsole;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by leon on 2/18/2020.
 */
public enum DatabaseConnection implements DatabaseConnectionInterface {
    PRODUCTION_DATABASE,
    TESTING_DATABASE;

    private static final IOConsole console = new IOConsole(IOConsole.AnsiColor.CYAN);
    private final ConnectionBuilder connectionBuilder;

    DatabaseConnection(ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }

    DatabaseConnection() {
        this(new ConnectionBuilder()
                .setUser("root")
                .setPassword("")
                .setPort(3360)
                .setDatabaseVendor("mariadb")
                .setHost("127.0.0.1"));
    }

    @Override
    public String getDatabaseName() {
        return name().toLowerCase();
    }

    @Override
    public Connection getDatabaseConnection() {
        return connectionBuilder
                .setDatabaseName(getDatabaseName())
                .build();
    }

    @Override
    public Connection getDatabaseEngineConnection() {
        return connectionBuilder.build();
    }

    @Override
    public void create() {
        try {
            executeStatement("CREATE DATABASE IF NOT EXISTS " + getDatabaseName() + ";");
        } catch (Exception sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public void drop() {
        executeStatement("DROP DATABASE IF EXISTS " + getDatabaseName() + ";");
    }

    @Override
    public void use() {
        executeStatement("USE DATABASE " + getDatabaseName() + ";");
    }

    @Override
    public void executeStatement(String sqlStatement) {
        console.println("Attempting to execute statement:\n\t%s", sqlStatement);
        Statement statement = getScrollableStatement();
        try {
            statement.execute(sqlStatement);
        } catch (SQLException e) {
            console.println("Unsuccessfully executed statement:\n\t%s", sqlStatement);
            throw new RuntimeException(e);
        }
        console.println("Successfully executed statement:\n\t%s", sqlStatement);
    }

    @Override
    public ResultSet executeQuery(String sqlQuery) {
        console.println("Attempting to execute query:\n\t%s", sqlQuery);
        Statement statement = getScrollableStatement();
        ResultSet rs;
        try {
            rs = statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            console.println("Unsuccessfully executed query:\n\t%s", sqlQuery);
            throw new RuntimeException(e);
        }
        console.println("Successfully executed query:\n\t%s", sqlQuery);
        return rs;
    }


    private Statement getScrollableStatement() {
        int resultSetType = ResultSet.TYPE_SCROLL_INSENSITIVE;
        int resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;
        try {
            return getDatabaseConnection().createStatement(resultSetType, resultSetConcurrency);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}