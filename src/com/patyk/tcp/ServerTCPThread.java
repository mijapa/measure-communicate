package com.patyk.tcp;

/**
 *
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import static com.patyk.baza.MainBaza.databaseInsert;

/**
 * @author micha
 *
 */
public class ServerTCPThread extends Thread {
    Socket mySocket;

    /**
     *
     */
    public ServerTCPThread(Socket socket) {
        super();// konstruktor klasy Thread
        mySocket = socket;
    }

    public void run()// proggram wątku
    {
        try {
            System.out.println("Wątek servera próbuje odebrać wiadomość\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            String id;
            String wart;
            String milisDate;
            System.out.println("Watek serwera odbiera wiadomość\n");
//            while (!(str = in.readLine()).equals("end"))
            id = in.readLine();
            System.out.println(mySocket.getInetAddress() + " : " + id);
            wart = in.readLine();
            System.out.println(mySocket.getInetAddress() + " : " + wart);
            milisDate = in.readLine();
            System.out.println(mySocket.getInetAddress() + " : " + milisDate);
            mySocket.close();
            System.out.println("Watek serwera próbuje zapisac wartość do bazy danych\n");
            databaseInsert(Integer.valueOf(id), Float.valueOf(wart), Long.valueOf(milisDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
