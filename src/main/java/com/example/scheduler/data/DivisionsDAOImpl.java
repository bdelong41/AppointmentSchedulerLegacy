package com.example.scheduler.data;

import com.example.scheduler.data.interfaces.FirstLevelDivisionsDAO;
import com.example.scheduler.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves name of the first level division with a specified id.
 * Retrieves all first level divisions recorded within the database.
 * Retrieves country id a division falls under.*/
public class DivisionsDAOImpl implements FirstLevelDivisionsDAO {

    /**
     * Retrieves the division name from the database with the associated id.
     * @param id - used to compare the division id.
     * @return divisionName
     */
    @Override
    public String get(Integer id) {
        String divisionName = "";
        try {

            //result sets
            Connection con = Data.getConnection();//verifying connection is active
            //customer information
            String query = "select Division from first_level_divisions where Division_ID = ?;";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if(rs.next())
            {
                divisionName = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return divisionName;
    }
    /**
     * Retrieves the all division names from the database.
     * @return list - ArrayList of division names.
     */
    @Override
    public List<String> getAll() {
        List<String> list = new ArrayList<String>();

        String division = null;
        try {

            //result sets
            Connection con = Data.getConnection();//verifying connection is active
            //customer information
            Statement st = con.createStatement();
            String query = "select Division from first_level_divisions;";
            ResultSet rs = st.executeQuery(query);

            while(rs.next())
            {
                division = rs.getString(1);
                list.add(division);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * Retrieves the name of the division that falls under a country name.
     * @param country - the name of the country the first level division belongs to.
     * @return list - ArrayList of division names.
     */
    public List<String> get(String country) {
        List<String> list = new ArrayList<String>();

        String division = null;
        try {

            //result sets
            Connection con = Data.getConnection();//verifying connection is active
            //customer information
            Statement st = con.createStatement();
            String query = "select Division from first_level_divisions where Country_ID = " +
                    "(select Country_ID from countries where Country = '" + country + "' limit 1);";
            ResultSet rs = st.executeQuery(query);

            while(rs.next())
            {
                division = rs.getString(1);
                list.add(division);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * Retrieves the country id the division belongs to.
     * @param divisionID - the id of the division.
     * @return countryID
     */
    public int getCountryid(int divisionID) {
        int countryID = -1;
        try {

            //result sets
            Connection con = Data.getConnection();//verifying connection is active
            //customer information
            String query = "select COUNTRY_ID from first_level_divisions where Division_ID = ? limit 1;";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, divisionID);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                countryID = rs.getInt("Country_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryID;
    }

    /**
     * Retrieves the id of the division that has the same name and country id.
     * @param divisionName Division name.
     * @param countryID Country id.
     * @return divisionID.
     */
    public int getDivisionId(String divisionName, int countryID){
        int divisionID = -1;
        try {

            //result sets
            Connection con = Data.getConnection();//verifying connection is active
            //customer information
            String query = "select Division_ID from first_level_divisions where Division = ?  and COUNTRY_ID = ?;";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, divisionName);
            st.setInt(2, countryID);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                divisionID = rs.getInt("Division_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return divisionID;
    }
    /**
     * Retrieves the id of the division that has the same name and country id.
     * @return list List of all countries within the database.
     */
    public List<Integer> getAllDivisions(){
        Connection con = Data.getConnection();
        List<Integer> list = new ArrayList<>();

        try {
            String query = "select Division_ID from first_level_divisions;";
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                list.add(rs.getInt("Division_ID"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return list;
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
