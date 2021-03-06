package com.patyk.tcp;

/**
 *
 */

import com.patyk.baza.DanePolaczeniaBaza;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import static com.patyk.baza.MainBaza.databaseInsert;

/**
 * @author micha
 *
 */
public class ServerTCPThread extends Thread {
    DanePolaczeniaBaza danePolaczeniaBaza;
    Socket mySocket;

    /**
     *Konstruktor wątku serwera
     */
    public ServerTCPThread(Socket socket, DanePolaczeniaBaza danePolaczeniaBaza) {
        super();// konstruktor klasy Thread
        mySocket = socket;
        this.danePolaczeniaBaza = danePolaczeniaBaza;
    }

    /**
     * Program wątku
     */
    public void run()
    {
        try {
            System.out.println("Wątek servera próbuje odebrać wiadomość\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            String id;
            String wart;
            String milisDate;
            System.out.println("Watek serwera odbiera wiadomość\n");
            id = in.readLine();
            System.out.println(mySocket.getInetAddress() + " : " + id);
            wart = in.readLine();
            System.out.println(mySocket.getInetAddress() + " : " + wart);
            milisDate = in.readLine();
            System.out.println(mySocket.getInetAddress() + " : " + milisDate);
            mySocket.close();
            System.out.println("Watek serwera próbuje zapisac wartość do bazy danych\n");
            databaseInsert(Integer.valueOf(id), Float.valueOf(wart), Long.valueOf(milisDate), danePolaczeniaBaza);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
