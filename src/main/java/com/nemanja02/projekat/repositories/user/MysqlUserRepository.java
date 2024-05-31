package com.nemanja02.projekat.repositories.user;

import com.nemanja02.projekat.entities.User;
import com.nemanja02.projekat.repositories.MySqlAbstractRepository;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MysqlUserRepository extends MySqlAbstractRepository implements UserRepository {
    @Override
    public User addUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (this.emailExists(user.getEmail())) {
            return new User(-1, "", "", "", "", "", "");
        }

        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};
            preparedStatement = connection.prepareStatement("INSERT INTO user (email, ime, prezime, tip, status, password) VALUES(?, ?, ?, ?, ?, ?)", generatedColumns);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setString(4, user.getType());
            preparedStatement.setString(5, user.getStatus());

            String hashedPassword = DigestUtils.sha256Hex(user.getPassword());
            preparedStatement.setString(6, hashedPassword);

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
//            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    private boolean emailExists(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean exists = false;

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return exists;
    }

    @Override
    public User editUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("UPDATE user SET email = ?, ime = ?, prezime = ?, tip = ?, status = ? WHERE id = ?");
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setString(4, user.getType());
            preparedStatement.setString(5, user.getStatus());
            preparedStatement.setInt(6, user.getId());

            preparedStatement.executeUpdate();
//            resultSet = preparedStatement.getGeneratedKeys();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
//            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return this.getUser(user.getId());
    }

    @Override
    public void deleteUser(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }

    @Override
    public List<User> getUsers(Integer page) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<User> users = new ArrayList<>();

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM user ORDER BY id DESC LIMIT ?, 10");
            preparedStatement.setInt(1, (page - 1) * 10);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("id"), resultSet.getString("email"), resultSet.getString("ime"), resultSet.getString("prezime"), resultSet.getString("tip"), resultSet.getString("status"), resultSet.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return users;
    }

    @Override
    public User activateUser(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("UPDATE user SET status = 'aktivan' WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return this.getUser(id);
    }

    @Override
    public User deactivateUser(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("UPDATE user SET status = 'neaktivan' WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return this.getUser(id);
    }

    @Override
    public User getUser(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getInt("id"), resultSet.getString("email"), resultSet.getString("ime"), resultSet.getString("prezime"), resultSet.getString("tip"), resultSet.getString("status"), resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE email = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getInt("id"), resultSet.getString("email"), resultSet.getString("ime"), resultSet.getString("prezime"), resultSet.getString("tip"), resultSet.getString("status"), resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public int getUsersCount() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM user");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return count;
    }
}
