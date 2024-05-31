package com.nemanja02.projekat;

import com.nemanja02.projekat.filters.CORSFilter;
import com.nemanja02.projekat.filters.ConstraintViolationExceptionMapper;
import com.nemanja02.projekat.repositories.activity.ActivityRepository;
import com.nemanja02.projekat.repositories.activity.MysqlActivityRepository;
import com.nemanja02.projekat.repositories.article.ArticleRepository;
import com.nemanja02.projekat.repositories.article.MysqlArticleRepository;
import com.nemanja02.projekat.repositories.comment.CommentRepository;
import com.nemanja02.projekat.repositories.comment.MysqlCommentRepository;
import com.nemanja02.projekat.repositories.destination.DestinationRepository;
import com.nemanja02.projekat.repositories.destination.MysqlDestinationRepository;
import com.nemanja02.projekat.repositories.user.MysqlUserRepository;
import com.nemanja02.projekat.repositories.user.UserRepository;
import com.nemanja02.projekat.resources.UserResource;
import com.nemanja02.projekat.services.*;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class HelloApplication extends ResourceConfig {
    public HelloApplication() {
        System.out.println("HelloApplication");
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                System.out.println("AbstractBinder");
                this.bind(MysqlActivityRepository.class).to(ActivityRepository.class).in(Singleton.class);
                this.bind(MysqlUserRepository.class).to(UserRepository.class).in(Singleton.class);
                this.bind(MysqlArticleRepository.class).to(ArticleRepository.class).in(Singleton.class);
                this.bind(MysqlCommentRepository.class).to(CommentRepository.class).in(Singleton.class);
                this.bind(MysqlDestinationRepository.class).to(DestinationRepository.class).in(Singleton.class);

                this.bindAsContract(ActivityService.class);
                this.bindAsContract(UserService.class);
                this.bindAsContract(ArticleService.class);
                this.bindAsContract(CommentService.class);
                this.bindAsContract(DestinationService.class);
            }
        };
        register(binder);
        System.out.println("register(binder)");

        packages("com.nemanja02.projekat");

        register(CORSFilter.class);
        register(ConstraintViolationExceptionMapper.class);
    }
}
