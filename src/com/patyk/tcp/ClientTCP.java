package com.patyk.tcp;

/**
 *
 */

import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author micha
 *
 */
public class ClientTCP {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        Scanner scan = new Scanner(System.in);
        int port = 0;
//        boolean portOK = false;
//		while (!portOK) {
//			try {
//				System.out.println("Podaj numer portu: \n");
//				port = Integer.parseInt(scan.nextLine());
//				portOK=true;
//			} catch (NumberFormatException nfe) {
//				System.err.println("Wprowadź poprawny numer portu: " + nfe);
//			}
//		}
//		scan.close();
        port = 8080;
        try {
            Socket socket = new Socket("localhost", port);
            Scanner file = new Scanner(new FileInputStream("plik.txt"));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner sc = new Scanner(System.in);
            String str;
            socket.setTcpNoDelay(true);
//			System.out.println("Wpisz wiadomość do przesłania: \n");
            while (file.hasNextLine()) {
                str = file.nextLine();
                out.println(str);
                out.flush();
                if (str.equals("end")) ;
            }
            System.out.println("Klient: Przesłałem wiadomość.");
            socket.close();
            file.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        System.out.println("Klient: Kończę prace.");
    }

    public static void wyslijKomunikatFloat(Float komunikat, String adres) {
        int port = 8080;
        try {
            Socket socket = new Socket(adres, port);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            socket.setTcpNoDelay(true);
            out.println(komunikat);
            out.println("end");
            out.flush();
            socket.close();
        } catch (Exception e) {
            System.err.println(e);
        }

    }
}
