import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to the chat server.");
            
            new ReadThread(socket).start();
            new WriteThread(socket).start();
        } catch (IOException e) {
            System.err.println("Unable to connect to server: " + e.getMessage());
        }
    }

    static class ReadThread extends Thread {
        private BufferedReader in;

        ReadThread(Socket socket) {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                System.err.println("Error getting input stream.");
            }
        }

        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.err.println("Disconnected from server.");
            }
        }
    }

    static class WriteThread extends Thread {
        private PrintWriter out;
        private BufferedReader userInput;

        WriteThread(Socket socket) {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                userInput = new BufferedReader(new InputStreamReader(System.in));
            } catch (IOException e) {
                System.err.println("Error getting output stream.");
            }
        }

        public void run() {
            try {
                String message;
                while ((message = userInput.readLine()) != null) {
                    out.println(message);
                }
            } catch (IOException e) {
                System.err.println("Error sending message.");
            }
        }
    }
}
