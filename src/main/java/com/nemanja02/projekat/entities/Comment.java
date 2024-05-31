package com.nemanja02.projekat.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Comment {

    private Integer id;

    // autor_id, tekst, vreme, article_id

    @NotNull(message = "Author is required")
    @NotEmpty(message = "Author is required")
    private String author;

    @NotNull(message = "Text is required")
    @NotEmpty(message = "Text is required")
    private String text;

    private String time;

    @NotNull(message = "Article id is required")
    private Integer article_id;

    private String author_name;

    public Comment() {
    }

    public Comment(String author, String text, Integer article_id) {
        this.author = author;
        this.text = text;
        this.article_id = article_id;
    }

public Comment(Integer id, String author, String text, String time, Integer article_id) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.time = time;
        this.article_id = article_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getArticle_id() {
        return article_id;
    }

    public void setArticle_id(Integer article_id) {
        this.article_id = article_id;
    }
}
