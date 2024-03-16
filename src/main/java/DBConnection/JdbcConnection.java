package DBConnection;

import java.sql.*;

public class JdbcConnection {
    public static void main(String[] args) throws SQLException {

        String host = "localhost";
        String port = "3306";
        //url ["jdbc:mysql://" + host + ":" +  port + "/databasename" ]

         Connection con = DriverManager.getConnection("jdbc:mysql://" + host + ":" +  port + "/demo","root", "root");

         Statement statement = con.createStatement();

        ResultSet resultSet = statement.executeQuery("select * from credentials where scenario = 'zerobalancecard'");

        while(resultSet.next()) {
            System.out.println("Username: "+resultSet.getString("username"));
            System.out.println("Password: "+resultSet.getString("password"));
        }

    }
}