package com.example.scheduler.data.interfaces;
import com.example.scheduler.models.User;

import java.util.List;


/**
 * interface model template used for each dataTable used within the database
 */
public interface DAO<dataType> {
    dataType get(Integer id);
    List<dataType> getAll();

    dataType get(String Name, String passwd);

    int create(User user);//wrapping customer and user information
    boolean update(dataType t);
    boolean delete(dataType t);
    boolean delete(Integer id);
}
