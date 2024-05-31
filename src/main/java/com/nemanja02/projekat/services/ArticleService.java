package com.nemanja02.projekat.services;

import com.nemanja02.projekat.entities.Article;
import com.nemanja02.projekat.repositories.article.ArticleRepository;
import javax.inject.Inject;

import java.util.List;

public class ArticleService {
    public ArticleService() {
        System.out.println(this);
    }

    @Inject
    private ArticleRepository articleRepository;

    public Article addArticle(Article article) {
        return this.articleRepository.addArticle(article);
    }
    public void deleteArticle(Integer id) {
        this.articleRepository.deleteArticle(id);
    }
    public Article updateArticle(Article article) {
        return this.articleRepository.updateArticle(article);
    }
    public Article incrementVisits(Integer id) {
        return this.articleRepository.incrementVisits(id);
    }
    public List<Article> getArticlesByDestinationID(Integer destination_id, Integer page) {
        return this.articleRepository.getArticlesByDestinationID(destination_id, page);
    }
    public List<Article> getMostVisitedArticles(Integer page) {
        return this.articleRepository.getMostVisitedArticles(page);
    }
    public List<Article> getLatestArticles(Integer page) {
        return this.articleRepository.getLatestArticles(page);
    }
    public Article getArticleByID(Integer id) {
        return this.articleRepository.getArticleByID(id);
    }

    public List<Article> getArticles(Integer page) {
        return this.articleRepository.getArticles(page);
    }
    public List<Article> getArticlesByActivity(Integer activity_id, Integer page) {
        return this.articleRepository.getArticlesByActivity(activity_id, page);
    }

    public int getArticlesCount() {
        return this.articleRepository.getArticlesCount();
    }

    public int getArticlesCountByDestination(Integer destination_id) {
        return this.articleRepository.getArticlesCountByDestination(destination_id);
    }

    public int getArticlesCountByActivity(Integer activity_id) {
        return this.articleRepository.getArticlesCountByActivity(activity_id);
    }
}
