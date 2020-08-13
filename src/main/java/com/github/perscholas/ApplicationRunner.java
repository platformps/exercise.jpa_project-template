package com.github.perscholas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

public class ApplicationRunner implements Runnable {

    @Override
    public void run() {
        DatabaseConnection dbConnection = DatabaseConnection.PRODUCTION_DATABASE;
        ResultSet rs = dbConnection.executeQuery("SELECT * FROM production_database.person;");
        printResults(rs);
    }

    public void printResults(ResultSet resultSet) {
        try {
            for (Integer rowNumber = 0; resultSet.next(); rowNumber++) {
                String firstColumnData = resultSet.getString(1);
                String secondColumnData = resultSet.getString(2);
                String thirdColumnData = resultSet.getString(3);
                System.out.println(new StringJoiner("\n")
                        .add("\nRow number = " + rowNumber.toString())
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
