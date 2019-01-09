package com.patyk;

import java.util.Date;

import static com.patyk.tcp.ClientTCP.wyslijKomunikatFloat;

public class Klient extends Thread {
    //TODO klient pownien być nowoczesnym watkiem
    private static Integer nextID = 0;
    private Integer ID;
    private Czujnik czujnik = new Czujnik();

    public Klient() {
        ID = nextID++;
    }

    public static void main(String[] args) {
        Klient kl = new Klient();
        kl.run();
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(czujnik.zmierzTemperature());
            wyslijKomunikatFloat(this.ID, czujnik.zmierzTemperature(), (new Date()).getTime(), "localhost");
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
