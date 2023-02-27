package com.example.scheduler.data;

import com.example.scheduler.data.interfaces.UserDAO;
import com.example.scheduler.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Retrieves user record information from the database with a specified id or returns all user information.
 * The function {@link #get(String, String)} is used to verify if login credentials match that of an existing user.*/

public class UserDAOImpl implements UserDAO {
    private Connection con;
    /**
     * Retrieves record of a user with the given id and creates a new User object.
     * @param id - used to compare to all User_ID fields.
     * @return user - stores the retrieved user information.
     */
    @Override
    public User get(Integer id) {
        User usr = null;
        try {
            con = Data.getConnection();//verifying connection is active
            Statement st = con.createStatement();
            String query = "select * from users where User_ID = " + id + ";";
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
            {
                Timestamp stamp = rs.getTimestamp(4);
                usr = new User(rs.getInt("UserID"), rs.getString("User_Name"),
                        rs.getString("Password"), rs.getTimestamp("Create_Date"),
                        rs.getTimestamp("Last_Update"), rs.getString("Last_Updated_By"));

//                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usr;
    }
    /**
     * Retrieves all users inside the users table
     * @return ls - ArrayList of User objects
     */
    @Override
    public List<User> getAll() {
        List<User> ls = new ArrayList<User>();
        User usr = null;
        try {

            Statement st = con.createStatement();
            String query = "select * from users;";
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
            {
                Timestamp stamp = rs.getTimestamp(4);
                usr = new User(rs.getInt("UserID"), rs.getString("User_Name"),
                        rs.getString("Password"), rs.getTimestamp("Create_Date"),
                        rs.getTimestamp("Last_Update"), rs.getString("Last_Updated_By"));

                ls.add(usr);
//                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ls;
    }
    /**
     * Retrieves the user with the specified name and password
     * @param Name User's name.
     * @param passwd User's password.
     * @return ls - ArrayList of User objects
     */
    @Override
    public User get(String Name, String passwd) {
        User usr = null;
        try {
            con = Data.getConnection();//verifying connection is active
            Statement st = con.createStatement();

            String query = "select * from users where " +
                    "User_Name  = '" + Name + "' and Password = '" + passwd + "';";
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
            {
                usr = new User(rs.getInt("User_ID"), rs.getString("User_Name"),
                        rs.getString("Password"), rs.getTimestamp("Create_Date"),
                        rs.getTimestamp("Last_Update"), rs.getString("Last_Updated_By"));
//                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usr;
    }
    @Override
    public int create(User user) {
        return 0;
    }
    /**
     * Updates the user record within the database.
     * @param t User object.
     */
    @Override
    public boolean update(User t) {
        if(t == null) return false;
        int rs = 0;
        try {
            con = Data.getConnection();//verifying connection is active
            Statement st = con.createStatement();
            String query = "Update users" +
                    "set User_Name = " + t.getName() +
                    ", Password = " + t.getPasswd() +
                    ", Last_Update = " + t.getLastUpdate() +
                    ", Last_Updated_By = " + t.getLastUpdateAuthor()+
                    " where User_ID = " + t.getId() + ";";
            rs = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(rs > 0) return true;
        return false;
    }
    @Override
    public boolean delete(User t) {
        return false;
    }
    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
