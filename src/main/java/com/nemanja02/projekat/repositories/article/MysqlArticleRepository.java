package com.nemanja02.projekat.repositories.article;

import com.nemanja02.projekat.entities.Activity;
import com.nemanja02.projekat.entities.Article;
import com.nemanja02.projekat.repositories.MySqlAbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MysqlArticleRepository extends MySqlAbstractRepository implements ArticleRepository{
    @Override
    public List<Article> getArticles(Integer page) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Article> articles = new ArrayList<>();

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT a.*, GROUP_CONCAT(ac.id) ac_id, GROUP_CONCAT(ac.naziv) ac_naziv, GROUP_CONCAT(ac.opis) ac_opis, u.email FROM articles a LEFT JOIN article_activities aa ON (a.id = aa.article_id) LEFT JOIN activities ac ON(ac.id = aa.activity_id) LEFT JOIN user u ON (a.autor_id = u.id) GROUP BY a.id ORDER BY a.vreme DESC LIMIT 10 OFFSET ?");
            preparedStatement.setInt(1, (page - 1) * 10);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                List<Activity> activities = new ArrayList<>();
                List<Integer> ac_ids = new ArrayList<>();

                String[] ac_id = resultSet.getString("ac_id").split(",");
                String[] ac_naziv = resultSet.getString("ac_naziv").split(",");
                String[] ac_opis = resultSet.getString("ac_opis").split(",");

                for (int i = 0; i < ac_id.length; i++) {
                    ac_ids.add(Integer.parseInt(ac_id[i]));
                    activities.add(new Activity(Integer.parseInt(ac_id[i]), ac_naziv[i], ac_opis[i]));
                }
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("naslov"), resultSet.getString("tekst"), resultSet.getString("vreme"), resultSet.getInt("posete"), resultSet.getInt("autor_id"), resultSet.getInt("destinacija_id"), ac_ids);
                article.setActivitiesList(activities);
                articles.add(article);
                article.setAuthorName(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return articles;
    }

    @Override
    public int getArticlesCountByActivity(Integer activity_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int count = 0;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM articles a LEFT JOIN article_activities aa ON (a.id = aa.article_id) LEFT JOIN activities ac ON(ac.id = aa.activity_id) WHERE ac.id = ?");
            preparedStatement.setInt(1, activity_id);

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

    @Override
    public List<Article> getArticlesByActivity(Integer activity_id, Integer page) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Article> articles = new ArrayList<>();

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT a.*, GROUP_CONCAT(ac.id) ac_id, GROUP_CONCAT(ac.naziv) ac_naziv, GROUP_CONCAT(ac.opis) ac_opis FROM articles a LEFT JOIN article_activities aa ON (a.id = aa.article_id) LEFT JOIN activities ac ON(ac.id = aa.activity_id) WHERE ac.id = ? GROUP BY a.id ORDER BY a.vreme DESC LIMIT 10 OFFSET ?");
            preparedStatement.setInt(1, activity_id);
            preparedStatement.setInt(2, (page - 1) * 10);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                List<Activity> activities = new ArrayList<>();
                List<Integer> ac_ids = new ArrayList<>();

                String[] ac_id = resultSet.getString("ac_id").split(",");
                String[] ac_naziv = resultSet.getString("ac_naziv").split(",");
                String[] ac_opis = resultSet.getString("ac_opis").split(",");

                for (int i = 0; i < ac_id.length; i++) {
                    activities.add(new Activity(Integer.parseInt(ac_id[i]), ac_naziv[i], ac_opis[i]));
                    ac_ids.add(Integer.parseInt(ac_id[i]));
                }
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("naslov"), resultSet.getString("tekst"), resultSet.getString("vreme"), resultSet.getInt("posete"), resultSet.getInt("autor_id"), resultSet.getInt("destinacija_id"), ac_ids);
                article.setActivitiesList(activities);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return articles;
    }

    private boolean articleWithNameExists(String title) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM articles WHERE naslov = ?");
            preparedStatement.setString(1, title);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return false;
    }

    @Override
    public Article addArticle(Article article) {
        if (this.articleWithNameExists(article.getTitle())) {
            return new Article(-1, "", "", "", 0, 0, 0, new ArrayList<>());
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("INSERT INTO articles (naslov, tekst, vreme, posete, autor_id, destinacija_id) VALUES (?, ?, NOW(), 0, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, article.getTitle());
            preparedStatement.setString(2, article.getText());
            preparedStatement.setInt(3, article.getAuthor_id());
            preparedStatement.setInt(4, article.getDestination_id());

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                article.setId(resultSet.getInt(1));
            }

            for (Integer activity_id : article.getActivities()) {
                preparedStatement = connection.prepareStatement("INSERT INTO article_activities (article_id, activity_id) VALUES (?, ?)");
                preparedStatement.setInt(1, article.getId());
                preparedStatement.setInt(2, activity_id);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return article;
    }

    @Override
    public void deleteArticle(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM articles WHERE id = ?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("DELETE FROM comments WHERE article_id = ?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("DELETE FROM article_activities WHERE article_id = ?");
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
    public Article updateArticle(Article article) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

                connection = this.newConnection();

                preparedStatement = connection.prepareStatement("UPDATE articles SET naslov = ?, tekst = ?, destinacija_id = ? WHERE id = ?");
                preparedStatement.setString(1, article.getTitle());
                preparedStatement.setString(2, article.getText());
                preparedStatement.setInt(3, article.getDestination_id());
                preparedStatement.setInt(4, article.getId());

                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement("DELETE FROM article_activities WHERE article_id = ?");
                preparedStatement.setInt(1, article.getId());

                preparedStatement.executeUpdate();

                for (Integer activity_id : article.getActivities()) {
                    preparedStatement = connection.prepareStatement("INSERT INTO article_activities (article_id, activity_id) VALUES (?, ?)");
                    preparedStatement.setInt(1, article.getId());
                    preparedStatement.setInt(2, activity_id);

                    preparedStatement.executeUpdate();
                }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);

        }

        return article;
    }

    @Override
    public Article incrementVisits(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("UPDATE articles SET posete = posete + 1 WHERE id = ?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);

        }

        return this.getArticleByID(id);
    }

    @Override
    public int getArticlesCountByDestination(Integer destination_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int count = 0;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM articles WHERE destinacija_id = ?");
            preparedStatement.setInt(1, destination_id);

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

    @Override
    public List<Article> getArticlesByDestinationID(Integer destination_id, Integer page) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Article> articles = new ArrayList<>();

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT a.*, GROUP_CONCAT(ac.id) ac_id, GROUP_CONCAT(ac.naziv) ac_naziv, GROUP_CONCAT(ac.opis) ac_opis FROM articles a LEFT JOIN article_activities aa ON (a.id = aa.article_id) LEFT JOIN activities ac ON(ac.id = aa.activity_id) WHERE a.destinacija_id = ? GROUP BY a.id ORDER BY a.vreme DESC LIMIT 10 OFFSET ?");
            preparedStatement.setInt(2, (page - 1) * 10);
            preparedStatement.setInt(1, destination_id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                List<Activity> activities = new ArrayList<>();
                List<Integer> ac_ids = new ArrayList<>();

                String[] ac_id = resultSet.getString("ac_id").split(",");
                String[] ac_naziv = resultSet.getString("ac_naziv").split(",");
                String[] ac_opis = resultSet.getString("ac_opis").split(",");

                for (int i = 0; i < ac_id.length; i++) {
                    ac_ids.add(Integer.parseInt(ac_id[i]));
                    activities.add(new Activity(Integer.parseInt(ac_id[i]), ac_naziv[i], ac_opis[i]));
                }
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("naslov"), resultSet.getString("tekst"), resultSet.getString("vreme"), resultSet.getInt("posete"), resultSet.getInt("autor_id"), resultSet.getInt("destinacija_id"), ac_ids);
                article.setActivitiesList(activities);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return articles;
    }

    @Override
    public List<Article> getMostVisitedArticles(Integer page) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Article> articles = new ArrayList<>();

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT a.*, GROUP_CONCAT(ac.id) ac_id, GROUP_CONCAT(ac.naziv) ac_naziv, GROUP_CONCAT(ac.opis) ac_opis FROM articles a LEFT JOIN article_activities aa ON (a.id = aa.article_id) LEFT JOIN activities ac ON(ac.id = aa.activity_id) GROUP BY a.id ORDER BY a.posete DESC LIMIT 10 OFFSET ?");
            preparedStatement.setInt(1, (page - 1) * 10);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                List<Activity> activities = new ArrayList<>();
                List<Integer> ac_ids = new ArrayList<>();

                String[] ac_id = resultSet.getString("ac_id").split(",");
                String[] ac_naziv = resultSet.getString("ac_naziv").split(",");
                String[] ac_opis = resultSet.getString("ac_opis").split(",");

                for (int i = 0; i < ac_id.length; i++) {
                    ac_ids.add(Integer.parseInt(ac_id[i]));
                    activities.add(new Activity(Integer.parseInt(ac_id[i]), ac_naziv[i], ac_opis[i]));
                }
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("naslov"), resultSet.getString("tekst"), resultSet.getString("vreme"), resultSet.getInt("posete"), resultSet.getInt("autor_id"), resultSet.getInt("destinacija_id"), ac_ids);
                article.setActivitiesList(activities);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return articles;
    }

    @Override
    public int getArticlesCount() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int count = 0;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM articles");

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

    @Override
    public List<Article> getLatestArticles(Integer page) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Article> articles = new ArrayList<>();

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT a.*, GROUP_CONCAT(ac.id) ac_id, GROUP_CONCAT(ac.naziv) ac_naziv, GROUP_CONCAT(ac.opis) ac_opis FROM articles a LEFT JOIN article_activities aa ON (a.id = aa.article_id) LEFT JOIN activities ac ON(ac.id = aa.activity_id) GROUP BY a.id ORDER BY a.vreme DESC LIMIT 10 OFFSET ?");
            preparedStatement.setInt(1, (page - 1) * 10);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                List<Activity> activities = new ArrayList<>();
                List<Integer> ac_ids = new ArrayList<>();

                String[] ac_id = resultSet.getString("ac_id").split(",");
                String[] ac_naziv = resultSet.getString("ac_naziv").split(",");
                String[] ac_opis = resultSet.getString("ac_opis").split(",");

                for (int i = 0; i < ac_id.length; i++) {
                    ac_ids.add(Integer.parseInt(ac_id[i]));
                    activities.add(new Activity(Integer.parseInt(ac_id[i]), ac_naziv[i], ac_opis[i]));
                }
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("naslov"), resultSet.getString("tekst"), resultSet.getString("vreme"), resultSet.getInt("posete"), resultSet.getInt("autor_id"), resultSet.getInt("destinacija_id"), ac_ids);
                article.setActivitiesList(activities);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return articles;
    }

    @Override
    public Article getArticleByID(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Article article = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT a.*, GROUP_CONCAT(ac.id) ac_id, GROUP_CONCAT(ac.naziv) ac_naziv, GROUP_CONCAT(ac.opis) ac_opis FROM articles a LEFT JOIN article_activities aa ON (a.id = aa.article_id) LEFT JOIN activities ac ON(ac.id = aa.activity_id) WHERE a.id = ? GROUP BY a.id");
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                List<Activity> activities = new ArrayList<>();
                List<Integer> ac_ids = new ArrayList<>();

                String[] ac_id = resultSet.getString("ac_id").split(",");
                String[] ac_naziv = resultSet.getString("ac_naziv").split(",");
                String[] ac_opis = resultSet.getString("ac_opis").split(",");

                for (int i = 0; i < ac_id.length; i++) {
                    ac_ids.add(Integer.parseInt(ac_id[i]));
                    activities.add(new Activity(Integer.parseInt(ac_id[i]), ac_naziv[i], ac_opis[i]));
                }
                article = new Article(resultSet.getInt("id"), resultSet.getString("naslov"), resultSet.getString("tekst"), resultSet.getString("vreme"), resultSet.getInt("posete"), resultSet.getInt("autor_id"), resultSet.getInt("destinacija_id"), ac_ids);
                article.setActivitiesList(activities);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return article;
    }
}
