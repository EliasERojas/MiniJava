package server.threading;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private static final int MAX_THREADS = 8;
    private static final int DEFAULT_BACKLOG = 8;
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final int DEFAULT_PORT = 8080;
    private final ServerSocket sv;
    private final ExecutorService fixedThreadPool;

    public Server(String ip, int port, int backlog) throws IOException {
        fixedThreadPool = Executors.newFixedThreadPool(MAX_THREADS);
        InetSocketAddress inAdd = new InetSocketAddress(ip, port);
        sv = new ServerSocket();
        sv.bind(inAdd, backlog);
    }

    public Server(String ip, int port) throws IOException {
        this(ip, port, DEFAULT_BACKLOG);
    }

    public Server(String ip) throws IOException {
        this(ip, DEFAULT_PORT, DEFAULT_BACKLOG);
    }

    public Server() throws IOException {
        this(DEFAULT_IP, DEFAULT_PORT, DEFAULT_BACKLOG);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket s = sv.accept();
                RequestSolver requestSolver = new RequestSolver(s);
                fixedThreadPool.execute(requestSolver);
            }
        } catch (IOException e) {
            System.err.println("Couldn't open connection");
            e.printStackTrace();
        }
    }
}
