package com.patyk;

import java.util.Date;
import java.util.concurrent.Callable;

import static com.patyk.tcp.ClientTCP.wyslijKomunikatFloat;
import static java.lang.Thread.sleep;

public class Klient implements Callable<Double> {
    private static Integer nextID = 0;
    public boolean stop = false;
    private Integer ID;
    private Czujnik czujnik = new Czujnik();
    private Float temperatura;
    private Double suma = Double.valueOf(0);
    private Integer ilosc = 0;
    private String adresSerwera;
    private Integer portSerwera;

    public Klient(String adresSerwera, Integer portSerwera) {
        ID = nextID++;
        this.adresSerwera = adresSerwera;
        this.portSerwera = portSerwera;

    }

    /**
     * Program wątku
     *
     * @return średnia pomiarów
     * @throws Exception
     */
    @Override
    public Double call() throws Exception {
        while (true) {
            System.out.println(czujnik.zmierzTemperature());
            temperatura = czujnik.zmierzTemperature();
            suma += temperatura;
            ilosc++;
            wyslijKomunikatFloat(this.ID, temperatura, (new Date()).getTime(), adresSerwera, portSerwera);
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (stop) return suma / ilosc;
        }
    }
}
