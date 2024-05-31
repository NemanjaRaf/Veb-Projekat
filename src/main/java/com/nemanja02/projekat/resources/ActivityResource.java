package com.nemanja02.projekat.resources;

import com.nemanja02.projekat.entities.Activity;
import com.nemanja02.projekat.filters.AuthRequired;
import com.nemanja02.projekat.services.ActivityService;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/activities")
public class ActivityResource {

    @Inject
    private ActivityService activityService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired(roles = {"admin", "editor"})
    public Response create(@Valid Activity activity) {
        return Response.ok(this.activityService.addActivity(activity)).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        return Response.ok(this.activityService.getActivities()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired(roles = {"admin", "editor"})
    public Response delete(@PathParam("id") Integer id) {
        this.activityService.deleteActivity(id);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Integer id) {
        Activity activity = this.activityService.getActivityByID(id);
        System.out.println(activity.getId());
        return Response.ok(activity).build();
    }
}
