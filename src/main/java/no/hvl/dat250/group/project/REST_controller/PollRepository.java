package no.hvl.dat250.group.project.REST_controller;

import no.hvl.dat250.group.project.Answer;
import no.hvl.dat250.group.project.PollMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PollRepository extends MongoRepository<PollMongo, String> {
}
