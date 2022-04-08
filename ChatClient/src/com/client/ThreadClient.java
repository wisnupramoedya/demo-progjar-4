package com.client;

import com.ww.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ThreadClient extends Thread {
    private ObjectInputStream objectInputStream;

    public ThreadClient(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = (Message) this.objectInputStream.readObject();
                System.out.println(message.outputBroadcastMessage());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
