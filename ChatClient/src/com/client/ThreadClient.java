package com.client;

import com.ww.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ThreadClient extends Thread {
    private final ObjectInputStream objectInputStream;

    public ThreadClient(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = (Message) this.objectInputStream.readObject();
                System.out.println(message.outputMessage());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error: Client could not receive messages from the server. " + e.getMessage());
                break;
            }
        }
    }
}
