package com.client;

import com.ww.Message;
import com.ww.User;

public class CommandReaderClient {
    public static void commandStarter() {
        String starterMessage = """
                ============
                Chat Progjar
                ============
                To use this app, you need to type command.
                1. lists => to show all online user
                2. * < your_message => to send broadcast message
                3. online_user < your_message => to send message for specific user
                ============""";
        System.out.println(starterMessage);
    }

    public User createUsername(String fullName) {
        User user = new User();
        user.setFullName(fullName);
        return user;
    }

    public Message createMessage(String command, User user) {
        Message message = new Message();
        message.setUserSender(user);
        message.setText(command);
        message.setCommand(true);
        return message;
    }
}
