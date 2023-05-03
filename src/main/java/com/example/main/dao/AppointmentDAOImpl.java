package com.example.main.dao;
import com.example.main.dao.interfaces.AppointmentDAO;
import com.example.main.model.Appointment;
import com.example.main.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class AppointmentDAOImpl implements AppointmentDAO{
    private Connection con;
    /**
     * Retrieves record of an appointment with the given id and creates a new Appointment object.
     * @param id - used to compare to all Appointment_ID fields.
     * @return app - the object representation of an appointment record within the database.
     */
    @Override
    public Appointment get(Integer id) {
        Appointment app = null;
        try {
            con = Data.getConnection();//verifying connection is active
            Statement st = con.createStatement();
            String query = "select * from appointments where Appointment_ID = '" + id + "';";
            ResultSet rs = st.executeQuery(query);
            if(rs.next())
            {
                app = new Appointment(rs.getInt("Appointment_ID"));
                app.setTitle(rs.getString("Title"));
                app.setDescription(rs.getString("Description"));
                app.setLocation(rs.getString("Location"));
                app.setType(rs.getString("Type"));
                app.setStart(rs.getTimestamp("Start").toLocalDateTime());
                app.setEnd(rs.getTimestamp("End").toLocalDateTime());
                app.setCustID(rs.getInt("Customer_ID"));
                app.setUsrID(rs.getInt("User_ID"));
                app.setContactID(rs.getInt("Contact_ID"));
                //retrieving contact name
                ContactsDAOImpl contactsDAO = new ContactsDAOImpl();
                String contactName = contactsDAO.get(app.getCustID()).getContactName();
                app.setContactName(contactName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return app;
    }

    /**
     * Retrieves all Appointments inside the appointments table
     * @return ls - ArrayList of Appointment objects
     */
    @Override
    public List<Appointment> getAll() {
        Appointment app = null;
        List<Appointment> ls = new ArrayList<Appointment>();
        try {
            con = Data.getConnection();//verifying connection is active
            Statement st = con.createStatement();
            String query = "select * from appointments;";
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
            {
                app = new Appointment(rs.getInt("Appointment_ID"));
                app.setTitle(rs.getString("Title"));
                app.setDescription(rs.getString("Description"));
                app.setLocation(rs.getString("Location"));
                app.setType(rs.getString("Type"));
                app.setStart(rs.getTimestamp("Start").toLocalDateTime());
                app.setEnd(rs.getTimestamp("End").toLocalDateTime());
                app.setCustID(rs.getInt("Customer_ID"));
                app.setUsrID(rs.getInt("User_ID"));
                app.setContactID(rs.getInt("Contact_ID"));
                //retrieving contact name

                //used when the appointment is newly created and lacks an associated contact.
                if(app.getContactID() != null) {
                    if (app.getContactID() > 0) {
                        ContactsDAOImpl contactsDAO = new ContactsDAOImpl();
                        String contactName = contactsDAO.get(app.getContactID()).getContactName();
                        app.setContactName(contactName);
                    }
                }

                ls.add(app);
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
    public Appointment get(String Name, String passwd) {
        return null;
    }
    /**
     * Creates initial appointment record and returns the auto-generated id.
     * @param user - takes a user object and uses it to fill out the user related fields in appointments.
     * @return id - returns the id of the newly created appointment.
     */
    @Override
    public int create(User user) {
        int id = -1;
        try {
            Connection con = Data.getConnection();
            Statement st = con.createStatement();
            String query = "insert into appointments(Title, Description, Location, Type, " +
                    "Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Start, End, Create_Date, Contact_ID)" +
                    "values('', '', '', '', ?, current_timestamp()," +
                    " ?, ?, ?, now(), now(), current_timestamp(), ?);";


            PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setInt(3, user.getCustID());
            preparedStatement.setInt(4, user.getId());

            //setting contact id to random contact until the appointment has been updated
            ContactsDAOImpl contactsDAO = new ContactsDAOImpl();
            preparedStatement.setInt(5, contactsDAO.getAll().get(0).getId());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()) id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    /**
     * Updates appointment fields in appointments whose record contains the same id as the parameter's.
     * @param t - Takes Appointment parameter to update the existing appointment fields with the specified
     * appointment id.
     * @return True if st returned a value greater than 0. Returns false if the
     * value is zero.
     */
    @Override
    public boolean update(Appointment t) {
        Integer rs = 0;
        Connection con = null;
        try {
            con = Data.getConnection();
            String query = "update appointments " +
                    "set Title = ?, " +
                    "Description = ?, " +
                    "Location = ?, " +
                    "Type = ?, " +
                    "Start = ?, " +
                    "End = ?, " +
                    "Last_Update = ?, " +
                    "Last_Updated_By = ?, " +
                    "Customer_ID = ?, " +
                    "User_ID = ?, " +
                    "Contact_ID = ? " +
                    "where Appointment_ID = ? ;";

            PreparedStatement st = con.prepareStatement(query);

            st.setString(1, t.getTitle());
            st.setString(2, t.getDescription());
            st.setString(3, t.getLocation());
            st.setString(4, t.getType());
            st.setTimestamp(5, Timestamp.valueOf(t.getStart()));
            st.setTimestamp(6, Timestamp.valueOf(t.getEnd()));
            st.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            st.setString(8, "Application");
            st.setInt(9, t.getCustID());
            st.setInt(10, t.getUsrID());
            st.setInt(11, t.getContactID());
            st.setInt(12, t.getId());


            rs = st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(rs > 0){
            return true;
        }
        return false;
    }

    /**
     * Deletes an appointment record with the same id as the parameter's.
     * @param t - takes Appointment object and uses its id member to delete a record within appointments that has the
     *          same id.
     * @return True if the PreparedStatement returned a value greater than 0. Returns false if the
     *      *          value is zero.
     */
    @Override
    public boolean delete(Appointment t) {

        int rs = 0;
        try {
            Connection con = Data.getConnection();//verifying connection is active
            Statement st = con.createStatement();
            String query = "delete from appointments where Appointment_ID = '" + t.getId() + "';";
            rs = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(rs > 0) return true;
        return false;
    }
    /**
     * Deletes an appointment record with whose id is equivalent to the parameters
     * @param id - used to compare appointment id(s)
     * @return rs - returns true if the PreparedStatement returned a value greater than 0. Returns false if the
     *          value is zero.
     *
     * Uses appointment DAO to count all appointments associated with a given customer id that also occur after the
     * current date and time.
     */
    @Override
    public boolean delete(Integer id) {
        int rs = 0;
        try {
            con = Data.getConnection();//verifying connection is active
            Statement st = con.createStatement();
            String query = "delete from appointments where Appointment_ID = '" + id + "';";

            rs = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(rs > 0) return true;
        return false;
    }
    /**
     * Retrieves the number of appointments that are associated with a customer.
     * @param customerID The customer id that is associated with other appointments.
     * @return occurrences - returns the number of appointments associated with the customer who scheduled them.
     */
    public Integer countCustomerAppointments(Integer customerID){
        Integer occurrences = -1;
        try {
            con = Data.getConnection();//verifying connection is active
            String query = "select count(Appointment_ID) from appointments " +
                    "where Customer_ID = '" + customerID + "';";
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if(rs.next()) occurrences = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return occurrences;
    }
    /**
     * Retrieves all distinct appointment types in the appointments table.
     * @return ls The list of distinct types.
     * */
    public List<String> getAllTypes(){
        List<String> ls = new ArrayList<>();
        try {
            con = Data.getConnection();//verifying connection is active
            Statement st = con.createStatement();
            String query = "select distinct Type from appointments;";
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
            {
                ls.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ls;
    }
}
