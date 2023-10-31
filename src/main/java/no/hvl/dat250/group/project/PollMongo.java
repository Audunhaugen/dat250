package no.hvl.dat250.group.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Getter
@Document("polls")
public class PollMongo {
    @Id
    @Setter
    private String id;

    public PollMongo(String id, int numberOfGreenAnswers, int numberOfRedAnswers) {
        this.id = id;
        this.numberOfGreenAnswers = numberOfGreenAnswers;
        this.numberOfRedAnswers = numberOfRedAnswers;
    }

    @Setter
    private int numberOfGreenAnswers;

    @Setter
    private int numberOfRedAnswers;


}
