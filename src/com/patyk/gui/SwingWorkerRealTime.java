package com.patyk.gui;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import static com.patyk.baza.Baza.*;

/**
 * Creates a real-time chart using SwingWorker
 */
public class SwingWorkerRealTime {
    public static final String CZUJNIK_1 = "czujnik 1";
    public static final Integer ILOSC_CZUJNIKOW = 10;
    //TODO zastąp jakos milisData na wykresie, mozna odjąc dzisiejszą datę i wyświetlać tylko krótkie dzisiejsze sekundy

    private static String sql;
    private static Statement s;
    private static Connection connection;
    MySwingWorker mySwingWorker;
    SwingWrapper<XYChart> sw;
    XYChart chart;

    public static void main(String[] args) throws Exception {

        if (ladujSterownik()) ;
        else {
            System.exit(1);
        }

        connection = connectToDatabase("localhost:3306", "IntDom", "root", "");

        // WYKONYWANIE OPERACJI NA BAZIE DANYCH
        System.out.println("Pobieranie danych z bazy:");



        SwingWorkerRealTime swingWorkerRealTime = new SwingWorkerRealTime();
        swingWorkerRealTime.go();

    }

    private void go() {

        // Create Chart
        chart =
                QuickChart.getChart(
                        "Czujniki",
                        "Czas [s]",
                        "Temperatura [C]",
                        CZUJNIK_1,
                        new double[]{0},
                        new double[]{0});
//        chart.getStyler().setLegendVisible(false);
//        chart.setTitle("CZUJNIKI");
//        chart.setXAxisTitle("Czas [s]");
//        chart.setYAxisTitle("Temperatura [C]");
        chart.getStyler().setXAxisTicksVisible(false);
        for (int i = 2; i <= ILOSC_CZUJNIKOW; i++) {
            chart.addSeries("czujnik " + i, new double[]{0}, new double[]{0});
        }

        // Show it
        sw = new SwingWrapper<XYChart>(chart);
        sw.displayChart();

        mySwingWorker = new MySwingWorker();
        mySwingWorker.execute();
    }

    class XYData {
        private double x;
        private double y;

        public XYData(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    class CzujnikiData {
        private XYData[] xyData;

        public CzujnikiData(XYData[] xyData) {
            this.xyData = xyData;
        }
    }

    private class MySwingWorker extends SwingWorker<Boolean, CzujnikiData[]> {

        final LinkedList<CzujnikiData> fifo = new LinkedList<>();

        public MySwingWorker() {

//            fifo.add(new XYData(0.0, 0.0));
        }

        @Override
        protected Boolean doInBackground() throws Exception {


            while (!isCancelled()) {
                XYData[] xyData = new XYData[ILOSC_CZUJNIKOW];
                for (int i = 0; i < ILOSC_CZUJNIKOW; i++) {
                    xyData[i] = new XYData(getDataX(i), getDataY(i));
                }
                CzujnikiData data = new CzujnikiData(xyData);
                fifo.add(data);
                if (fifo.size() > 500) {
                    fifo.removeFirst();
                }

                CzujnikiData[] array = new CzujnikiData[fifo.size()];
                for (int i = 0; i < fifo.size(); i++) {
                    array[i] = fifo.get(i);
                }
                publish(array);

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    // eat it. caught when interrupt is called
                    System.out.println("MySwingWorker shut down.");
                }
            }

            return true;
        }

        @Override
        protected void process(List<CzujnikiData[]> chunks) {

            System.out.println("number of chunks: " + chunks.size());

//            XYData[] mostRecentDataSet = chunks.get(chunks.size() - 1);
            CzujnikiData[] mostRecentDataSet = chunks.get(chunks.size() - 1);
            XYData[] xyData = new XYData[mostRecentDataSet.length];
            double[][] mostRecentDataSetX = new double[ILOSC_CZUJNIKOW][mostRecentDataSet.length];
            double[][] mostRecentDataSetY = new double[ILOSC_CZUJNIKOW][mostRecentDataSet.length];

            for (int i = 0; i < mostRecentDataSet.length; i++) {
                for (int j = 0; j < ILOSC_CZUJNIKOW; j++) {
                    xyData[i] = mostRecentDataSet[i].xyData[j];
                    mostRecentDataSetX[j][i] = xyData[i].x;
                    mostRecentDataSetY[j][i] = xyData[i].y;

                }

            }

            for (int i = 0; i < ILOSC_CZUJNIKOW; i++) {
                chart.updateXYSeries("czujnik " + (i + 1), mostRecentDataSetX[i], mostRecentDataSetY[i], null);
            }
            sw.repaintChart();

            long start = System.currentTimeMillis();
            long duration = System.currentTimeMillis() - start;
            try {
                Thread.sleep(40 - duration); // 40 ms ==> 25fps
                // Thread.sleep(400 - duration); // 400 ms ==> 2.5fps
            } catch (InterruptedException e) {
                System.out.println("InterruptedException occurred.");
            }
        }

        protected Double getDataY(int id) {
            Double data;
            sql = "SELECT * FROM czujniki where id=" + id + " ORDER BY milisDate DESC LIMIT 1";
            s = createStatement(connection);
            ResultSet r = executeQuery(s, sql);
            data = 1.0;
            try {
                r.first();
                data = (double) r.getFloat("wart");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return data;
        }

        protected Double getDataX(int id) {
            sql = "SELECT * FROM czujniki where id=" + id + " ORDER BY milisDate DESC LIMIT 1";
            s = createStatement(connection);
            ResultSet r = executeQuery(s, sql);
            Double data = 1.0;
            try {
                r.first();
                data = Double.valueOf(r.getLong("milisDate"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return data / 1000;
        }
    }
}