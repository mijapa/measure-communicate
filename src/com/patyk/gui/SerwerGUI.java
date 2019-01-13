package com.patyk.gui;

import com.patyk.Serwer;
import com.patyk.baza.DanePolaczeniaBaza;
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

/**
 * GUI Serwera
 */
public class SerwerGUI extends JFrame {
    XYChart chart;
    SerwerGUI gui;
    private Executor exec = Executors.newCachedThreadPool();
    private JTextField adresBazyField;
    private JTextField portBazyField;
    private JTextField portNasluchuField;
    private JTextField userField;
    private JTextField passwordField;

    public SerwerGUI() throws HeadlessException {
        super("Serwer");
        gui = this;
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

        westPanel.add(new JLabel("urzytkownik bazy:"));
        userField = new JTextField("root", 15);
        westPanel.add(userField);

        westPanel.add(new JLabel("hasło użytkownika bazy:"));
        passwordField = new JTextField("", 15);
        westPanel.add(passwordField);

        westPanel.add(new JLabel("port nasłuchu dla czujników:"));
        portNasluchuField = new JTextField("8080", 15);
        westPanel.add(portNasluchuField);


        westPanel.add(new UruchomSerwerButton());
        JLabel threadCountLabel = new JLabel("-");
        westPanel.add(threadCountLabel);

        ThreadCountSwingWorker threadCountSwingWorker = new ThreadCountSwingWorker(threadCountLabel);
        threadCountSwingWorker.execute();

        add(westPanel, BorderLayout.WEST);

        chart =
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

        String adresBazy = adresBazyField.getText();
        Integer portBazy = Integer.parseInt(portBazyField.getText());
        String user = userField.getText();
        String password = passwordField.getText();
        DanePolaczeniaBaza danePolaczeniaBaza = new DanePolaczeniaBaza(adresBazy, portBazy, user, password);

        uruchomSwingWorkera(danePolaczeniaBaza);


        setVisible(true);
    }

    /**
     * Uruchamia SwingWorkera aktualizujacego wykres
     *
     * @param danePolaczeniaBaza
     */
    public void uruchomSwingWorkera(DanePolaczeniaBaza danePolaczeniaBaza) {
        SwingWorkerRealTime swingWorkerRealTime = new SwingWorkerRealTime(chart, gui, danePolaczeniaBaza);
        swingWorkerRealTime.go();
    }

    /**
     * Spersonalizowany przycisk
     */
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
            String user = userField.getText();
            String password = passwordField.getText();
            DanePolaczeniaBaza danePolaczeniaBaza = new DanePolaczeniaBaza(adresBazy, portBazy, user, password);
            exec.execute(new Serwer(danePolaczeniaBaza, portSerwera));
            setText("Serwer Uruchomiony");
            uruchomSwingWorkera(danePolaczeniaBaza);
            setEnabled(false);
        }
    }

    /**
     * AsyncTask aktualizujący JLabel z ilością aktywnych wątków
     */
    class ThreadCountSwingWorker extends SwingWorker<Void, JLabel> {

        private JLabel threadCountLabel;

        public ThreadCountSwingWorker(JLabel threadCountLabel) {
            this.threadCountLabel = threadCountLabel;
        }

        @Override
        protected Void doInBackground() throws Exception {
            while (!isCancelled()) {
                threadCountLabel.setText("Aktywnych Watków: " + Thread.activeCount());
            }
            return null;
        }
    }
}

