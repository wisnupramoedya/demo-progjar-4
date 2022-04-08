package com.client;

import com.ww.Message;
import com.ww.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ThreadClient extends Thread {
    private final ObjectInputStream objectInputStream;
    private final Socket socket;

    public ThreadClient(Socket socket, ObjectInputStream objectInputStream) {
        this.socket = socket;
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!this.socket.isConnected()) {
                    System.err.println("Error: Socket has been disconnected");
                    System.out.println("Type ENTER to exit.");
                    break;
                }
                Message message = (Message) this.objectInputStream.readObject();
                System.out.println(message.outputMessage());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error: Client could not receive messages from the server. " + e.getMessage());
                System.out.println("Type ENTER to exit.");
                break;
            }
        }
    }
}
