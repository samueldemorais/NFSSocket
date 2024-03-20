package com.example.nfssockets;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private static List<String> files = new ArrayList<>();

    public static void main(String[] args) {
        int port = 8888; 
        
        files.add("file1.txt");
        files.add("file2.txt");
        files.add("file3.txt");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor NFS iniciado na porta " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                Thread clientHandler = new Thread(new ClientHandler(clientSocket));
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    String[] tokens = inputLine.split(" ");

                    String command = tokens[0];
                    String response = "";

                    switch (command) {
                        case "readdir":
                            response = readdir();
                            break;
                        case "rename":
                            rename(tokens[1], tokens[2]);
                            break;
                        case "create":
                            create(tokens[1]);
                            break;
                        case "remove":
                            remove(tokens[1]);
                            break;
                        default:
                            response = "Comando inválido.";
                    }

                    out.println(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String readdir() {
            StringBuilder fileList = new StringBuilder();
            for (String file : files) {
                fileList.append(file).append("\n");
            }
            return fileList.toString();
        }

        private void rename(String oldName, String newName) {
            if (files.contains(oldName)) {
                files.remove(oldName);
                files.add(newName);
            }
        }

        private void create(String fileName) {
            files.add(fileName);
        }

        private void remove(String fileName) {
            files.remove(fileName);
        }
    }
}

