package com.nemanja02.projekat.filters;

import com.nemanja02.projekat.services.UserService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

@Provider
@AuthRequired
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    UserService userService;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("AuthFilter " + requestContext.getUriInfo().getPath());

        // Retrieve the roles from the annotation
        Method resourceMethod = resourceInfo.getResourceMethod();
        AuthRequired authRequired = resourceMethod.getAnnotation(AuthRequired.class);
        if (authRequired == null) {
            authRequired = resourceInfo.getResourceClass().getAnnotation(AuthRequired.class);
        }

        String[] roles = authRequired.roles();

        String token = requestContext.getHeaderString("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        System.out.println("Token: " + token);
        if (!this.userService.isAuthorized(token, new ArrayList<>())) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        if (roles.length > 0 && !this.userService.hasRole(token, roles)) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        System.out.println("User is authorized and has the required roles");
    }
}
