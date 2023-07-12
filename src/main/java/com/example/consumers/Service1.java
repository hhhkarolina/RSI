package com.example.consumers;

import com.rabbitmq.client.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Service1 {
    static String EXCHANGE_NAME = "MovieQueue";
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Scanner scanner = new Scanner(System.in);

        try {

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            String queueName = channel.queueDeclare().getQueue();

            channel.queueBind(queueName, EXCHANGE_NAME, "standard");

            DefaultConsumer consumer = new DefaultConsumer(channel){
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String msg = new String(body, "UTF-8");
                    Date currentDate = new Date(System.currentTimeMillis());
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/logs/standardLog.txt", true))) {
                        writer.write("Log " + currentDate + " : - " + msg);
                        writer.newLine();

                        System.out.println("pomyslnie zapisano do pliku.");
                    } catch (IOException e) {
                        System.out.println("wystapil blad przy zapisie do pliku.");
                        e.printStackTrace();
                    }
                    System.out.println("Log " + currentDate + " : - " + msg);
                    System.out.println(envelope.getRoutingKey());
                }

            };
            channel.basicConsume(queueName, true, consumer);

            scanner.next();
            channel.close();
            connection.close();

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }
}
