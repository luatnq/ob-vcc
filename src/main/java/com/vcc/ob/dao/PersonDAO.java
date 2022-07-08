package com.vcc.ob.dao;

import com.vcc.ob.data.entity.Person;

import java.sql.SQLException;

public interface PersonDAO {
    void updatePerson(String personName, int personId) throws SQLException;

    Person selectPerson(int personId) throws SQLException;
}
