package com.example.main.dao;
import com.example.main.dao.interfaces.CustomerDAO;
import com.example.main.model.Customer;
import com.example.main.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Customer DAO retrieves customer information either with a specified id or it retrieves all customers from the
 * database. Allows a customer record to be deleted from the database or updated.
 * Use {@link #create(User)} to auto generate a customer id.
 * */
public class CustomerDAOImpl implements CustomerDAO {
    /**
     * Retrieves customer record from database that has the specified id and stores it in
     * a Customer object.
     * @param id - used to compare to all Customer_ID fields.
     * @return customer - stores the customer information.
     */
    @Override
    public Customer get(Integer id) {
        Customer customer = null;
        try {

            DivisionsDAOImpl divisionsDAO = new DivisionsDAOImpl();
            CountryDAOImpl countryDAO = new CountryDAOImpl();
            Connection con = Data.getConnection();//verifying connection is active

            String query = "select * from customers where Customer_ID = ?;";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next())
            {
                //retrieving division information
                String divisionName = divisionsDAO.get(rs.getInt("Division_ID"));
                String countryName = countryDAO.get(divisionsDAO.getCountryid(rs.getInt("Division_ID")));

                customer = new Customer(rs.getString("Customer_Name"), rs.getString("Address"),
                        rs.getString("Phone"), countryName, divisionName,
                        rs.getString("Postal_Code"), rs.getInt("Customer_ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
    /**
     * Retrieves all customer records from the database and stores each record a Customer object.
     * @return customers - ArrayList of Customers.
     */
    @Override
    public List<Customer> getAll(){
        Customer customer = null;
        List<Customer> customers = new ArrayList<Customer>();
        try {
            Connection con = Data.getConnection();//verifying connection is active
            String query = "select * from customers;";
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            //dao models
            DivisionsDAOImpl divisionsDAO = new DivisionsDAOImpl();
            CountryDAOImpl countryDAO = new CountryDAOImpl();

            while(rs.next())
            {
                int divisionID = rs.getInt("Division_ID");
                int countryID = divisionsDAO.getCountryid(divisionID);
                String division = divisionsDAO.get(divisionID);
                String country = countryDAO.get(countryID);
                //creating customer object
                customer = new Customer(rs.getString("Customer_Name"), rs.getString("Address"),
                        rs.getString("Phone"), country, division, rs.getString("Postal_Code")
                        , rs.getInt("Customer_ID"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    @Override
    public Customer get(String Name, String passwd) {
        return null;
    }

    /**
     * Creates initial customer record and returns the auto-generated id.
     * @param user - takes a user object and uses it to fill out the user related fields in customers.
     * @return id - returns the id of the newly created customer, returns -1 if the database failed to create the
     * customer.
     */
    public int create(User user)
    {
        int id = -1;
        try {
            Connection con = Data.getConnection();
            Statement st = con.createStatement();
            String query = "insert into customers(Customer_Name, Address, Postal_Code, Phone, Create_Date, " +
                    "Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                    "values('none', 'none', 'none', 'none', NOW(), 'none', current_timestamp(), '" + user.getName() + "', ?);";
            PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            //getting random division id of existing division from the database
            DivisionsDAOImpl divisionsDAO = new DivisionsDAOImpl();
            preparedStatement.setInt(1, divisionsDAO.getAllDivisions().get(0));

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()) id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }
    /**
     * Updates customer fields in appointments whose record contains the same id as the parameter's.
     * @param t - Takes Customer parameter to update the existing appointment fields with the specified
     * Customer_ID.
     * @return True if the PreparedStatement returned a value greater than 0. Returns false if the
     * value is zero.
     */
    @Override
    public boolean update(Customer t) {
        int rs = 0;
        try {
            Connection con = Data.getConnection();
            CountryDAOImpl countryDAO = new CountryDAOImpl();
            DivisionsDAOImpl divisionsDAO = new DivisionsDAOImpl();
            int countryID = countryDAO.getID(t.getCountry());
            int divisionID = divisionsDAO.getDivisionId(t.getProvince(), countryID);
            String query = "update customers " +
                    "set Customer_Name = ?, " +
                    "Address = ?, " +
                    "Postal_Code = ?, " +
                    "Phone = ?, " +
                    "Last_Update = current_timestamp(), " +
                    "Last_Updated_By = ?, " +
                    "Division_ID = ? " +
                    "where Customer_ID = ?;";

            PreparedStatement st = con.prepareStatement(query);

            st.setString(1, t.getName());
            st.setString(2, t.getAddress());
            st.setString(3, t.getPostalCode());
            st.setString(4, t.getPhone());
            st.setString(5, t.getLastUpdatedBy());
            st.setInt(6, divisionID);
            st.setInt(7, t.getId());
            rs = st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if(rs > 0) return true;
        return false;
    }
    /**
     * Deletes a customer record with the same id as the parameter's.
     * @param t - takes Customer object and uses its id member to delete a record within appointments that has the
     *          same id.
     * @return True if the PreparedStatement returned a value greater than 0. Returns false if the
     *      *          value is zero.
     */
    @Override
    public boolean delete(Customer t) {

        return false;
    }
    /**
     * Deletes a customer record with whose id is equivalent to the parameters
     * @param id - used to compare customer id(s)
     * @return rs - returns true if the PreparedStatement returned a value greater than 0. Returns false if the
     *          value is zero.
     */
    @Override
    public boolean delete(Integer id) {
        int rs = 0;
        try {
            Connection con = Data.getConnection();
            Statement st = con.createStatement();
            String query = "Delete from customers where Customer_ID = '" + id + "';";

            rs = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(rs > 0) return true;
        return false;
    }
}
