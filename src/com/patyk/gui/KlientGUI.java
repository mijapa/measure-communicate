package com.patyk.gui;

import com.patyk.Klient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * GUI klienta
 */
public class KlientGUI extends JFrame {
    private final JTextField adresSerweraField;
    private final JTextField portSerweraField;
    private final JLabel sredniaLabel;
    private Future<Double> sredniaFuture;
    private ExecutorService exec = Executors.newCachedThreadPool();

    public KlientGUI() {
        super("Klient");
        pack();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        setSize(300, 200);
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


        westPanel.add(new UruchomKlientaButton());

        sredniaLabel = new JLabel();
        sredniaLabel.setVisible(false);
        westPanel.add(sredniaLabel);

        add(westPanel, BorderLayout.WEST);

        setVisible(true);
    }

    /**
     * Personalizowany przycisk
     */
    class UruchomKlientaButton extends JButton implements ActionListener {
        private Klient klient;

        public UruchomKlientaButton() {
            super("Uruchom Klienta");
            addActionListener(this);
        }

        /**
         * Callback wykonywany przy naciśnięciu Personalizowanego przycisku
         *
         * @param actionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (getText().equals("Uruchom Klienta")) {
                setBackground(Color.GREEN);
                String adresSerwera = adresSerweraField.getText();
                Integer portSerwera = Integer.parseInt(portSerweraField.getText());
                klient = new Klient(adresSerwera, portSerwera);
                sredniaFuture = exec.submit(klient);
                setText("Zatrzymaj Klienta");
            } else {
                setBackground(Color.gray);
                Double srednia = null;
                klient.stop = true;
                try {
                    srednia = sredniaFuture.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                sredniaLabel.setText(srednia.toString());
                sredniaLabel.setVisible(true);
                setText("Klient Zatrzymany");
                setEnabled(false);

            }
        }
    }
}
