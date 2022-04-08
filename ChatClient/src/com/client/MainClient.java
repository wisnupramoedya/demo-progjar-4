package com.client;

import com.ww.Message;
import com.ww.User;
import com.ww.Utils;

import java.io.*;
import java.net.Socket;

public class MainClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(Utils.HOST, Utils.PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            CommandReaderClient.commandStarter();
            CommandReaderClient commandReaderClient = new CommandReaderClient();

            System.out.print("Login as: ");
            String name = bufferedReader.readLine();
            User user = commandReaderClient.createUsername(name);
            System.out.println("Username named " + user.getFullName() + " has been created.");

            System.out.println("Start the connection.");
            ThreadClient threadClient = new ThreadClient(new ObjectInputStream(socket.getInputStream()));
            threadClient.start();
            objectOutputStream.writeObject(user);
            objectOutputStream.flush();

            while (true) {
                try {
                    String text = bufferedReader.readLine();
                    Message message = commandReaderClient.createMessage(text, user);

                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();
                } catch (Exception e) {
                    System.err.println("Error: Client could not send messages to the server. " + e.getMessage());
                    break;
                }
            }

            System.out.println("Connection stop.");
        } catch (IOException e) {
            System.err.println("Error: Client program is shutdown. " + e.getMessage());
        }
    }
}
