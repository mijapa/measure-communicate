package com.patyk.gui;

import com.patyk.Serwer;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.patyk.gui.SwingWorkerRealTime.CZUJNIK_1;
import static com.patyk.gui.SwingWorkerRealTime.ILOSC_CZUJNIKOW;

public class SerwerGUI extends JFrame {
    private JButton uruchomSerwerButton;
    private Executor exec = Executors.newCachedThreadPool();
    private JTextField adresBazyField;
    private JTextField portBazyField;
    private JTextField portNasluchuField;

    public SerwerGUI() throws HeadlessException {
        super("Serwer");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(2000, 800);
        setLocation(50, 50);

        setLayout(new BorderLayout());

        JPanel westPanel = new JPanel();
        westPanel.setLayout(new GridLayout(25, 1));
        westPanel.add(new JLabel("adres bazy danych:"));
        adresBazyField = new JTextField("localhost", 15);
        westPanel.add(adresBazyField);
        westPanel.add(new JLabel("port bazy danych:"));
        portBazyField = new JTextField("3306", 15);
        westPanel.add(portBazyField);
        westPanel.add(new JLabel("port nasłuchu dla czujników:"));
        portNasluchuField = new JTextField("8080", 15);
        westPanel.add(portNasluchuField);


        westPanel.add(new UruchomSerwerButton());

        //TODO uruchamianie czujników przy pomocy guzika


        add(westPanel, BorderLayout.WEST);

        SwingWorkerRealTime swingWorkerRealTime = new SwingWorkerRealTime();
        swingWorkerRealTime.go();
        XYChart chart =
                QuickChart.getChart(
                        "Czujniki",
                        "Czas [s]",
                        "Temperatura [C]",
                        CZUJNIK_1,
                        new double[]{0},
                        new double[]{0});
        chart.getStyler().setXAxisTicksVisible(false);

        for (int i = 2; i <= ILOSC_CZUJNIKOW; i++) {
            chart.addSeries("czujnik " + i, new double[]{0}, new double[]{0});
        }
        JPanel chartPanel = new XChartPanel<XYChart>(chart);
        chartPanel.setSize(1024, 600);
        add(chartPanel, BorderLayout.CENTER);


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
            String adresBazy = adresBazyField.getText();
            Integer portBazy = Integer.parseInt(portBazyField.getText());
            Integer portSerwera = Integer.parseInt(portNasluchuField.getText());
            exec.execute(new Serwer(adresBazy, portBazy, portSerwera));
            setText("Serwer Uruchomiony");
            setEnabled(false);
        }
    }
}

