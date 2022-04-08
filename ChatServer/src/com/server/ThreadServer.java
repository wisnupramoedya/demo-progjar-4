package com.server;

import com.ww.Message;
import com.ww.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class ThreadServer extends Thread {
    private Hashtable<String, ThreadClient> clientList;
    private final ServerSocket serverSocket;

    public ThreadServer() throws IOException {
        this.clientList = new Hashtable<>();
        this.serverSocket = new ServerSocket(Utils.PORT);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = this.serverSocket.accept();
                ThreadClient threadClient = new ThreadClient(socket, this);
                threadClient.start();

                String clientId = threadClient.getClientId();
                this.clientList.put(clientId, threadClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToAll(Message message) throws IOException {
        Enumeration<String> enumeration = this.clientList.keys();

        while (enumeration.hasMoreElements()) {
            String clientId = enumeration.nextElement();

            ThreadClient threadClient = this.clientList.get(clientId);
            threadClient.sendMessage(message);
        }
    }

    public Hashtable<String, ThreadClient> getClientList() {
        return clientList;
    }
}
