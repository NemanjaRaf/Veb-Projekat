package com.nemanja02.projekat.resources;

import com.nemanja02.projekat.entities.Destination;
import com.nemanja02.projekat.filters.AuthRequired;
import com.nemanja02.projekat.services.DestinationService;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

@Path("/destinations")
public class DestinationResource {

    @Inject
    private DestinationService destinationService;

    @GET
    @Path("/all/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(@PathParam("page") Integer page) {
        List<Destination> destinations = this.destinationService.getDestinations(page);
        int count = this.destinationService.getDestinationsCount();

        HashMap<String, Object> response = new HashMap<>();
        response.put("destinations", destinations);
        response.put("count", count);

        return Response.ok(response).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired(roles = {"admin", "editor"})
    public Response create(@Valid Destination destination) {
        return Response.ok(this.destinationService.addDestination(destination)).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired(roles = {"admin", "editor"})
    public Response edit(@Valid Destination destination, @PathParam("id") Integer id){
        return Response.ok(this.destinationService.editDestination(destination)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Integer id){
        return Response.ok(this.destinationService.getDestination(id)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired(roles = {"admin", "editor"})
    public Response delete(@PathParam("id") Integer id){
        boolean response = this.destinationService.deleteDestination(id);
        if (response) {
            return Response.ok().build();
        } else {
            // error: destination has active articles
            return Response.status(422, "Unprocessable Entity").build();
        }
    }
}
