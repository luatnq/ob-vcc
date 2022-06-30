package com.vcc.ob.service.impl;

import com.vcc.ob.constant.MessageResponse;
import com.vcc.ob.dao.UserDAO;
import com.vcc.ob.dao.impl.UserDataSourceImpl;
import com.vcc.ob.data.dto.request.UserRequestDTO;
import com.vcc.ob.data.dto.response.BaseResponse;
import com.vcc.ob.data.dto.response.UserResponseDTO;
import com.vcc.ob.data.entity.User;
import com.vcc.ob.exception.EntityExistedException;
import com.vcc.ob.exception.NotFoundException;
//import com.vcc.ob.mapper.MapStructMapper;
import com.vcc.ob.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
//    @Transactional
    public BaseResponse createUser(UserRequestDTO userRequestDTO) throws SQLException {

        User userReq = new User(userRequestDTO, this.generateUserId());
        if (this.checkUserIdExist(userReq.getUserId())) throw new EntityExistedException();

        UserDAO userDAO = new UserDataSourceImpl();
        return new BaseResponse(MessageResponse.USER_INFO, new UserResponseDTO(userDAO.insertUser(userReq)));

    }

    @Override
    public BaseResponse updateUser(UserRequestDTO userRequestDTO, String userId) throws SQLException {
        if (!checkUserIdExist(userId)) throw new NotFoundException();
        UserDAO userDAO = new UserDataSourceImpl();

        User userExisted = userDAO.getUserByUserId(userId);
        User userUpdate = new User(userExisted, userRequestDTO);
        userDAO.updateUser(userUpdate);

        return new BaseResponse(MessageResponse.SUCCESS);

    }

    @Override
    public BaseResponse deleteUser(String userId) throws SQLException {

        if (!checkUserIdExist(userId)) throw new NotFoundException();

        UserDAO userDAO = new UserDataSourceImpl();
        User user = userDAO.getUserByUserId(userId);
        user.setDeleted(true);
        userDAO.updateUser(user);
        return new BaseResponse(MessageResponse.SUCCESS);

    }

    @Override
    public BaseResponse findByUserId(String userId) throws SQLException {

        UserDAO userDAO = new UserDataSourceImpl();
        User user = userDAO.getUserByUserId(userId);

        if (Objects.isNull(user)) throw new NotFoundException();

        return new BaseResponse(MessageResponse.SUCCESS, new UserResponseDTO(user));
    }

    @Override
    public BaseResponse searchUser(String name, String address) throws SQLException {

        UserDAO userDAO = new UserDataSourceImpl();
        List<User> users = userDAO.searchUserByNameOrAddress(name, address);

        return new BaseResponse(MessageResponse.SUCCESS, users);
    }


    @Override
    public BaseResponse createUsers() throws SQLException {
        UserDAO userDAO = new UserDataSourceImpl();

        for (int i = 0; i < 1000; i++) {
            userDAO.insertBatchUser();
        }
        return new BaseResponse(MessageResponse.SUCCESS);
    }

    private String generateUserId() {
        return UUID.randomUUID().toString();
    }

    private boolean checkUserIdExist(String userId) throws SQLException {

        UserDAO userDAO = new UserDataSourceImpl();
        User user = userDAO.getUserByUserId(userId);
        if (Objects.nonNull(user)) {
            return true;
        }
        return false;
    }

}
