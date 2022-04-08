package com.server;

import com.ww.Message;
import com.ww.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadClient extends Thread {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private ThreadServer threadServer;
    private User user;

    public ThreadClient(Socket socket, ThreadServer threadServer) throws IOException {
        this.socket = socket;
        this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        this.threadServer = threadServer;
    }

    public void sendMessage(Message message) throws IOException {
        this.objectOutputStream.writeObject(message);
        this.objectOutputStream.flush();
    }

    @Override
    public void run() {
        try {
            User user = (User) this.objectInputStream.readObject();
            this.user = user;
            System.out.println("User " + user.getFullName() + " with id " + this.getClientId() + " is connected.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: Client's username is not defined. " + e.getMessage());
            return;
        }

        while (true) {
            try {
                Message message = (Message) this.objectInputStream.readObject();
                this.threadServer.sendToAll(message);
            } catch (IOException e) {
                this.threadServer.getClients().remove(this.getClientId());
                System.err.println("Error: Client with id " + this.getClientId() + " could not be accessed. " + e.getMessage());
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Error: Wrong sent message. " + e.getMessage());
            }
        }
    }

    public String getClientId() {
        return this.socket.getInetAddress().getHostAddress() + ":" + this.socket.getPort();
    }
}
