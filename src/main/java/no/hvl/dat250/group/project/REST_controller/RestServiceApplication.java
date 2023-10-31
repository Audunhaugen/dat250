package no.hvl.dat250.group.project.REST_controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories
public class RestServiceApplication {
    private static EntityManager em;
    static final String PERSISTENCE_UNIT_NAME = "group-project";
    public static void main(String[] args) {
        createEntityManager();
        SpringApplication.run(RestServiceApplication.class, args);
    }

    private static void createEntityManager(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
    }
    public static EntityManager getEntityManager(){
        return em;
    }
}
