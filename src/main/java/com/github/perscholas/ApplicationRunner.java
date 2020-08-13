package com.github.perscholas;

import com.github.perscholas.utils.IOConsole;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;
/**
 * Created by leon on 2/18/2020.
 */
public class ApplicationRunner implements Runnable {
    private static final IOConsole console = new IOConsole();

    @Override
    public void run() {
        JdbcConfigurator.initialize();
        DatabaseConnection dbConnection = DatabaseConnection.PRODUCTION_DATABASE;
        dbConnection.drop();
        dbConnection.create();
        dbConnection.use();
        dbConnection.executeStatement(new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS databaseName.pokemonTable(")
                .append("id int auto_increment primary key,")
                .append("name text not null,")
                .append("primary_type int not null,")
                .append("secondary_type int null);").toString());

        dbConnection.executeStatement(new StringBuilder()
                .append("INSERT INTO databaseName.pokemonTable ")
                .append("(id, name, primary_type, secondary_type)")
                .append(" VALUES (12, 'Ivysaur', 3, 7);").toString());

        String query = "SELECT * FROM databaseName.pokemonTable;";
        ResultSet rs = dbConnection.executeQuery(query);
        printResults(rs);
    }

    public void printResults(ResultSet resultSet) {
        try {
            for (Integer rowNumber = 0; resultSet.next(); rowNumber++) {
                String firstColumnData = resultSet.getString(1);
                String secondColumnData = resultSet.getString(2);
                String thirdColumnData = resultSet.getString(3);
                System.out.println(new StringJoiner("\n")
                        .add("Row number = " + rowNumber.toString())
                        .add("First Column = " + firstColumnData)
                        .add("Second Column = " + secondColumnData)
                        .add("Third column = " + thirdColumnData)
                        .toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
