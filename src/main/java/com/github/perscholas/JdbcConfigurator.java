package com.github.perscholas;

import com.github.perscholas.utils.DirectoryReference;
import com.github.perscholas.utils.FileReader;
import org.mariadb.jdbc.Driver;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by leon on 8/13/2020.
 */
public class JdbcConfigurator {
    static {
        // Attempt to register JDBC Driver
        try {
            DriverManager.registerDriver(Driver.class.newInstance());
        } catch (InstantiationException | IllegalAccessException | SQLException e1) {
            throw new Error(e1);
        }
    }

    private static final DatabaseConnection dbc = DatabaseConnection.PRODUCTION_DATABASE;

    public static void initialize() {
        dbc.drop();
        dbc.create();
        dbc.use();
        executeSqlFile("person.create-table.sql");
        executeSqlFile("person.populate-table.sql");
    }

    private static void executeSqlFile(String fileName) {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory(fileName);
        FileReader fileReader = new FileReader(creationStatementFile.getAbsolutePath());
        String[] statements = fileReader.toString().split(";");
        for (int i = 0; i < statements.length; i++) {
            String statement = statements[i];
            dbc.executeStatement(statement);
        }
    }
}
