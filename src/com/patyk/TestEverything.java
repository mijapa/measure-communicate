package com.patyk;

import com.patyk.gui.KlientGUI;
import com.patyk.gui.SerwerGUI;

import java.awt.*;

import static com.patyk.gui.SwingWorkerRealTime.ILOSC_CZUJNIKOW;

public class TestEverything {


    public static void main(String[] args) {


        EventQueue.invokeLater(() -> new SerwerGUI());
        for (int i = 0; i < ILOSC_CZUJNIKOW; i++) {
            EventQueue.invokeLater(() -> new KlientGUI());
        }


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
