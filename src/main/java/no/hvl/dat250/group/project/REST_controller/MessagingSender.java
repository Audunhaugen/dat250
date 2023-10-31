package no.hvl.dat250.group.project.REST_controller;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import no.hvl.dat250.group.project.Answer;

public class MessagingSender {

    private static final String EXCHANGE_NAME = "polls";

    public void sendMessage(Answer answer) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            String message = "pollId:"+answer.getPoll().getId()+",vote:"+String.valueOf(answer.getColor());

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
