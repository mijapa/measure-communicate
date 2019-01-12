package com.patyk.gui;

import com.patyk.Klient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KlientGUI extends JFrame {
    private final JTextField adresSerweraField;
    private final JTextField portSerweraField;
    private Future<Double> srednia;
    private ExecutorService exec = Executors.newCachedThreadPool();

    public KlientGUI() {
        super("Klient");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(400, 400);
        setLocation(50, 800);

        setLayout(new FlowLayout());

        JPanel westPanel = new JPanel();
        westPanel.setLayout(new GridLayout(25, 1));
        westPanel.add(new JLabel("adres serwera:"));

        adresSerweraField = new JTextField("localhost", 15);
        westPanel.add(adresSerweraField);

        westPanel.add(new JLabel("port serwera danych:"));

        portSerweraField = new JTextField("8080", 15);
        westPanel.add(portSerweraField);


        westPanel.add(new UruchomSerwerButton());

        add(westPanel, BorderLayout.WEST);

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
            String adresSerwera = adresSerweraField.getText();
            Integer portSerwera = Integer.parseInt(portSerweraField.getText());
            srednia = exec.submit(new Klient());
            setText("Klient Uruchomiony");
            setEnabled(false);
        }
    }
}
