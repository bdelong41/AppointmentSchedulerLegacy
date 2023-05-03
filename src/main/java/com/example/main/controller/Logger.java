package com.example.main.controller;

import java.io.BufferedWriter;
import java.io.File;  // Import the File class
import java.io.IOException;
import java.io.FileWriter;   // Import the FileWriter class
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * records user logins and login times as well as login attempts in "login_activity.txt".
 * Call {@link #loginSucessfull(String, LocalDateTime)} if the user login was successful.
 * Call {@link #loginAttempt(String, LocalDateTime)} if the user login failed.
 */
public class Logger {

    private final String fileName = "login_activity.txt";

    /**
     * Checks if the file exists. If the file doesn't exist then it creates a new file using fileName.
     */
    public void createFile() {
        try {
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when a login was successful.
     * Records the username, and the time and date it took place.
     * Stores date and time as yyyy:mm:dd and hh:mm a.
     * @param userName The user's name.
     * @param ldt The local date and time the login took place.
     */
    public void loginSucessfull(String userName, LocalDateTime ldt) {
        try {
            //setting the date and time formats
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy MM dd");
            String dateFormat = ldt.format(dateTimeFormatter);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            String timeFormat = timeFormatter.format(ldt.toLocalTime());

            String str = "User, " + userName + " successfully logged in at " + dateFormat + " " + timeFormat;

            File file = new File(fileName);
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(str + "\n");
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Called when a login was successful.
     * Records the username, and the time and date it took place.
     * Stores date and time as yyyy:mm:dd and hh:mm a.
     * @param userName The user's name.
     * @param ld The local date and time the login attempt took place.
     */
    public void loginAttempt(String userName, LocalDateTime ld){
        try {
            //setting the date and time formats
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy MM dd");
            String dateFormat = ld.format(dateTimeFormatter);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            String timeFormat = timeFormatter.format(ld.toLocalTime());
            String str = "User, " + userName + " gave invalid login at " + dateFormat + " " + timeFormat;
            File file = new File(fileName);
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(str + "\n");
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
