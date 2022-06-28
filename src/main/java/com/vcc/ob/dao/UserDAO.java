package com.vcc.ob.dao;

import com.vcc.ob.data.entity.User;

import java.util.List;

public interface UserDAO {
    User insertUser(User user);

    User getUserByUserId(String userId);

    User updateUser(User user);

    List<User> searchUserByNameOrAddress(String name, String address);
}
