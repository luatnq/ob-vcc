package com.vcc.ob.service.impl;

import com.vcc.ob.constant.MessageResponse;
import com.vcc.ob.dao.UserDAO;
import com.vcc.ob.dao.impl.UserDAOImpl;
import com.vcc.ob.data.dto.request.UserRequestDTO;
import com.vcc.ob.data.dto.response.BaseResponse;
import com.vcc.ob.data.dto.response.UserResponseDTO;
import com.vcc.ob.data.entity.User;
import com.vcc.ob.exception.EntityExistedException;
import com.vcc.ob.exception.NotFoundException;
import com.vcc.ob.repository.UserRepository;
import com.vcc.ob.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Override
//    @Transactional
    public BaseResponse createUser(UserRequestDTO userRequestDTO) {

        User userReq = new User(userRequestDTO, this.generateUserId());

        if (!this.checkUserIdExist(userReq.getUserId())) {

            UserDAO userDAO = new UserDAOImpl();
            return new BaseResponse(MessageResponse.USER_INFO, new UserResponseDTO(userDAO.insertUser(userReq)));

        } else {
            throw new EntityExistedException();
        }

    }

    @Override
    public BaseResponse updateUser(UserRequestDTO userRequestDTO, String userId) {
        if (checkUserIdExist(userId)) {
            UserDAO userDAO = new UserDAOImpl();

            User userExisted = userDAO.getUserByUserId(userId);
            User userUpdate = new User(userExisted, userRequestDTO);

            userDAO.updateUser(userUpdate);

            return new BaseResponse(MessageResponse.SUCCESS);

        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public BaseResponse deleteUser(String userId) {

        if (checkUserIdExist(userId)) {

            UserDAO userDAO = new UserDAOImpl();
            User user = userDAO.getUserByUserId(userId);
            user.setDeleted(true);
            userDAO.updateUser(user);
            return new BaseResponse(MessageResponse.SUCCESS);

        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public BaseResponse findByUserId(String userId) {

        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.getUserByUserId(userId);

        if (Objects.isNull(user)) throw new NotFoundException();

        return new BaseResponse(MessageResponse.SUCCESS, new UserResponseDTO(user));
    }

    @Override
    public BaseResponse searchUser(String name, String address) {

        UserDAO userDAO = new UserDAOImpl();
        List<User> users = userDAO.searchUserByNameOrAddress(name, address);

        return new BaseResponse(MessageResponse.SUCCESS, users);
    }


    private String generateUserId() {
        return UUID.randomUUID().toString();
    }

    private boolean checkUserIdExist(String userId) {

        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.getUserByUserId(userId);
        if (Objects.nonNull(user)) {
            return true;
        }
        return false;
    }

}
