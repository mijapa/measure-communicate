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
        sql = "SELECT * FROM czujniki where id=0 ORDER BY sample DESC LIMIT 1";
        s = createStatement(connection);


        SwingWorkerRealTime swingWorkerRealTime = new SwingWorkerRealTime();
        swingWorkerRealTime.go();

    }

    private void go() {

        // Create Chart
        chart =
                QuickChart.getChart(
                        "SwingWorker XChart Real-time Demo",
                        "Time",
                        "Value",
                        "randomWalk",
                        new double[]{0},
                        new double[]{0});
        chart.getStyler().setLegendVisible(false);
//        chart.getStyler().setXAxisTicksVisible(false);

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

    private class MySwingWorker extends SwingWorker<Boolean, XYData[]> {

        final LinkedList<XYData> fifo = new LinkedList<XYData>();

        public MySwingWorker() {

//            fifo.add(new XYData(0.0, 0.0));
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            //TODO sprubój pobierać też informację o czasie zamiast "sample"

            while (!isCancelled()) {
                XYData data = new XYData(getDataX(), getDataY());
                fifo.add(data);
                if (fifo.size() > 500) {
                    fifo.removeFirst();
                }

                XYData[] array = new XYData[fifo.size()];
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
        protected void process(List<XYData[]> chunks) {

            System.out.println("number of chunks: " + chunks.size());

            XYData[] mostRecentDataSet = chunks.get(chunks.size() - 1);
            double[] mostRecentDataSetX = new double[mostRecentDataSet.length];
            double[] mostRecentDataSetY = new double[mostRecentDataSet.length];
            for (int i = 0; i < mostRecentDataSet.length; i++) {
                mostRecentDataSetX[i] = mostRecentDataSet[i].x;
                mostRecentDataSetY[i] = mostRecentDataSet[i].y;


            }


            chart.updateXYSeries("randomWalk", mostRecentDataSetX, mostRecentDataSetY, null);
            sw.repaintChart();

            long start = System.currentTimeMillis();
            long duration = System.currentTimeMillis() - start;
            try {
                Thread.sleep(40 - duration); // 40 ms ==> 25fps
                // Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
            } catch (InterruptedException e) {
                System.out.println("InterruptedException occurred.");
            }
        }

        protected Double getDataY() {
            Double data;
            ResultSet r = executeQuery(s, sql);
            data = (double) 1.0;
            try {
                r.first();
                data = (double) r.getFloat("wart");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return data;
        }

        protected Double getDataX() {
            ResultSet r = executeQuery(s, sql);
            Double data = (double) 1.0;
            try {
                r.first();
                data = r.getDouble("sample");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return data;
        }
    }
}