package com.nemanja02.projekat.repositories.user;

import com.nemanja02.projekat.entities.User;

import java.util.List;

public interface UserRepository {
    public User addUser(User user);
    public User editUser(User user);
    public void deleteUser(Integer id);
    public List<User> getUsers(Integer page);

    public User activateUser(Integer id);
    public User deactivateUser(Integer id);
    public User getUser(Integer id);
    public User getUserByUsername(String username);
    public int getUsersCount();
}
