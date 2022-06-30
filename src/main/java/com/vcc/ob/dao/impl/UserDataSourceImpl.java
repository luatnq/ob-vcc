package com.vcc.ob.dao.impl;

import com.vcc.ob.dao.DataSource;
import com.vcc.ob.dao.UserDAO;
import com.vcc.ob.data.entity.User;
import com.vcc.ob.exception.QueryFailException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Repository
public class UserDataSourceImpl implements UserDAO {

    private final String INSERT_USER = "INSERT INTO users (address, age, is_deleted, name, user_id) VALUES (?,?,?,?,?) ";
    private final String UPDATE_USER = "UPDATE users as u SET u.address = ?, u.age = ?, u.name = ?, u.is_deleted = ? WHERE u.id = ? ";
    private final String QUERY_GET_USER_BY_UID = "SELECT * FROM users as u WHERE u.user_id  = ? ";
    private final String SEARCH_USER_BY_NAME_OR_ADDRESS = "SELECT * FROM users u WHERE ( ? is null OR  u.name LIKE ? ) " +
            " and ( ? is null OR u.address LIKE ? ) ORDER BY u.name "
             + "LIMIT ?,? ";

    private final String SEARCH_USER_BY_NAME = "SELECT * FROM users u WHERE ( ? is null OR  u.name LIKE ? ) ORDER BY u.name "
            + "LIMIT ?,? ";
    private final int PAGE_SIZE_DEFAULT = 10;


    public User insertUser(User user) throws SQLException {
        try(Connection conn = DataSource.getConnection()){

            System.out.println("Inserting record into table users....");
            conn.setAutoCommit(false);
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
            DataSource.getConnection().rollback();
            throw new QueryFailException();
        } finally {
            if (Objects.nonNull(DataSource.getConnection())) {
                DataSource.getConnection().close();
            }
        }
    }

    public User updateUser(User user) throws SQLException {
        try(Connection conn = DataSource.getConnection()){
            System.out.println("Updating record:...");

            conn.setAutoCommit(false);
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
            DataSource.getConnection().rollback();
            throw new QueryFailException();
        }
    }
    public User getUserByUserId(String userId) throws SQLException {
        try(Connection conn = DataSource.getConnection()){
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
        } finally {
            if (Objects.nonNull(DataSource.getConnection())) {
                DataSource.getConnection().close();
            }
        }
    }

    public List<User> searchUserByNameOrAddress(String name, String address) throws SQLException {

        try {

            PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(SEARCH_USER_BY_NAME_OR_ADDRESS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, "%" + name + "%");
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, "%" + address + "%");

            preparedStatement.setFetchSize(20);
            ResultSet rs = preparedStatement.executeQuery();
            rs.setFetchSize(20);

            List<User> users = new ArrayList<>();
            while (rs.next()) {
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
        } finally {
            if (Objects.nonNull(DataSource.getConnection())) {
                DataSource.getConnection().close();
            }
        }
    }

    public List<User> searchUserByName(String name, int pageNum, int pageSize) throws SQLException {

        try {

            PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(SEARCH_USER_BY_NAME_OR_ADDRESS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, "%" + name + "%");
            preparedStatement.setInt(3, pageNum*pageSize);
            preparedStatement.setInt(4, pageSize);

            ResultSet rs = preparedStatement.executeQuery();

            List<User> users = new ArrayList<>();
            while (rs.next()) {
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
        } finally {
            if (Objects.nonNull(DataSource.getConnection())) {
                DataSource.getConnection().close();
            }
        }
    }

    public void insertBatchUser() throws SQLException {
        String[] names = {"Luat", "Nam", "Ha", "Mai", "Trong", "Viet", "Trinh", "Xoan", "Hong", "Dai"};
        String[] address = {"Ha Noi", "Hai Phong", "Hai Duong", "Nam Dinh", "Quang Ninh", "Cao Bang", "Thanh Hoa", "Sai Gon", "Quang tri", "Quang Binh"};
        try(Connection conn = DataSource.getConnection()){

            System.out.println("Inserting record into table users....");

            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_USER);
            conn.setAutoCommit(false);

            for (int i = 0; i< 10000; i++) {

                preparedStatement.setString(1, address[(int) Math.floor(Math.random() * (9 + 1))]);
                preparedStatement.setInt(2, (int) Math.floor(Math.random() * (100 - 1 + 1) + 1));
                preparedStatement.setBoolean(3, false);
                preparedStatement.setString(4, names[(int) Math.floor(Math.random() * (9 + 1))]);
                preparedStatement.setString(5, UUID.randomUUID().toString());
                preparedStatement.addBatch();
            }

            int[] rows = preparedStatement.executeBatch();
            System.out.println("Row affected: " + rows.length);
            conn.commit();

        } catch (SQLException e) {
            DataSource.getConnection().rollback();
            throw new QueryFailException();
        }
    }
}
