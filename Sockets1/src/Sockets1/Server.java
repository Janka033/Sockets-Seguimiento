package Sockets1;

import java.io.*;
import java.net.*;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private final String TERMINATION_COMMAND = "Exit";

    /**
     * Establece una conexión en el puerto 3307.
     * @param puerto El puerto en el que se va a escuchar la conexión.
     */
    public void startServer(int puerto) {
        try {
            serverSocket = new ServerSocket(puerto);
            System.out.println("Waiting for incoming connection on port " + puerto + "...");
            socket = serverSocket.accept();
            System.out.println("Connection established with " + socket.getInetAddress().getHostName());
            openStreams();
            communicate();
        } catch (IOException e) {
            System.out.println("Exception when starting server: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    /**
     * Abre los flujos de entrada y salida para la comunicación con el cliente.
     */
    private void openStreams() throws IOException {
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.flush();
    }

    /**
     * Envía un mensaje al cliente.
     * @param message El mensaje a enviar.
     */
    private void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("IOException on send");
        }
    }

    /**
     * Cierra la conexión con el cliente.
     */
    private void closeConnection() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            System.out.println("IOException on close: " + e.getMessage());
        }
    }

    /**
     * Gestiona la comunicación con el cliente.
     */
    private void communicate() throws IOException {
        String receivedMessage;
        do {
            receivedMessage = inputStream.readUTF();
            System.out.println("\n[Client] => " + receivedMessage);
            sendMessage("[Server] => Message received: " + receivedMessage);
        } while (!receivedMessage.equals(TERMINATION_COMMAND));
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer(3307);
    }
}
