package Sockets1;

import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private final String TERMINATION_COMMAND = "Exit";

    /**
     * Establece una conexión con el servidor 3307 por la dirección IP y el puerto.
     * @param ip La dirección IP del servidor.
     * @param port El puerto del servidor.
     */
    public void startClient(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            System.out.println("Connecting to " + socket.getInetAddress().getHostName());
            openStreams();
            communicate();
        } catch (IOException e) {
            System.out.println("Exception when connecting: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    /**
     * Abre los flujos de entrada y salida para la comunicación con el servidor.
     */
    private void openStreams() throws IOException {
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.flush();
    }

    /**
     * Envía un mensaje al servidor.
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
     * Cierra la conexión con el servidor.
     */
    private void closeConnection() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            System.out.println("IOException on close");
        }
    }

    /**
     * Gestiona la comunicación con el servidor.
     */
    private void communicate() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message;
        do {
            System.out.print("[You] => ");
            message = reader.readLine();
            sendMessage(message);
            System.out.println("[Server] => " + inputStream.readUTF());
        } while (!message.equals(TERMINATION_COMMAND));
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startClient("localhost", 3307);
    }
}


