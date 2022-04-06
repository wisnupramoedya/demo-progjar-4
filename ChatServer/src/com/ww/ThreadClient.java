package com.ww;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadClient extends Thread {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private ThreadServer threadServer;

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
        while (true) {
            try {
                Message message = (Message) this.objectInputStream.readObject();
                this.threadServer.sendToAll(message);
            } catch (IOException e) {
                this.threadServer.getClientList().remove(this.getClientId());
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public String getClientId() {
        return this.socket.getInetAddress().getHostAddress() + ":" + this.socket.getPort();
    }
}
