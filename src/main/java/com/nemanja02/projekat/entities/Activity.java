package com.nemanja02.projekat.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Activity {

    private Integer id;

    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name is required")
    private String name;

    @NotNull(message = "Description is required")
    @NotEmpty(message = "Description is required")
    private String description;

    public Activity() {
    }

    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Activity(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
