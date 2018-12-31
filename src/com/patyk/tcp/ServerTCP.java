package com.patyk.tcp;

/**
 *
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author micha
 *
 */
public class ServerTCP {

    /**
     * @param args
     */
    public static void main(String[] args) {
        uruchomSerwer();

    }

    public static void uruchomSerwer() {
        int port;
        // boolean portOK = false;
        // while (!portOK) {
        // Scanner scan = new Scanner(System.in);
        // try {
        // System.out.println("Podaj numer dla serwera:");
        // port = Integer.parseInt(scan.next());
        // portOK=true;
        // } catch (NumberFormatException nfe) {
        // System.out.println("Wprowadź poprwny numer portu!\n" + nfe);
        // }finally {
        // scan.close();
        // }
        // }
        port = 8080;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                System.out.println("aktywnych wątków: " + Thread.activeCount() + "\n");
                System.out.println("Czekam na połączenie na porcie " + port + "\n");
                Socket socket = serverSocket.accept();
                System.out.println("Odbieram połączenie \n");
                (new ServerTCPThread(socket)).start();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
}
