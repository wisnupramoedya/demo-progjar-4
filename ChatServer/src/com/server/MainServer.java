package com.server;

public class MainServer {
    public static void main(String[] args) {
        try {
            ThreadServer threadServer = new ThreadServer();
            threadServer.start();
        } catch (Exception e) {
            System.err.println("Error: Server has been terminated. " + e.getMessage());
        }
    }
}
