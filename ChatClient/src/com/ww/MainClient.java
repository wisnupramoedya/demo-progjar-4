package com.ww;

import java.io.*;
import java.net.Socket;

public class MainClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(Utils.HOST, Utils.PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ThreadClient threadClient = new ThreadClient(new ObjectInputStream(socket.getInputStream()));
            threadClient.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Username: ");
            String username = bufferedReader.readLine();
            User user = new User();
            user.setFullName(username);

            while (true) {
                String text = bufferedReader.readLine();

                Message message = new Message();
                message.setUser(user);
                message.setText(text);

                objectOutputStream.writeObject(message);
                objectOutputStream.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
