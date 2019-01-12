package com.patyk;

import java.util.Date;
import java.util.concurrent.Callable;

import static com.patyk.tcp.ClientTCP.wyslijKomunikatFloat;
import static java.lang.Thread.sleep;

public class Klient implements Callable<Double> {
    //TODO klient pownien być nowoczesnym watkiem
    private static Integer nextID = 0;
    private Integer ID;
    private Czujnik czujnik = new Czujnik();
    private Float temperatura;
    private Double suma = Double.valueOf(0);
    private Integer ilosc = 0;

    public Klient() {
        ID = nextID++;
    }

    public static void main(String[] args) {
        Klient kl = new Klient();
        try {
            kl.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Double call() throws Exception {
        while (true) {
            System.out.println(czujnik.zmierzTemperature());
            temperatura = czujnik.zmierzTemperature();
            suma += temperatura;
            ilosc++;
            wyslijKomunikatFloat(this.ID, temperatura, (new Date()).getTime(), "localhost");
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Thread.currentThread().isInterrupted()) return suma / ilosc;
        }
    }
}
