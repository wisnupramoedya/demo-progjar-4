package com.client;

import com.ssl.SSLSocketKeystoreFactory;
import com.ww.Message;
import com.ww.User;
import com.ww.Utils;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class MainClient {
    public static void main(String[] args) {
        try {
            SSLSocket socket = SSLSocketKeystoreFactory.getSocketWithCert(
                    Utils.HOST, Utils.PORT, Utils.getPublicCertPath(), Utils.PUBLIC_CERT_PASSWORD,
                    SSLSocketKeystoreFactory.SecureType.SSL
            );
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            CommandReaderClient.commandStarter();
            CommandReaderClient commandReaderClient = new CommandReaderClient();

            System.out.print("Login as: ");
            String name = bufferedReader.readLine();
            User user = commandReaderClient.createUsername(name);
            System.out.println("Username named " + user.getFullName() + " has been created.");

            System.out.println("Start the connection.");
            try {
                ThreadClient threadClient = new ThreadClient(socket, new ObjectInputStream(socket.getInputStream()));
                threadClient.start();
                objectOutputStream.writeObject(user);
                objectOutputStream.flush();
            } catch (IOException e) {
                throw new IOException("Socket is disconnected");
            }

            while (true) {
                try {
                    String text = bufferedReader.readLine();
                    Message message = commandReaderClient.createMessage(text, user);

                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();
                } catch (Exception e) {
                    System.err.println("Error: Client could not send messages to the server. " + e.getMessage());
                    break;
                }
            }

            System.out.println("Connection stop.");
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException | CertificateException | KeyStoreException e) {
            System.err.println("Error: Client program is shutdown. " + e.getMessage());
        }
    }
}
