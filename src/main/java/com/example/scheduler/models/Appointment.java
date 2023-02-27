package com.example.scheduler.models;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * Stores appointment information as well as the customer who scheduled the appointment,
 * the user who created it and the contact information.
 */
public class Appointment {
    private final Integer id;
    private String title;
    private String description;
    private String location;
    private String contactName;
    private String type;
    private Integer custID;
    private Integer usrID;
    private Integer contactID;
    private LocalDateTime start;
    private LocalDateTime end;
    private String startTimeOfDay;//start of day interval AM/PM
    private String endTimeOfDay;//end of day interval AM/PM

    /**
     * Default Appointment constructor. Initializes final id.
     * @param id initializes the appointment id
     */
    public Appointment(Integer id) {
        this.id = id;
    }
    /**
     * @return returns the appointment id
     */
    public Integer getId() {
        return id;
    }
    /**
     * @return returns the appointment title.
     */
    public String getTitle() {
        return title;
    }
    /**
     * @return returns the appointment description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @return returns the appointment location.
     */
    public String getLocation() {
        return location;
    }
    /**
     * @return returns the appointment type.
     */
    public String getType() {
        return type;
    }
    /**
     * @return returns the appointment customer id.
     */
    public Integer getCustID() {
        return custID;
    }

    /**
     * @return returns the user id who scheduled the appointment.
     */
    public Integer getUsrID() {
        return usrID;
    }

    /**
     * @return returns the contact name the appointment is scheduled with.
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @return returns the contact id the appointment is scheduled with.
     */
    public Integer getContactID() {
        return contactID;
    }
    /**
     * @return returns the appointment start date and time.
     */
    public LocalDateTime getStart() {
        return start;
    }
    /**
     * @return  returns the appointment end date and time.
     */
    public LocalDateTime getEnd() {
        return end;
    }
    /**
     * @param title Sets the appointment title.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     *
     * @param  description Sets the appointment description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     *
     * @param  location Sets the appointment location.
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /**
     *
     * @param contactName Sets the appointment contactName.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    /**
     *
     * @param type Sets the appointment type.
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     *
     * @param custID Sets the appointment custID.
     */
    public void setCustID(Integer custID) {
        this.custID = custID;
    }
    /**
     *
     * @param  usrID Sets the appointment usrID.
     */
    public void setUsrID(Integer usrID) {
        this.usrID = usrID;
    }
    /**
     *
     * @param  contactID Sets the appointment contactID.
     */
    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }
    /**
     *
     * @param start Sets the appointment start time.
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
        if(this.start.getHour() > 12) startTimeOfDay = "PM";
        else startTimeOfDay = "AM";
    }
    /**
     *
     * @param end - Sets the appointment end time.
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
        if(this.end.getHour() > 12) endTimeOfDay = "PM";
        else endTimeOfDay = "AM";
    }

    /**
     *
     * @return Returns the month the appointment takes place on.
     */
    public String getMonth(){
        return start.getMonth().toString();
    }
    /**
     *
     * @return Returns week of the month the appointment takes place.
     */
    public Integer getWeek(){
        Timestamp timestamp = Timestamp.valueOf(start.toLocalDate().atStartOfDay());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return((Integer) calendar.get(Calendar.WEEK_OF_MONTH));
    }
    /**
     *Called by the appointments table in the Report controller.
     * @return The start date of the appointment.
     */
    public String getStartDate(){
        LocalDate ld = start.toLocalDate();
        return (ld.toString());

    }
    /**
     *Called by the appointments table in the Report controller.
     * @return The start time of the appointment.
     */
    public String getStartTime(){
        LocalTime lt = start.toLocalTime();
        return lt.toString();
    }
    /**
     * Called by the appointments table in the Report controller.
     * @return  The end date of the appointment along with the day interval (AM/PM)
     */
    public String getEndDate(){
        LocalDate lt = end.toLocalDate();
        return (lt.toString() + " " + startTimeOfDay);
    }
    /**
     * Called by the appointments table in the Report controller.
     * @return The end time along with the time of day interval (AM/PM)
     */
    public String getEndTime(){
        LocalTime lt = end.toLocalTime();
        return (lt.toString() + " " + endTimeOfDay);
    }
    /**
     * Called by the appointments table in the Record controller.
     * @return  The end time along with the time of day interval (AM/PM)
     */
    public String getStartString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return start.format(formatter);
    }

    /**
     * Called by the appointments table in the Record controller.
     * @return  The end time along with the time of day interval (AM/PM)
     */
    public String getEndString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return end.format(formatter);
    }

}
