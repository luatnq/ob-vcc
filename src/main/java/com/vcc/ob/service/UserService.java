package com.vcc.ob.service;

import com.vcc.ob.data.dto.request.UserRequestDTO;
import com.vcc.ob.data.dto.request.UserSearchRequestDTO;
import com.vcc.ob.data.dto.response.BaseResponse;

import java.sql.SQLException;

public interface UserService {

    BaseResponse createUser(UserRequestDTO userRequestDTO) throws SQLException;

    BaseResponse updateUser(UserRequestDTO userRequestDTO, String userId) throws SQLException;

    BaseResponse deleteUser(String userId) throws SQLException;

    BaseResponse findByUserId(String userId) throws SQLException;

    BaseResponse searchUser(String name, String address, int pageNum, int pageSize) throws SQLException;

    BaseResponse createUsers() throws SQLException;

    BaseResponse getUsersByUserId(UserSearchRequestDTO userSearchRequestDTO) throws SQLException;

}
