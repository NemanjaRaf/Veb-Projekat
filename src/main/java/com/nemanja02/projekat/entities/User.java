package com.nemanja02.projekat.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class User {
    private Integer id;

    // email, ime, prezime, tip, status, password

    @NotEmpty(message = "Email is required")
    @NotNull(message = "Email is required")
    private String email;

    @NotEmpty(message = "First name is required")
    @NotNull(message = "First name is required")
    private String name;

    @NotEmpty(message = "Last name is required")
    @NotNull(message = "Last name is required")
    private String surname;

    @NotEmpty(message = "Type is required")
    @NotNull(message = "Type is required")
    private String type;

    @NotEmpty(message = "Status is required")
    @NotNull(message = "Status is required")
    private String status;

    @NotEmpty(message = "Password is required")
    @NotNull(message = "Password is required")
    private String password;

    public User() {
    }

    public User(String email, String name, String surname, String type, String status, String password) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.type = type;
        this.status = status;
        this.password = password;
    }

    public User(Integer id, String email, String name, String surname, String type, String status, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.type = type;
        this.status = status;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
