import com.ww.Message;
import com.ww.User;
import com.ww.Utils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(Utils.HOST, Utils.PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ThreadClient threadClient = new ThreadClient(new ObjectInputStream(socket.getInputStream()));
            threadClient.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Input username: ");
            String username = bufferedReader.readLine();
            User user = new User();
            user.setFullName(username);

            while (true) {
                System.out.print("> ");
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
