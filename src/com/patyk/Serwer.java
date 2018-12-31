package com.patyk;

import static com.patyk.tcp.ServerTCP.uruchomSerwer;

public class Serwer {

    public static void main(String[] args) {
        Serwer serwer = new Serwer();
        serwer.uruchom();
    }

    public void uruchom() {
        System.out.println("Uruchamiam serwer");
        uruchomSerwer();
    }
}
