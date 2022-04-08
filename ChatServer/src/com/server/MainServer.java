package com.server;

import java.io.IOException;

public class MainServer {
    public static void main(String[] args) {
        try {
            ThreadServer threadServer = new ThreadServer();
            threadServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
