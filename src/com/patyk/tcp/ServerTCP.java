package com.patyk.tcp;

/**
 *
 */

import com.patyk.baza.DanePolaczeniaBaza;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.patyk.baza.MainBaza.tryToConnectOrCreateDatabase;

/**
 * @author micha
 *
 */
public class ServerTCP {


    public static void uruchomSerwer(DanePolaczeniaBaza danePolaczeniaBaza, Integer portSerwera) {

        tryToConnectOrCreateDatabase(danePolaczeniaBaza);

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portSerwera);
            while (true) {
                System.out.println("aktywnych wątków: " + Thread.activeCount() + "\n");
                System.out.println("Czekam na połączenie na porcie " + portSerwera + "\n");
                Socket socket = serverSocket.accept();
                System.out.println("Odbieram połączenie \n");
                (new ServerTCPThread(socket, danePolaczeniaBaza)).start();
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
