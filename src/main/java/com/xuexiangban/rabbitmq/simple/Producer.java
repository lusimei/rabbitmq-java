package com.xuexiangban.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class Producer {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("/");

        Connection connection = null;
        Channel channel = null;
        try {
            connection = factory.newConnection("生成者");
            channel = connection.createChannel();
            String queueName = "queue1";
            channel.queueDeclare(queueName,true,false,false,null);
            String massage = "Hello xuaxiangban";
            channel.basicPublish("",queueName,null,massage.getBytes());
            System.out.println("消息发送成功！！！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(channel != null && channel.isOpen()){
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(connection != null && connection.isOpen()){
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
