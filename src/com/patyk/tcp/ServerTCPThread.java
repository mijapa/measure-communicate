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
            String str;
            System.out.println("Watek serwera odbiera wiadomość\n");
//            while (!(str = in.readLine()).equals("end"))
            str = in.readLine();
                System.out.println(mySocket.getInetAddress() + " : " + str);
            mySocket.close();
            System.out.println("Watek serwera próbuje zapisac wartość do bazy danych\n");
            databaseInsert(0, Float.valueOf(str));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
