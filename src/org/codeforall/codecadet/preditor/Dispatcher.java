package org.codeforall.codecadet.preditor;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Dispatcher implements Runnable {
    Socket clientSocket;
    private static final List<String> TEXT_EXTENSIONS = Arrays.asList("txt", "html");
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "png", "jpeg");
    private BufferedReader inputStream = null;


    public Dispatcher(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        processRequest();

    }


    public void processRequest() {


        try {
            String incomingHeader = inputStream.readLine();
            if (incomingHeader == null) {
                return;
            }
            String[] headers = incomingHeader.split(" ");
            String stringFile = headers[1];
            File file = new File("www/" + stringFile);

            handleRequest(file);




        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void handleRequest(File file) {
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(clientSocket.getOutputStream());


            if (!file.exists()) {
                file = new File("www/404.html");

            }else if (file.isDirectory()){
                file = new File("www/index.html");
            }

            outputStream.write(makeHeader(file).getBytes());

            outputStream.flush();

            System.out.println(file.toString());

            FileInputStream filereader = new FileInputStream(file);
            int b;
            while ((b = filereader.read()) != -1) {
                outputStream.write(b);
            }
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String makeHeader(File file) {
        String header;
        String code = "";
        String type = "";
        String[] typeArray = file.getName().split("\\.");
        String extension = typeArray[1];

        if (!file.exists()) {
            code = "404 Not Found";
            type = "text/html; charset=UTF-8";

        }else if (TEXT_EXTENSIONS.contains(extension)) {
            code = "200 Document Follows";
            type = "text/html; charset=UTF-8";

        } else if (IMAGE_EXTENSIONS.contains(extension)) {
            code = "200 Document Follows";
            type = "image/" + extension;
        }
        header = "HTTP/1.0 " + code + "\r\n" +
                "Content-Type: " + type + "\r\n" +
                "Content-Length: " + file.length() + " \r\n\r\n";

        return header;
    }
}

