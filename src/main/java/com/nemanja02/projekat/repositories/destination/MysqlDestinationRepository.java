package com.nemanja02.projekat.repositories.destination;

import com.nemanja02.projekat.entities.Destination;
import com.nemanja02.projekat.repositories.MySqlAbstractRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MysqlDestinationRepository  extends MySqlAbstractRepository implements DestinationRepository{
    @Override
    public Destination addDestination(Destination destination) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            String[] columns = {"id"};
            preparedStatement = connection.prepareStatement("INSERT INTO destinations (ime, opis) VALUES(?, ?)", columns);

            preparedStatement.setString(1, destination.getName());
            preparedStatement.setString(2, destination.getDescription());

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                destination.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
//            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return destination;
    }

    @Override
    public Destination getDestination(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Destination destination = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM destinations WHERE id = ?");
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                destination = new Destination(resultSet.getInt("id"), resultSet.getString("ime"), resultSet.getString("opis"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return destination;
    }

    private Integer getArticleCount(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Integer count = 0;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM articles WHERE destinacija_id = ?");
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
//            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        System.out.println(count);

        return count;
    }

    @Override
    public Destination editDestination(Destination destination) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("UPDATE destinations SET ime = ?, opis = ? WHERE id = ?");
            preparedStatement.setString(1, destination.getName());
            preparedStatement.setString(2, destination.getDescription());
            preparedStatement.setInt(3, destination.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
//            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return destination;
    }

    @Override
    public boolean deleteDestination(Integer id) {

        Integer articleCount = this.getArticleCount(id);
        if (articleCount > 0) {
            return false;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM destinations WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return true;
    }



    @Override
    public List<Destination> getDestinations(Integer page) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Destination> destinations = new ArrayList<>();

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM destinations ORDER BY id DESC LIMIT ?, 10");
            preparedStatement.setInt(1, (page - 1) * 10);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                destinations.add(new Destination(resultSet.getInt("id"), resultSet.getString("ime"), resultSet.getString("opis")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return destinations;
    }

    @Override
    public int getDestinationsCount() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int count = 0;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM destinations");
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
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
