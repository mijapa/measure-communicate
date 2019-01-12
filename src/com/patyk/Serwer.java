package com.patyk;

import static com.patyk.tcp.ServerTCP.uruchomSerwer;

public class Serwer implements Runnable {
    private final Integer portBazy;
    private final Integer portSerwera;
    private String adresBazy;

    public Serwer(String adresBazy, Integer portBazy, Integer portSerwera) {
        this.adresBazy = adresBazy;
        this.portBazy = portBazy;
        this.portSerwera = portSerwera;
    }

    public static void main(String[] args) {
        Serwer serwer = new Serwer("localhost", 3306, 8080);
        serwer.run();
    }

    @Override
    public void run() {
//        TODO jak przekazać parametry do wątku??
        System.out.println("Uruchamiam serwer");

        uruchomSerwer(adresBazy, portBazy, portSerwera);
    }
}
