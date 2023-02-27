package com.example.scheduler.models;
/**
 * Stores customer name, id, location and contact information.
 */
public class Customer {
    private String name;
    private String address;
    private String phone;

    private String country;
    private String province;
    private String postalCode;

    private String createdBy;

    private String lastUpdatedBy;

    //private String createdBy;
    private final Integer id;

    /**
     * default Customer constructor, initializes all customer data members.
     * @param name Customer name.
     * @param address Customer's address.
     * @param phone Customer's phone number.
     * @param country Customer's country.
     * @param province Customer's first level division.
     * @param postalCode Customer's postal code.
     * @param id Customer's record id.
     */
    public Customer(String name, String address, String phone, String country, String province, String postalCode,
                    Integer id) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.country = country;
        this.province = province;
        this.postalCode = postalCode;
        this.id = id;

    }
    /**
     * @return Returns customer name.
     */
    public String getName() {
        return name;
    }
    /**
     * @return Returns customer address.
     */
    public String getAddress() {
        return address;
    }
    /**
     * @return Returns customer phone.
     */
    public String getPhone() {
        return phone;
    }
    /**
     * @return Returns customer first level division.
     */
    public String getProvince() {
        return province;
    }
    /**
     * @return Returns customer postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }
    /**
     * @return Returns customer id.
     */
    public Integer getId() {
        return id;
    }
    /**
     * @return Returns customer's country.
     */
    public String getCountry() {
        return country;
    }
    /**
     * @return Returns the username who last updated the customers information on the database.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }
    /**
     * @param name Sets customer's name.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @param createdBy records the username who created the customer record on the database.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * @param lastUpdatedBy records the username who last updated the customer's information.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    /**
     * @param country sets the customer's country name.
     */
    public void setCountry(String country) {
        this.country = country;
    }
}

/*

StartTime
EndTime
Title
Description
Location
Contact
Type
StartDate
EndDate
Submit
Cancel
 */
