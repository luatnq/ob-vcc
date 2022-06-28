package com.vcc.ob.service;

import com.vcc.ob.data.dto.request.UserRequestDTO;
import com.vcc.ob.data.dto.response.BaseResponse;

public interface UserService {

    BaseResponse createUser(UserRequestDTO userRequestDTO);

    BaseResponse updateUser(UserRequestDTO userRequestDTO, String userId);

    BaseResponse deleteUser(String userId);

    BaseResponse findByUserId(String userId);

    BaseResponse searchUser(String name, String address);

}
