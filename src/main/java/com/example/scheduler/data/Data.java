package com.example.scheduler.data;
import java.sql.*;


/**
 * Creates database connection using jdbc driver.
 * Data is used as the sole method to creating a connection to the client_schedule database
 */
public class Data {


    private static final String url = "jdbc:mysql://localhost:3306/client_schedule?connectionTimeZone=SERVER";
    private static final String uname = "sqlUser";
    private static final String passwd = "Passw0rd!";
    private static Connection con;


    /**
     * Creates static database connection upon object creation.
     */
    static
    {
        try {
            con = DriverManager.getConnection(url, uname, passwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /**
     * Static function.
     * checks if connection is still active.
     * if the connection is closed then it creates a new connection.
     * @return Connection.
     */
    public static Connection getConnection() {
        try {
            if (con == null) return null;
            if (con.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(url, uname, passwd);
            }
            return con;
        }catch (SQLException| ClassNotFoundException e){
            e.printStackTrace();
        }
        return con;
    }

}


