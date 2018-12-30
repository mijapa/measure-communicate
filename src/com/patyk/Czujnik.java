package com.patyk;

import java.util.Random;

public class Czujnik {
    static Float ostatniPomiar = Float.valueOf(20);
    public Float zmierzTemperature(){
        Random rand = new Random();
        Float temp = ostatniPomiar + (rand.nextFloat() - (float)0.5) * (float)0.01;
        ostatniPomiar = temp;
        return temp;
    }
}
