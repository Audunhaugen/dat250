package no.hvl.dat250.group.project.REST_controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import static org.hibernate.cfg.AvailableSettings.PERSISTENCE_UNIT_NAME;

@SpringBootApplication
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
