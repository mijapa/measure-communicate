package com.patyk;

import static com.patyk.tcp.ClientTCP.wyslijKomunikatFloat;
import static java.lang.Thread.sleep;

public class Klient {
    private static Integer nextID = 0;
    private Integer ID;

    public Klient() {
        ID = nextID++;
    }

    public static void main(String[] args) {
        Klient kl = new Klient();
        while (true) {
            System.out.println(kl.temperatura());
            wyslijKomunikatFloat(kl.temperatura(), "localhost");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Float temperatura() {
        Czujnik czujnik = new Czujnik();
        return czujnik.zmierzTemperature();
    }

}
