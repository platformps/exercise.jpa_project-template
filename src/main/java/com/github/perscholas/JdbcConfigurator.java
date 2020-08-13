package com.github.perscholas;

import com.github.perscholas.utils.DirectoryReference;
import com.github.perscholas.utils.FileReader;
import org.mariadb.jdbc.Driver;

import java.io.File;
import java.sql.DriverManager;
/**
 * Created by leon on 2/18/2020.
 */
public class JdbcConfigurator {
    static {
        try {
            DriverManager.registerDriver(Driver.class.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
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
