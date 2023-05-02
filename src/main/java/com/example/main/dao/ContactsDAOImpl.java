package com.example.main.dao;

import com.example.main.dao.interfaces.ContactsDAO;
import com.example.main.model.Contact;
import com.example.main.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactsDAOImpl implements ContactsDAO{
    private Connection con;

    /**
     * Retrieves record of a contact with the given id and creates a new Contact object.
     * @param id - used to compare to all Contact_ID fields.
     * @return contact - the object representation of an appointment record within the database.
     */
    @Override
    public Contact get(Integer id) {
        Contact contact = null;
        try {
            con = Data.getConnection();//verifying connection is active
            String query = "select Contact_ID, Contact_Name, Email from contacts where Contact_ID = ?;";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next())
            {
                contact = new Contact(rs.getInt(1));
                contact.setContactName(rs.getString(2));
                contact.setEmail(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contact;
    }

    /**
     * Retrieves all contacts inside the contacts table
     * @return ls - ArrayList of Contact objects
     */
    @Override
    public List<Contact> getAll() {
        Contact contact;
        List<Contact> ls = new ArrayList<Contact>();
        try {
            con = Data.getConnection();//verifying connection is active
            Statement st = con.createStatement();
            String query = "select Contact_ID, Contact_Name, Email from contacts;";
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
            {
                contact = new Contact(rs.getInt("Contact_ID"));
                contact.setContactName(rs.getString("Contact_Name"));
                contact.setEmail(rs.getString("Email"));
                ls.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ls;
    }

    /**
     *Unused method from DAO interface.
     * @param Name - used by UserDaoImpl.
     * @param passwd - used by UserDaoImpl.
     * @return null - returns null value as the function doesn't execute anything meaningful
     */
    @Override
    public Contact get(String Name, String passwd) {
        return null;
    }
    /**
     * Retrieves contacts information using a given name
     * @param name The name of the contact.
     * @return contact The contact being retrieved.
     */
    public Contact get(String name){
        Contact contact = null;
        try {
            con = Data.getConnection();//verifying connection is active
            String query = "select Contact_ID, Contact_Name, Email from contacts where Contact_Name = ? limit 1;";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1,name);
            ResultSet rs = st.executeQuery();
            if(rs.next())
            {
                contact = new Contact(rs.getInt("Contact_ID"));
                contact.setContactName(rs.getString("Contact_Name"));
                contact.setEmail(rs.getString("Email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contact;
    }
    @Override
    public int create(User user) {
        return 0;
    }
    @Override
    public boolean update(Contact t) {
        return false;
    }
    @Override
    public boolean delete(Contact t) {
        return false;
    }
    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
