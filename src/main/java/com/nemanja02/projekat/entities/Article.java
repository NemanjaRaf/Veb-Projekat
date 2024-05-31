package com.nemanja02.projekat.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class Article {
    private Integer id;

    // naslov, tekst, vreme, posete, autor_id, destinacija_id

    @NotEmpty(message = "Title is required")
    @NotNull(message = "Title is required")
    private String title;

    @NotEmpty(message = "Text is required")
    @NotNull(message = "Text is required")
    private String text;

    private String time;

    private Integer visits;

    @NotNull(message = "Author id is required")
    private Integer author_id;

    @NotNull(message = "Destination id is required")
    private Integer destination_id;

    @NotNull
    @NotEmpty
    List<Integer> activities;

    private List<Activity> activitiesList;
    private String authorName;

    public Article() {
    }

    public Article(String title, String text, Integer author_id, Integer destination_id, List<Integer> activities) {
        this.title = title;
        this.text = text;
        this.author_id = author_id;
        this.destination_id = destination_id;
        this.activities = activities;
    }

    public Article(Integer id, String title, String text, String time, Integer visits, Integer author_id, Integer destination_id, List<Integer> activities) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.time = time;
        this.visits = visits;
        this.author_id = author_id;
        this.destination_id = destination_id;
        this.activities = activities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public Integer getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(Integer destination_id) {
        this.destination_id = destination_id;
    }

    public List<Integer> getActivities() {
        return activities;
    }

    public void setActivities(List<Integer> activities) {
        this.activities = activities;
    }

    public List<Activity> getActivitiesList() {
        return activitiesList;
    }

    public void setActivitiesList(List<Activity> activitiesList) {
        this.activitiesList = activitiesList;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
