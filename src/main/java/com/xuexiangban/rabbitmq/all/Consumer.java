package com.xuexiangban.rabbitmq.all;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {

    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            factory.setPort(5672);
            factory.setUsername("admin");
            factory.setPassword("admin");
            factory.setVirtualHost("/");

            final String queueName = Thread.currentThread().getName();
            Connection connection = null;
            Channel channel = null;
            try {
                connection = factory.newConnection("生成者");
                channel = connection.createChannel();
                String exchangeName = "direct_message_exchange";
                channel.queueDeclare("queue4",true,false,false,null);
                channel.queueDeclare("queue5",true,false,false,null);
                channel.queueDeclare("queue6",true,false,false,null);

                channel.queueBind("queue4",exchangeName,"order");
                channel.queueBind("queue5",exchangeName,"order");
                channel.queueBind("queue6",exchangeName,"course");
                channel.basicConsume(queueName, true, new DeliverCallback() {
                    @Override
                    public void handle(String consumerTag, Delivery delivery) throws IOException {
                        System.out.println(queueName + "收到消息是" + new String(delivery.getBody(), "UTF-8"));
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
    };

    public static void main(String[] args) {
        new Thread(runnable,"queue4").start();
        new Thread(runnable,"queue5").start();
        new Thread(runnable,"queue6").start();
    }
}
