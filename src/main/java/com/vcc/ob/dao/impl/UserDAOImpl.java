package com.vcc.ob.dao.impl;

import com.vcc.ob.dao.DAO;
import com.vcc.ob.dao.UserDAO;
import com.vcc.ob.data.entity.User;
import com.vcc.ob.exception.QueryFailException;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDAOImpl extends DAO implements UserDAO {

    private final String INSERT_USER = "INSERT INTO users (address, age, is_deleted, name, user_id) VALUES (?,?,?,?,?) ";
    private final String UPDATE_USER = "UPDATE users as u SET u.address = ?, u.age = ?, u.name = ?, u.is_deleted = ? WHERE u.id = ? ";
    private final String QUERY_GET_USER_BY_UID = "SELECT * FROM users as u WHERE u.user_id  = ? ";
    private final String SEARCH_USER_BY_NAME_OR_ADDRESS = "SELECT * FROM users u WHERE ( ? is null OR  u.name LIKE ? ) " +
            " and ( ? is null OR u.address LIKE ? ) ORDER BY u.name ";


    public UserDAOImpl(){
        super();
    }

    public User insertUser(User user){
        try{

            System.out.println("Inserting record into table users....");

            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_USER);
            preparedStatement.setString(1, user.getAddress());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setBoolean(3, user.isDeleted());
            preparedStatement.setString(4, user.getName());
            preparedStatement.setString(5, user.getUserId());

            int row = preparedStatement.executeUpdate();
            System.out.println("Row affected: " + row);

            return this.getUserByUserId(user.getUserId());

        } catch (SQLException e) {
            throw new QueryFailException();
        }
    }

    public User updateUser(User user){
        try{
            System.out.println("Updating record:...");

            PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER);
            preparedStatement.setString(1, user.getAddress());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setBoolean(4, user.isDeleted());
            preparedStatement.setLong(5, user.getId());

            int row = preparedStatement.executeUpdate();
            System.out.println("Row affected: " + row);

            return this.getUserByUserId(user.getUserId());
        } catch (SQLException e) {
            throw new QueryFailException();
        }
    }
    public User getUserByUserId(String userId){
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(QUERY_GET_USER_BY_UID);
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                User user = new User(
                        rs.getLong("id"),
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("age"),
                        rs.getBoolean("is_deleted"));

                return user;
            }
            return null;

        } catch (SQLException e) {
            throw new QueryFailException();
        }
    }

    public List<User> searchUserByNameOrAddress(String name, String address){
        try{

            PreparedStatement preparedStatement = conn.prepareStatement(SEARCH_USER_BY_NAME_OR_ADDRESS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, "%" + name + "%");
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, "%" + address + "%");

            ResultSet rs = preparedStatement.executeQuery();

            List<User> users = new ArrayList<>();
            while (rs.next()){
                User user = new User(
                        rs.getLong("id"),
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("age"),
                        rs.getBoolean("is_deleted"));

                users.add(user);
            }
            return users;

        } catch (SQLException e) {
            throw new QueryFailException();
        }
    }

}
