package com.nemanja02.projekat.repositories.article;

import com.nemanja02.projekat.entities.Article;

import java.util.List;

public interface ArticleRepository {
    public Article addArticle(Article article);
    public void deleteArticle(Integer id);
    public Article updateArticle(Article article);
    public Article incrementVisits(Integer id);
    public List<Article> getArticlesByDestinationID(Integer destination_id, Integer page);
    public List<Article> getMostVisitedArticles(Integer page);
    public List<Article> getLatestArticles(Integer page);
    public Article getArticleByID(Integer id);
    public List<Article> getArticles(Integer page);
    public List<Article> getArticlesByActivity(Integer activity, Integer page);

    public int getArticlesCount();

    public int getArticlesCountByDestination(Integer destination_id);

    public int getArticlesCountByActivity(Integer activity_id);
}
