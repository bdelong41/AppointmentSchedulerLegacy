package com.example.scheduler.models;
/**
 * Stores the contact information such as id and name.
 */
public class Contact {
    private String contactName;
    private String email;
    private final Integer id;

    /**
     * default constructor of Contact.
     * @param id - Sets the contact id.
     */
    public Contact(Integer id) {
        this.id = id;
    }

    /**
     * @return contactName Returns the contact name.
     */
    public String getContactName() {
        return contactName;
    }
    /**
     * @return id Returns the contact id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return email Returns the contact's email.
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param contactName Sets the contact name.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    /**
     * @param email Sets the contact name.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return Returns the contact's name.
     */
    @Override
    public String toString(){
        return contactName;
    }
}
