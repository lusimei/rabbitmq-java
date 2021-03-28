package com.xuexiangban.rabbitmq.simple;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {

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
            channel.basicConsume("queue1", true, new DeliverCallback() {
                @Override
                public void handle(String consumerTag, Delivery delivery) throws IOException {
                    System.out.println("收到消息是" + new String(delivery.getBody(), "UTF-8"));
                }
            }, new CancelCallback() {
                @Override
                public void handle(String s) throws IOException {
                    System.out.println("接受失败了。。。");
                }
            });
            System.out.println("开始接受消息");
            System.in.read();
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
