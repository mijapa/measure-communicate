package com.patyk;

import java.util.Date;

import static com.patyk.tcp.ClientTCP.wyslijKomunikatFloat;

public class Klient extends Thread {
    private static Integer nextID = 0;
    private Integer ID;

    public Klient() {
        ID = nextID++;
    }

    public static void main(String[] args) {
        Klient kl = new Klient();
        kl.run();
    }

    private Float temperatura() {
        Czujnik czujnik = new Czujnik();
        return czujnik.zmierzTemperature();
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(this.temperatura());
            wyslijKomunikatFloat(this.ID, this.temperatura(), (new Date()).getTime(), "localhost");
            try {
                sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
