package model.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class SignUpServer {

    private final int PORT = 4567;
    private ServerSocket server;
    private List<Client> clientList;

    private SignUpServer() throws IOException {
        this.clientList = new ArrayList<>();
        this.server = new ServerSocket(this.PORT);
        this.server.setSoTimeout(30000); // sets connection timeout to 30 seconds
        //this.signUp();
    }

    private void signUp() throws IOException {
        int repeat = 0;
        while (repeat < 2) {
            try {
                if (clientList.size() < 10) {
                    Socket socket = this.server.accept();
                    Client client = new Client(socket, clientList.size() + 1);
                    this.clientList.add(client);
                } else {
                    break;
                }
            } catch (SocketTimeoutException e) {
                if (repeat > 0 && clientList.size() < 5) {
                    System.out.println("Not enough players connected");
                    return;
                }
            }
            repeat++;
        }
    }

}
