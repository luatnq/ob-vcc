package com.vcc.ob.dao.impl;

import com.vcc.ob.dao.DataSource;
import com.vcc.ob.dao.PersonDAO;
import com.vcc.ob.data.entity.Person;
import com.vcc.ob.data.entity.User;
import com.vcc.ob.exception.QueryFailException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Repository
public class PersonDAOImpl implements PersonDAO {

    private final String UPDATE_PERSON = "UPDATE persons p set p.name = ? where p.id = ? ";
    private final String SELECT_PERSON = "SELECT persons p where p.id = ? ";

    public void updatePerson(String personName, int personId) throws SQLException {
        Connection conn = null;

        try {

            conn = DataSource.getInstance().getConnection();
            conn.setAutoCommit(false);

            PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_PERSON);
            preparedStatement.setString(1, personName);
            preparedStatement.setInt(2, personId);

            int row = preparedStatement.executeUpdate();
            System.out.println("Row updated: " + row);

            Thread.sleep(100);

            conn.commit();

        } catch (SQLException e) {

            conn.rollback();
            throw new QueryFailException();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {

            if (Objects.nonNull(conn)) {
                conn.close();
            }

        }
    }

    public Person selectPerson(int personId) throws SQLException {
        Connection conn = null;

        try {

            conn = DataSource.getInstance().getConnection();
            conn.setAutoCommit(false);

            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_PERSON);
            preparedStatement.setInt(1, personId);
//            preparedStatement.executeQuery();
            ResultSet rs = preparedStatement.executeQuery();

            Person person = null;
            if (rs.next()){
                person = new Person(
                        rs.getInt("id"),
                        rs.getString("name"));
            }

            Thread.sleep(100);

            conn.commit();
            return person;

        } catch (SQLException e) {

            conn.rollback();
            throw new QueryFailException();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {

            if (Objects.nonNull(conn)) {
                conn.close();
            }

        }
    }
}
