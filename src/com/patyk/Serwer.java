package com.patyk;

import com.patyk.baza.DanePolaczeniaBaza;

import static com.patyk.tcp.ServerTCP.uruchomSerwer;

public class Serwer implements Runnable {
    private final Integer portSerwera;
    private final DanePolaczeniaBaza danePolaczeniaBaza;

    public Serwer(DanePolaczeniaBaza danePolaczeniaBaza, Integer portSerwera) {
        this.danePolaczeniaBaza = danePolaczeniaBaza;
        this.portSerwera = portSerwera;
    }

    @Override
    public void run() {
        System.out.println("Uruchamiam serwer");

        uruchomSerwer(danePolaczeniaBaza, portSerwera);
    }
}
