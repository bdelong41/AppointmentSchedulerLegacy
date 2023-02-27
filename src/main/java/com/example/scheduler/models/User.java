package com.example.scheduler.models;

import java.sql.Timestamp;
/**
 * Stores the user's name and password as well as some customer information that the user is currently altering.
 */
public class User {
    private final Integer id;
    private String name;
    private String passwd;
    private Timestamp createDate;
    private Timestamp lastUpdate;
    private String lastUpdateAuthor;

    private Integer custID = null;//for use with creating an appointment in AppointmentDAOImpl
    /**
     * Constructor. Initializes all data members.
     * @param id User id.
     * @param name User name.
     * @param passwd User password.
     * @param create_Date Date the user record was created in the database.
     * @param lastUpdate Date the user record was last updated.
     * @param lastUpdateAuthor Author who last altered the user's record.
     */
    public User(Integer id, String name, String passwd,
                Timestamp create_Date,
                Timestamp lastUpdate, String lastUpdateAuthor) {
        this.id = id;
        this.name = name;
        this.passwd = passwd;
        createDate = create_Date;
        this.lastUpdate = lastUpdate;
        this.lastUpdateAuthor = lastUpdateAuthor;
    }
    /**
     * @return id Returns user's id.
     */
    public Integer getId() {
        return id;
    }
    /**
     * @return name Returns user's name.
     */
    public String getName() {
        return name;
    }
    /**
     * @return passwd Returns user's password.
     */
    public String getPasswd() {
        return passwd;
    }
    /**
     * @return createDate Returns the date the user was created in the database.
     */
    public Timestamp getCreateDate() {
        return createDate;
    }
    /**
     * @return lastUpdate Returns the timestamp of when the user's record was last updated on the database.
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
    /**
     * @return lastUpdateAuthor Returns the user who last updated the user's record on the database.
     */
    public String getLastUpdateAuthor() {
        return lastUpdateAuthor;
    }
    /**
     * getCustID is used to encapsulate user information due to data type limitations of the DAO interface.
     * @return custID Returns the id of the customer currently being served by the user.
     */
    public Integer getCustID() {
        return custID;
    }
    /**
     * setCustID helps to encapsulate user information to help overcome the data type limitations of the
     * DAO interface.
     * @param custID initializes the customer id the user is currently serving.
     */
    public void setCustID(Integer custID) {
        this.custID = custID;
    }
}
