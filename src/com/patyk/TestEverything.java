package com.patyk;

import com.patyk.gui.SerwerGUI;

import java.awt.*;

public class TestEverything {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SerwerGUI();
            }
        });

//        (new Klient()).start();
//        (new Klient()).start();
//        (new Klient()).start();
//        (new Klient()).start();
//        (new Klient()).start();
//        (new Klient()).start();
//        (new Klient()).start();
//        (new Klient()).start();
//        (new Klient()).start();
//        (new Klient()).start();
//        Serwer serwer = new Serwer();
//        serwer.uruchom();
    }
}
