import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class XTCP  {

    private int port = 4567;
    private ServerSocket server;
    private Socket socket;

    /*
     * Default Constructor
     */
    private XTCP() throws IOException {
        this.executeProgram();
    }

    /*
     * Constructor takes in port
     */
    private XTCP(int port) throws IOException {
        this.port = port;
        this.executeProgram();
    }

    /*
     * Entry point for the application
     *
     * @param args program arguments
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            new XTCP();
        } else if (args.length > 1) {
            System.out.println("usage: ./xtcp natural-number");
            System.exit(0);
        } else {
            try {
                new XTCP(Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {
                System.out.println("usage: ./xtcp natural-number");
                System.exit(0);
            }
        }
    }

    /*
     * Creates a socket, reads & writes to the socket, and closes the socket
     */
    private void executeProgram() throws IOException {
        // create socket
        this.server = new ServerSocket(this.port);
        this.server.setSoTimeout(3000); // sets connection timeout to 3 seconds
        this.socket = this.server.accept();

        // read & write to socket
        List<String> json = this.read(this.port);
        List<String> result = this.parseJSON(json);
        this.write(result);

        // close socket
        this.socket.close();
        this.server.close();
    }

    /*
     * Reads JSON values from the input side of a TCP connection
     */
    private List<String> read(int port) throws IOException {
        try {
            // read json from client
            Scanner scanner = new Scanner(this.socket.getInputStream());
            List<String> list = new ArrayList<>();
            while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }
            this.socket.shutdownInput(); // closes input side of socket

            return list;
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException("Timeout: client didn't connect to server in 3 seconds");
        }
    }

    /*
     * Parses the JSON values and returns a result
     */
    private List<String> parseJSON(List<String> json) throws IOException {
        // file path to execute xjson
        String filePath = new File(".").getCanonicalPath();
        filePath = filePath.substring(0, filePath.length() - 1) + "C/";

        // execute xjson
        ProcessBuilder processBuilder = new ProcessBuilder("./xjson");
        processBuilder.directory(new File(filePath));
        Process process = processBuilder.start();

        OutputStream stdin = process.getOutputStream();
        InputStream stdout = process.getInputStream();

        // writes json to xjson
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
        for (String j : json) {
            writer.write(j);
            writer.newLine();
        }
        writer.close(); // closes input side, i.e. EOF

        // reads json from xjson
        Scanner scanner = new Scanner(stdout);
        List<String> result = new ArrayList<>();
        while (scanner.hasNext()) {
            result.add(scanner.next());
        }

        process.destroy(); // terminates the process

        return result;
    }

    /*
     * Writes the JSON result to the client
     */
    private void write(List<String> result) throws IOException {
        PrintWriter writer = new PrintWriter(this.socket.getOutputStream(), true);
        for (String s : result) {
            writer.write(s + "\n");
        }
        writer.flush();
        writer.close();
    }

}