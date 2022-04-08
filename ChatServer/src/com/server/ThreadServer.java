package com.server;

import com.ww.Message;
import com.ww.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class ThreadServer extends Thread {
    private Hashtable<String, ThreadClient> clients;
    private final ServerSocket serverSocket;

    public ThreadServer() throws IOException {
        this.clients = new Hashtable<>();
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
                this.clients.put(clientId, threadClient);
            } catch (IOException e) {
                System.err.println("Error: Socket connection has been shutdown. " + e.getMessage());
                break;
            }
        }
    }

    public String getAllOnlineUsers() {
        StringBuilder text = new StringBuilder("Online users:");
        Enumeration<String> enumeration = this.clients.keys();
        while (enumeration.hasMoreElements()) {
            text.append("\n");
            String clientId = enumeration.nextElement();
            if (this.clients.get(clientId).getUser() == null) {
                continue;
            }
            text.append("-> ").append(this.clients.get(clientId).getUser().getFullName());
        }

        return text.toString();
    }

    public void sendToAll(Message message) throws IOException {
        String text = message.getText();
        text = text.substring(text.indexOf("* < ") + "* < ".length());

        message.setCommand(false);
        message.setBroadcast(true);
        message.setText(text);

        Enumeration<String> enumeration = this.clients.keys();
        while (enumeration.hasMoreElements()) {
            String clientId = enumeration.nextElement();
            ThreadClient threadClient = this.clients.get(clientId);
            threadClient.sendMessage(message);
        }
    }

    public boolean sendToSpecificUser(Message message) throws IOException {
        String text = message.getText();
        int endOfFullName = text.indexOf(" < ");
        String receiverFullName = text.substring(0, endOfFullName);
        String newText = text.substring(endOfFullName + " < ".length());

        message.setCommand(false);
        message.setBroadcast(false);
        message.setText(newText);

        boolean isUserFound = false;
        Enumeration<String> enumeration = this.clients.keys();
        while (enumeration.hasMoreElements() && !isUserFound) {
            String clientId = enumeration.nextElement();
            ThreadClient threadClient = this.clients.get(clientId);
            isUserFound = threadClient.getUser() != null && threadClient.getUser().getFullName().equals(receiverFullName);
            if (isUserFound) {
                message.setUserReceiver(threadClient.getUser());
                threadClient.sendMessage(message);
            }
        }
        return isUserFound;
    }

    public Message sendBackMessageToSender(String text) {
        Message message = new Message();
        message.setCommand(true);
        message.setText(text);
        return message;
    }

    public void readCommand(Message message, ThreadClient threadClient) throws IOException {
        if (!message.isCommand()) {
            return;
        }

        if (message.outputMessage().equals("lists")) {
            threadClient.sendMessage(this.sendBackMessageToSender(this.getAllOnlineUsers()));
        } else if (message.outputMessage().startsWith("* < ")) {
            this.sendToAll(message);
        } else if (message.outputMessage().contains(" < ")) {
            if (!this.sendToSpecificUser(message)) {
                threadClient.sendMessage(this.sendBackMessageToSender("User is not found."));
            }
        } else {
            threadClient.sendMessage(this.sendBackMessageToSender("Your command is invalid."));
        }
    }



    public Hashtable<String, ThreadClient> getClients() {
        return clients;
    }
}
