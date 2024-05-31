package com.nemanja02.projekat.repositories.comment;

import com.nemanja02.projekat.entities.Comment;
import com.nemanja02.projekat.repositories.MySqlAbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MysqlCommentRepository extends MySqlAbstractRepository implements CommentRepository {

    @Override
    public Comment addComment(Comment comment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("INSERT INTO comments (autor, tekst, article_id) VALUES(?, ?, ?)");
            preparedStatement.setString(1, comment.getAuthor());
            preparedStatement.setString(2, comment.getText());
            preparedStatement.setInt(3, comment.getArticle_id());

            preparedStatement.executeUpdate();


            try (ResultSet result = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    comment.setId(result.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return comment;
    }

    @Override
    public List<Comment> getCommentsByArticleID(Integer article_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Comment> comments = new ArrayList<>();

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM comments WHERE article_id = ? ORDER BY id DESC");
            preparedStatement.setInt(1, article_id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Comment comment = new Comment(resultSet.getInt("id"), resultSet.getString("autor"), resultSet.getString("tekst"), resultSet.getString("vreme"), resultSet.getInt("article_id"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return comments;
    }
}
