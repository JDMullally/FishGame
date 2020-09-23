import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class XTCP  {

    private XTCP(int port) throws IOException {
        this.listen(port);
    }

    /*
     * Entry point for the application
     *
     * @param args program arguments
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            new XTCP(4567);
        } else {
            try {
                if (args.length > 1) throw new Exception();
                new XTCP(Integer.parseInt(args[0]));
            } catch (Exception e) {
                System.out.println("usage: ./xtcp positive-integer");
                System.exit(0);
            }
        }
    }

    /*
     * Creates a socket with the specified port and listens for JSON values from the input side of a TCP connection
     */
    private void listen(int port) throws IOException {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(3000); // sets connection timeout to 3 seconds
            Socket socket = server.accept();

            Scanner scanner = new Scanner(socket.getInputStream());
            while (scanner.hasNext()) {
                System.out.println(scanner.next());
            }
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException("Timeout: client didn't connect to server in 3 seconds");
        }
    }

}