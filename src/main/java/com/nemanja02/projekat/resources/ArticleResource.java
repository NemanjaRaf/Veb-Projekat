package com.nemanja02.projekat.resources;

import com.nemanja02.projekat.entities.Article;
import com.nemanja02.projekat.filters.AuthRequired;
import com.nemanja02.projekat.services.ArticleService;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

@Path("/articles")
public class ArticleResource {

    @Inject
    private ArticleService articleService;

    @GET
    @Path("/destination/{destinationId}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(@PathParam("destinationId") Integer destinationId, @PathParam("page") Integer page) {
        List<Article> articles = this.articleService.getArticlesByDestinationID(destinationId, page);
        int count = this.articleService.getArticlesCountByDestination(destinationId);
        HashMap<String, Object> response = new HashMap<>();
        response.put("articles", articles);
        response.put("count", count);

        return Response.ok(response).build();
    }

    @GET
    @Path("/latest/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(@PathParam("page") Integer page) {
        List<Article> articles = this.articleService.getLatestArticles(page);
        int count = this.articleService.getArticlesCount();
        HashMap<String, Object> response = new HashMap<>();
        response.put("articles", articles);
        response.put("count", count);

        return Response.ok(response).build();
    }

    @GET
    @Path("/most-visited/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response mostVisited(@PathParam("page") Integer page) {
        List<Article> articles = this.articleService.getMostVisitedArticles(page);
        int count = this.articleService.getArticlesCount();
        HashMap<String, Object> response = new HashMap<>();
        response.put("articles", articles);
        response.put("count", count);

        return Response.ok(response).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired(roles = {"admin", "editor"})
    public Response create(@Valid Article article) {
        return Response.ok(this.articleService.addArticle(article)).build();
    }

    @GET
    @Path("/activity/{id}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByActivity(@PathParam("id") Integer id, @PathParam("page") Integer page){
        System.out.println("getByActivity");
        try {
            List<Article> articles = this.articleService.getArticlesByActivity(id, page);
            int count = this.articleService.getArticlesCountByActivity(id);
            HashMap<String, Object> response = new HashMap<>();
            response.put("articles", articles);
            response.put("count", count);
            return Response.ok(response).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired(roles = {"admin", "editor"})
    public Response edit(@Valid Article article){
        return Response.ok(this.articleService.updateArticle(article)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Integer id){
        return Response.ok(this.articleService.getArticleByID(id)).build();
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired(roles = {"admin", "editor"})
    public Response delete(@PathParam("id") Integer id){
        this.articleService.deleteArticle(id);
        return Response.ok().build();
    }

    @POST
    @Path("/increment-visits/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response incrementVisits(@PathParam("id") Integer id){
        return Response.ok(this.articleService.incrementVisits(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all/{page}")
    public Response getall(@PathParam("page") Integer page) {

        List<Article> articles = this.articleService.getArticles(page);
        int count = this.articleService.getArticlesCount();
        HashMap<String, Object> response = new HashMap<>();
        response.put("articles", articles);
        response.put("count", count);

        return Response.ok(response).build();
    }
}
