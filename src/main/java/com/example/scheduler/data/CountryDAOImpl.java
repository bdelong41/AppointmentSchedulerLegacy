package com.example.scheduler.data;

import com.example.scheduler.data.interfaces.CountryDAO;
import com.example.scheduler.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves the Country information from the database that has the specified name/id.
 * */
public class CountryDAOImpl implements CountryDAO {
    /**
     * Retrieves the country name from the countries datatable with the specified id.
     * @param id - used to compare to all Appointment_ID fields.
     * @return countryName
     */
    @Override
    public String get(Integer id) {
        String countryName = "";
        try {
            Connection con = Data.getConnection();//verifying connection is active
            String query = "select Country from countries where Country_ID = ?;";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next())
            {
                countryName = rs.getString("Country");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryName;
    }

    /**
     * Retrieves all country names from the countries datatable.
     * @return list - ArrayList of country names.
     */
    @Override
    public List<String> getAll() {
        List<String> list = new ArrayList<String>();
        try {
            Connection con = Data.getConnection();//verifying connection is active
            //country information
            Statement st = con.createStatement();
            String query = "select Country from countries;";
            ResultSet rs = st.executeQuery(query);

            while(rs.next())
            {
                list.add(rs.getString("Country"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * Retrieves country id of the country with the specified name.
     * @param name Country name.
     * @return list - ArrayList of country names.
     */
    public int getID(String name){
        int countryID = -1;
        try {
            //result sets
            Connection con = Data.getConnection();//verifying connection is active
            //customer information
            String query = "select Country_ID from countries where Country = ?;";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            if(rs.next())
            {
                countryID = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryID;
    }

    @Override
    public String get(String Name, String passwd) {
        return null;
    }
    @Override
    public int create(User user) {
        return 0;
    }
    @Override
    public boolean update(String t) {
        return false;
    }
    @Override
    public boolean delete(String t) {
        return false;
    }
    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
