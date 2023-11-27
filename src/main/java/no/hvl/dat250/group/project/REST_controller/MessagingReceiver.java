package no.hvl.dat250.group.project.REST_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import no.hvl.dat250.group.project.Answer;
import no.hvl.dat250.group.project.PollMongo;
import no.hvl.dat250.group.project.REST_controller.PollRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

public class MessagingReceiver {
    private static final String EXCHANGE_NAME = "polls";



    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/analytics");
        MongoDatabase database = mongoClient.getDatabase("analytics");
        MongoCollection<Document> polls = database.getCollection("polls");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");

            Answer answer = new ObjectMapper().readValue(message, Answer.class);
            String pollId = String.valueOf(answer.getPoll().getId());
            String key = answer.getColor() == 1 ? "numberOfGreenAnswers" : "numberOfRedAnswers";

            Bson filter = eq("_id", pollId);
            Bson update = combine(
                    setOnInsert(answer.getColor() == 1 ? "numberOfRedAnswers" : "numberOfGreenAnswers", 0),
                    inc(key, 1)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            polls.updateOne(filter, update, options);
        };
        String s = channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}

