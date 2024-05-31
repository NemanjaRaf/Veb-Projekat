package com.nemanja02.projekat.resources;

import com.nemanja02.projekat.entities.User;
import com.nemanja02.projekat.filters.AuthRequired;
import com.nemanja02.projekat.requests.LoginRequest;
import com.nemanja02.projekat.services.UserService;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/users")
public class UserResource {

    @Inject
    private UserService userService;

    @POST
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(@Valid LoginRequest loginRequest) {
        Map<String, String> response = new HashMap<>();

        String jwt = this.userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (jwt == null) {
            response.put("message", "These credentials do not match our records");
            return Response.status(422, "Unprocessable Entity").entity(response).build();
        }

        if (jwt == "neaktivan") {
            response.put("message", "Your account is deactivated");
            return Response.status(422, "Unprocessable Entity").entity(response).build();
        }

        response.put("jwt", jwt);

        return Response.ok(response).build();
    }

    @GET
    @Path("/users/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(@PathParam("page") Integer page) {

        List<User> users = this.userService.getUsers(page);
        int count = this.userService.getUsersCount();

        HashMap<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("count", count);

        return Response.ok(response).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid User user) {
        return Response.ok(this.userService.addUser(user)).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired(roles = {"admin"})
    public Response edit(@PathParam("id") Integer id, @Valid User user) {
        user.setId(id);
        return Response.ok(this.userService.editUser(user)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Integer id) {
        return Response.ok(this.userService.getUser(id)).build();
    }

    @POST
    @Path("/activate/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired(roles = {"admin"})
    public Response activate(@PathParam("id") Integer id) {
        return Response.ok(this.userService.activateUser(id)).build();
    }

    @POST
    @Path("/deactivate/{id}")
    @AuthRequired(roles = {"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivate(@PathParam("id") Integer id) {
        return Response.ok(this.userService.deactivateUser(id)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired(roles = {"admin"})
    public void delete(@PathParam("id") Integer id) {
        this.userService.deleteUser(id);
    }
}
