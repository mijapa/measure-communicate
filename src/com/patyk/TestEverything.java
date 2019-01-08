package com.patyk;

public class TestEverything {
    public static void main(String[] args) {
        (new Klient()).start();
        (new Klient()).start();
        (new Klient()).start();
        (new Klient()).start();
        (new Klient()).start();
        (new Klient()).start();
        (new Klient()).start();
        (new Klient()).start();
        (new Klient()).start();
        (new Klient()).start();
        Serwer serwer = new Serwer();
        serwer.uruchom();
    }
}
