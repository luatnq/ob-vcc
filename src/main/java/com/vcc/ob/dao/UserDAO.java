package com.vcc.ob.dao;

import com.vcc.ob.data.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User insertUser(User user) throws SQLException;

    User getUserByUserId(String userId) throws SQLException;

    User updateUser(User user) throws SQLException;

    List<User> searchUserByNameOrAddress(String name, String address) throws SQLException;

    List<User> searchUserByName(String name, int pageNum, int pageSize) throws SQLException;

    void insertBatchUser() throws SQLException;
}
