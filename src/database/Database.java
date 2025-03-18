package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
// this database use to store the connection with root server
    private static Connection connection = null;
    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
//create the connection  
    public static Connection connect() {
        if(connection == null){
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
    return connection;
    }
//execute query (SELECT)
    public static ResultSet executeQuery(String query){
        try {
            Statement statement = connect().createStatement();
            return statement.executeQuery(query);

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
        return null;
    }
}
//

