package com.xuexiangban.rabbitmq.work.fair;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Work2 {

    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.1.18");
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
                //定义接收消息的回调
                Channel finalChannel = channel;
                finalChannel.basicQos(1);//指定限制消费者的消费数量
                channel.basicConsume(queueName, false, new DeliverCallback() {
                    @Override
                    public void handle(String consumerTag, Delivery delivery) throws IOException {
                        try {
                            System.out.println("Work2- 收到消息是" + new String(delivery.getBody(), "UTF-8"));
                            Thread.sleep(300);
                            finalChannel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }                    }
                }, new CancelCallback() {
                    @Override
                    public void handle(String s) throws IOException {
                        System.out.println(s);
                    }
                });
                System.out.println("Work2开始接受消息");
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
        new Thread(runnable,"queue1").start();
//        new Thread(runnable,"queue2").start();
//        new Thread(runnable,"queue3").start();
    }
}
