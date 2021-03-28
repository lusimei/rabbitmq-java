package com.xuexiangban.rabbitmq.work.lunxun;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.18");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("/");

        Connection connection = null;
        Channel channel = null;
        try {
            connection = factory.newConnection("生成者");
            channel = connection.createChannel();
            for (int i = 0; i < 20; i++) {
                String msg = "学相伴"+i;
                channel.basicPublish("","queue1",null,msg.getBytes());
            }
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
