package com.nemanja02.projekat.resources;

import com.nemanja02.projekat.entities.Comment;
import com.nemanja02.projekat.services.CommentService;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/comments")
public class CommentResource {

    @Inject
    private CommentService commentService;


    @GET
    @Path("/{articleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(@PathParam("articleId") Integer articleId) {
        return Response.ok(this.commentService.getCommentsByArticleID(articleId)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid Comment comment) {
        return Response.ok(this.commentService.addComment(comment)).build();
    }
}
