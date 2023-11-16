package server.threading;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client implements Runnable {
    public static final String YES = "yes";
    public static final String NO = "no";
    public static final int SIZE = 256;
    private String stringToServer;
    private final Socket serverConn;
    private String filename = "file-copy-";

    public Client(String ip, int port) throws IOException {
        serverConn = new Socket(ip, port);
    }

    @Override
    public void run() {
        try (OutputStream outputToServer = serverConn.getOutputStream();
             InputStream inputFromServer = serverConn.getInputStream();) {
            if (stringToServer == null) {
                System.err.println("Must set string to send to server before connecting");
                return;
            }
            updateFilenameWithThreadId();
            outputToServer.write(stringToServer.getBytes());
            writeInStreamToFile(inputFromServer, filename);

        } catch (IOException e) {
            try {
                Files.delete(Path.of(filename));
            } catch (IOException fileUndeleteableException) {
                fileUndeleteableException.printStackTrace();
            }
            System.err.println("Error copying file");
        }
    }

    public void setStringToServer(String stringToServer) {
        this.stringToServer = stringToServer;
    }

    public String getStringToServer() {
        return stringToServer;
    }

    private void updateFilenameWithThreadId() {
        filename += Thread.currentThread().getId();
    }

    private void writeInStreamToFile(InputStream inputStream, String filename) throws IOException {
        try (InputStreamReader byteToCharTranslator = new InputStreamReader(inputStream);
             FileWriter fileToWrite = new FileWriter(filename)) {
            char[] charBuf = new char[SIZE];
            int charsRead;
            while ((charsRead = byteToCharTranslator.read(charBuf, 0, SIZE)) != -1) {
                fileToWrite.write(charBuf, 0, charsRead);
            }
        }
    }

}
