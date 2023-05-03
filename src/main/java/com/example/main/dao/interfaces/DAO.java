package com.example.main.dao.interfaces;

import com.example.main.model.User;

import java.util.List;

public interface DAO<dataType> {
    dataType get(Integer id);
    List<dataType> getAll();

    dataType get(String Name, String passwd);

    int create(User user);//wrapping customer and user information
    boolean update(dataType t);
    boolean delete(dataType t);
    boolean delete(Integer id);
}
