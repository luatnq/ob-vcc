package com.vcc.ob.service.impl;

import com.vcc.ob.dao.PersonDAO;
import com.vcc.ob.data.entity.Person;
import com.vcc.ob.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonDAO personDAO;

    @Override
    public void updatePerson(String personName, int personId) throws SQLException {
        personDAO.updatePerson(personName, personId);
    }

    @Override
    public Person selectPerson(int personId) throws SQLException {
        return personDAO.selectPerson(personId);
    }

}
