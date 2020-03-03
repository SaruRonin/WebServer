package org.codeforall.codecadet.preditor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class WebServer {



    private final static int PORT = 8080;
    private Thread thread;




    public void connect() {

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            Socket clientSocket;

            while (true) {

                clientSocket = serverSocket.accept();
                System.out.println("connection established");
                thread = new Thread(new Dispatcher(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}