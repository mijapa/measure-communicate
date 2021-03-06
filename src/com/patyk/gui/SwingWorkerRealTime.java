package com.patyk.gui;

import com.patyk.baza.DanePolaczeniaBaza;
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
import static com.patyk.baza.MainBaza.DATABASE_NAME;

/**
 * Creates a real-time chart using SwingWorker
 */
public class SwingWorkerRealTime {
    public static final String CZUJNIK_1 = "czujnik 1";
    public static final Integer ILOSC_CZUJNIKOW = 10;

    private static String sql;
    private static Statement s;
    private static Connection connection;
    MySwingWorker mySwingWorker;
    SwingWrapper<XYChart> sw;
    public XYChart chart;
    private SerwerGUI serwerGUI;
    private DanePolaczeniaBaza danePolaczeniaBaza;

    public SwingWorkerRealTime(XYChart chart, SerwerGUI serwerGUI, DanePolaczeniaBaza danePolaczeniaBaza) {
        this.chart = chart;
        this.serwerGUI = serwerGUI;
        this.danePolaczeniaBaza = danePolaczeniaBaza;
    }


    public void go() {
        if (ladujSterownik()) ;
        else {
            System.exit(1);
        }
//        tryToConnectOrCreateDatabase(danePolaczeniaBaza);
        connection = connectToDatabase(
                danePolaczeniaBaza.getAdresBazy() + ":" + danePolaczeniaBaza.getPortBazy(),
                DATABASE_NAME,
                danePolaczeniaBaza.getUser(),
                danePolaczeniaBaza.getPassword());
//        connection = connectToDatabase("localhost:3306", "IntDom", "root", "");
        s = createStatement(connection);

        // WYKONYWANIE OPERACJI NA BAZIE DANYCH
        System.out.println("Pobieranie danych z bazy:");




//        // Show it
//        sw = new SwingWrapper<XYChart>(chart);
//        sw.displayChart();

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


        @Override
        protected Boolean doInBackground() throws Exception {


            while (!isCancelled()) {
                XYData[] xyData = new XYData[ILOSC_CZUJNIKOW];
                for (int i = 0; i < ILOSC_CZUJNIKOW; i++) {
                    xyData[i] = new XYData(getDataX(i), getDataY(i));
                }
                CzujnikiData data = new CzujnikiData(xyData);
                fifo.add(data);
                if (fifo.size() > 1000) {
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

            long start = System.currentTimeMillis();
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
//            sw.repaintChart();
            serwerGUI.repaint();


            long duration = System.currentTimeMillis() - start;
            try {
                Thread.sleep(20 - duration); // 40 ms ==> 25fps
                // Thread.sleep(400 - duration); // 400 ms ==> 2.5fps
            } catch (InterruptedException e) {
                System.out.println("InterruptedException occurred.");
            }
        }

        protected Double getDataY(int id) {
            Double data;
            sql = "SELECT * FROM czujniki where id=" + id + " ORDER BY milisDate DESC LIMIT 1";
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