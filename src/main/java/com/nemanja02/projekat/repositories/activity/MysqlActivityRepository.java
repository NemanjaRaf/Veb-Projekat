package com.nemanja02.projekat.repositories.activity;

import com.nemanja02.projekat.entities.Activity;
import com.nemanja02.projekat.repositories.MySqlAbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MysqlActivityRepository extends MySqlAbstractRepository implements ActivityRepository{
    @Override
    public Activity addActivity(Activity activity) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};
            preparedStatement = connection.prepareStatement("INSERT INTO activities (naziv, opis) VALUES(?, ?)", generatedColumns);
            preparedStatement.setString(1, activity.getName());
            preparedStatement.setString(2, activity.getDescription());

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                activity.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return activity;
    }

    @Override
    public List<Activity> getActivities() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Activity> activities = new ArrayList<>();

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM activities");

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                activities.add(new Activity(resultSet.getInt("id"), resultSet.getString("naziv"), resultSet.getString("opis")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return activities;
    }

    @Override
    public void deleteActivity(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM activities WHERE id = ?");
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
    public Activity getActivityByID(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Activity activity = null;

        System.out.println("getActivityByID");
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM activities WHERE id = ?");
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                activity = new Activity(resultSet.getInt("id"), resultSet.getString("naziv"), resultSet.getString("opis"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            System.out.println(1);
            this.closeStatement(preparedStatement);
            System.out.println(2);
            this.closeResultSet(resultSet);
            System.out.println(3);
            this.closeConnection(connection);
        }

        System.out.println(activity);

        return activity;
    }
}
