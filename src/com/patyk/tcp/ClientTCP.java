package com.patyk.tcp;

/**
 *
 */

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author micha
 *
 */
public class ClientTCP {
    /**
     * Statyczna metoda wysyłająca komunikat TCP do serwera
     * @param ID klienta
     * @param wart pomiaru
     * @param milisDate data w milisekundach
     * @param adres adres serwera
     * @param portSerwera port serwera
     */
    public static void wyslijKomunikatFloat(Integer ID, Float wart, long milisDate, String adres, Integer portSerwera) {
        try {
            Socket socket = new Socket(adres, portSerwera);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            socket.setTcpNoDelay(true);
            out.println(ID);
            out.println(wart);
            out.println(milisDate);
            out.println("end");
            out.flush();
            socket.close();
        } catch (Exception e) {
            System.err.println(e);
        }

    }
}
