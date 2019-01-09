package com.patyk;

import static com.patyk.tcp.ServerTCP.uruchomSerwer;

public class Serwer implements Runnable {

    public static void main(String[] args) {
        Serwer serwer = new Serwer();
        serwer.run();
    }

    @Override
    public void run() {
        System.out.println("Uruchamiam serwer");
        uruchomSerwer();
    }
}
