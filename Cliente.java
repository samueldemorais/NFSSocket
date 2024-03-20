package com.example.nfssockets;

import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Endereço do servidor
        int serverPort = 8888; // Porta utilizada pelo servidor

        try (
            Socket socket = new Socket(serverAddress, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Conectado ao servidor NFS.");

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                String serverResponse = in.readLine();
                System.out.println("Servidor: " + serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

