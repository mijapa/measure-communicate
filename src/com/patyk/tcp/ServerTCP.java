package com.patyk.tcp;

/**
 *
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.patyk.baza.MainBaza.tryToConnectOrCreateDatabase;

/**
 * @author micha
 *
 */
public class ServerTCP {

    /**
     * @param args
     */
    public static void main(String[] args) {
        uruchomSerwer("localhost", 3306, 8080);

    }

    public static void uruchomSerwer(String databaseAdres, int databasePort, Integer portSerwera) {
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

        tryToConnectOrCreateDatabase(databaseAdres, databasePort);

        port = portSerwera;
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
