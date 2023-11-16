package server.threading;

import java.io.*;
import java.net.Socket;

public class RequestSolver implements Runnable {
    private static final int BUF_SIZE = 16;
    Socket clientSocket;

    RequestSolver(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (OutputStream outputToClient = clientSocket.getOutputStream();
             InputStream inputFromClient = clientSocket.getInputStream();
             BufferedReader fileReader = new BufferedReader(new FileReader("saracatunga.txt"));
        ) {
            byte[] clientInputBuffer = new byte[BUF_SIZE];
            int bytesRead = inputFromClient.read(clientInputBuffer);
            String clientInputString = new String(clientInputBuffer, 0, bytesRead);
            if (clientInputString.equals("yes")) {
                writeFileToOutStream(fileReader, outputToClient);
            } else if (clientInputString.equals("no")) {
                outputToClient.write("client didn't agree to receive file".getBytes());
            } else {
                outputToClient.write("command not recognized".getBytes());
            }
        } catch (IOException e) {
            System.err.println("Error trying to send data to client");
            e.printStackTrace();
        }
    }

    private static void writeFileToOutStream(BufferedReader file, OutputStream outputToClient) throws IOException {
        String line;
        while ((line = file.readLine()) != null) {
            line += "\n";
            outputToClient.write(line.getBytes());
        }
    }

}
