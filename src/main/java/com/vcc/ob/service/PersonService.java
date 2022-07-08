package com.vcc.ob.service;

import com.vcc.ob.data.entity.Person;

import java.sql.SQLException;

public interface PersonService {
    void updatePerson(String personName, int personId) throws SQLException;

    Person selectPerson(int personId) throws SQLException;
}
