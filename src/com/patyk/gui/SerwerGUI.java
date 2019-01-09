package com.patyk.gui;

import com.patyk.Serwer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SerwerGUI extends JFrame {
    private JButton uruchomSerwerButton;
    private Executor exec = Executors.newCachedThreadPool();

    public SerwerGUI() throws HeadlessException {
        super("Serwer");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(600, 600);
        setLocation(50, 50);

        setLayout(new FlowLayout());

        add(new UruchomSerwerButton());

//        JPanel chartPanel = new XChartPanel<XYChart>(chart);
//        add(chartPanel);


        setVisible(true);

    }

    class UruchomSerwerButton extends JButton implements ActionListener {
        public UruchomSerwerButton() {
            super("Uruchom Serwer");
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setBackground(Color.GREEN);
            exec.execute(new Serwer());
            setText("Serwer Uruchomiony");
            setEnabled(false);
        }
    }
}

