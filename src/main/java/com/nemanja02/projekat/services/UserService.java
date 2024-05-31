package com.nemanja02.projekat.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.nemanja02.projekat.entities.User;
import com.nemanja02.projekat.repositories.user.UserRepository;
import javax.inject.Inject;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.List;

public class UserService {
    public UserService() {
        System.out.println(this);
    }

    @Inject
    private UserRepository userRepository;

    public User addUser(User user) {
        return this.userRepository.addUser(user);
    }
    public User editUser(User user) {
        return this.userRepository.editUser(user);
    }
    public void deleteUser(Integer id) {
        this.userRepository.deleteUser(id);
    }
    public List<User> getUsers(Integer page) {
        return this.userRepository.getUsers(page);
    }

    public User activateUser(Integer id) {
        return this.userRepository.activateUser(id);
    }
    public User deactivateUser(Integer id) {
        return this.userRepository.deactivateUser(id);
    }
    public User getUser(Integer id) {
        return this.userRepository.getUser(id);
    }

    public String login(String username, String password) {
        User user = this.userRepository.getUserByUsername(username);

        String hashedPassword = DigestUtils.sha256Hex(password);

        if (user == null || !user.getPassword().equals(hashedPassword)) {
            return null;
        }

        if (user.getStatus().equals("neaktivan")) {
            return "neaktivan";
        }


        Date issuedAt = new Date();

        Algorithm algorithm = Algorithm.HMAC256("secret");

        return JWT.create()
                .withIssuedAt(issuedAt)
                .withSubject(username)
                .withClaim("id", user.getId())
                .withClaim("role", user.getType())
                .sign(algorithm);
    }


    public boolean isAuthorized(String token, List<String> validRoles){
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        String username = jwt.getSubject();

        User user = this.userRepository.getUserByUsername(username);

        if (user == null){
            return false;
        }

        if (!validRoles.isEmpty() && !validRoles.contains(user.getType())) {
            return false;
        }

        return true;
    }

    public boolean hasRole(String token, String[] roles) {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        String username = jwt.getSubject();

        User user = this.userRepository.getUserByUsername(username);

        if (user == null) {
            return false;
        }

        for (String role : roles) {
            if (role.equals(user.getType())) {
                return true;
            }
        }

        return false;
    }

    public int getUsersCount() {
        return this.userRepository.getUsersCount();
    }
}
