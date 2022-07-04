package com.vcc.ob.dao.impl;

import com.vcc.ob.constant.ErrorMessageConstant;
import com.vcc.ob.dao.DataSource;
import com.vcc.ob.dao.UserDAO;
import com.vcc.ob.data.entity.User;
import com.vcc.ob.exception.QueryFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class UserDAOImpl implements UserDAO {

    private final String INSERT_USER = "INSERT INTO users (address, age, is_deleted, name, user_id) VALUES (?,?,?,?,?) ";
    private final String UPDATE_USER = "UPDATE users as u SET u.address = ?, u.age = ?, u.name = ?, u.is_deleted = ?, u.money = ?  WHERE u.id = ? ";
    private final String QUERY_GET_USER_BY_UID = "SELECT * FROM users as u WHERE u.user_id  = ? ";
    private final String SEARCH_USER_BY_NAME_OR_ADDRESS = "SELECT * FROM users u WHERE ( ? is null OR  u.name LIKE ? ) " +
            " and ( ? is null OR u.address LIKE ? ) ORDER BY u.name "
            + "LIMIT ?,? ";

    private final String SEARCH_USER_BY_USER_ID = "select * from users u where u.user_id in (?)";

    private final String SEARCH_USER_BY_NAME = "SELECT * FROM users u WHERE ( ? is null OR  u.name LIKE ? ) ORDER BY u.name "
            + "LIMIT ?,? ";
    private final String SEARCH_USER_FULL_TEXT = "SELECT u.*  FROM users u " +
            "where MATCH (u.name) AGAINST ( ? IN BOOLEAN MODE) LIMIT ?,? ";
    private final int PAGE_SIZE_DEFAULT = 10;


    public User insertUser(User user) throws SQLException {

        Connection conn = null;


        try {

            conn = DataSource.getInstance().getConnection();
            System.out.println("Inserting record into table users....");
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_USER);

            preparedStatement.setString(1, user.getAddress());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setBoolean(3, user.isDeleted());
            preparedStatement.setString(4, user.getName());
            preparedStatement.setString(5, user.getUserId());

            int row = preparedStatement.executeUpdate();
            conn.commit();
            System.out.println("Row affected: " + row);

            return this.getUserByUserId(user.getUserId());

        } catch (SQLException e) {
            conn.rollback();
            throw new QueryFailException();
        } finally {
            if (Objects.nonNull(conn)) {
                conn.close();
            }
        }
    }

    public User updateUser(User user) throws SQLException {

        Connection conn = null;
        try {

            conn = DataSource.getInstance().getConnection();
            System.out.println("Updating record:...");

            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER);

            preparedStatement.setString(1, user.getAddress());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setBoolean(4, user.isDeleted());
            preparedStatement.setLong(5, user.getMoney());
            preparedStatement.setLong(6, user.getId());

            int row = preparedStatement.executeUpdate();
            conn.commit();
            System.out.println("Row affected: " + row);

            return this.getUserByUserId(user.getUserId());

        } catch (SQLException e) {
            conn.rollback();
            throw new QueryFailException();
        } finally {
            if (Objects.nonNull(conn)) {
                conn.close();
            }
        }
    }

    public void updateUserNonResponse(User user) throws SQLException {

        Connection conn = null;

        try {

            conn = DataSource.getInstance().getConnection();

            System.out.println("Updating record:...");

            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER);

            preparedStatement.setString(1, user.getAddress());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setBoolean(4, user.isDeleted());
            preparedStatement.setLong(5, user.getMoney());
            preparedStatement.setLong(6, user.getId());

            int row = preparedStatement.executeUpdate();
            conn.commit();
            System.out.println("Row affected: " + row);

        } catch (SQLException e) {
            conn.rollback();
            log.error("(Exception) message: {}", ErrorMessageConstant.QUERY_DATABASE_FAIL);
            throw new QueryFailException();
        } finally {
            if (Objects.nonNull(conn)) {
                conn.close();
            }
        }
    }

    public User getUserByUserId(String userId) throws SQLException {

        Connection conn = null;

        try {

            conn = DataSource.getInstance().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(QUERY_GET_USER_BY_UID);
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getLong("id"),
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("age"),
                        rs.getBoolean("is_deleted"),
                        rs.getLong("money"));

                return user;
            }
            return null;

        } catch (SQLException e) {
            throw new QueryFailException();
        } finally {
            if (Objects.nonNull(conn)) {
                conn.close();
            }
        }
    }

    public List<User> searchUserByNameOrAddress(String name, String address) throws SQLException {

        Connection conn = null;
        try {

            conn = DataSource.getInstance().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(SEARCH_USER_BY_NAME_OR_ADDRESS);
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
            if (Objects.nonNull(conn)) {
                conn.close();
            }
        }
    }

    public List<User> searchUsersByUserId(List<String> userIds) throws SQLException {

        Connection conn = null;

        try {

            conn = DataSource.getInstance().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    this.insertParamQuery(userIds, QUERY_GET_USER_BY_UID));

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

            if (Objects.nonNull(conn)) {
                conn.close();
            }
        }
    }

    public List<User> searchUserByName(String name, int pageNum, int pageSize) throws SQLException {

        Connection conn = null;

        try {

            conn = DataSource.getInstance().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(SEARCH_USER_FULL_TEXT);

            preparedStatement.setString(1, "*" + name);
            preparedStatement.setInt(2, pageNum * pageSize);
            preparedStatement.setInt(3, pageSize);

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
            System.out.println(users.size());
            return users;

        } catch (SQLException e) {

            throw new QueryFailException();

        } finally {

            if (Objects.nonNull(conn)) {
                conn.close();
            }

        }
    }


    public void insertBatchUser() throws SQLException {
        String[] names = {"Luat", "Nam", "Ha", "Mai", "Trong", "Viet", "Trinh", "Xoan", "Hong", "Dai"};
        String[] firstName = {"Nguyen", "Le", "Dang", "Ta", "Bui", "Vu", "Dao", "Luong", "Tran", "Nghiem"};
        String[] middleName = {"Huy", "Thi", "Van", "Thanh", "Quoc", "Viet", "Quang", "Nguyet", "Hong", "Dai"};
        String[] address = {"Ha Noi", "Hai Phong", "Hai Duong", "Nam Dinh", "Quang Ninh", "Cao Bang", "Thanh Hoa", "Sai Gon", "Quang tri", "Quang Binh"};

        Connection conn = null;

        try {

            conn = DataSource.getInstance().getConnection();

            System.out.println("Inserting record into table users....");

            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_USER);
            conn.setAutoCommit(false);

            for (int i = 0; i < 10000; i++) {

                preparedStatement.setString(1, address[(int) Math.floor(Math.random() * (9 + 1))]);
                preparedStatement.setInt(2, (int) Math.floor(Math.random() * (100 - 1 + 1) + 1));
                preparedStatement.setBoolean(3, false);
                preparedStatement.setString(4, firstName[(int) Math.floor(Math.random() * (9 + 1))] + " " +
                        middleName[(int) Math.floor(Math.random() * (9 + 1))] + " " + names[(int) Math.floor(Math.random() * (9 + 1))]);
                preparedStatement.setString(5, UUID.randomUUID().toString());
                preparedStatement.addBatch();
            }

            int[] rows = preparedStatement.executeBatch();
            System.out.println("Row affected: " + rows.length);
            conn.commit();

        } catch (SQLException e) {

            conn.rollback();
            throw new QueryFailException();

        } finally {

            if (Objects.nonNull(conn)) {
                conn.close();
            }

        }
    }

    private String insertParamQuery(List<String> userIds, String queryInput) {

        String sqlIN = userIds.stream().collect(Collectors.joining("', '", "('", "')"));
        return queryInput.replace("(?)", sqlIN);

    }
}
