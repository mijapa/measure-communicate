package com.patyk.baza;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class CreateDatabase {
//    public static void main(String[] args) {
//        Connection con = getConnection("localhost", 3306);
//        Statement st = createStatement(con);
//// utworzenie bazy
//        if (executeUpdate(st, "create Database nowaBaza;") != -1)
//            System.out.println("Baza utworzona");
//        else
//            System.out.println("Baza nieutworzona!");
//
//        if (executeUpdate(st, "USE nowaBaza;") != -1)
//            System.out.println("Baza wybrana");
//        else
//            System.out.println("Baza niewybrana!");
//
//        if (executeUpdate(st, "CREATE TABLE ksiazki ( id INT NOT NULL, tytul VARCHAR(50) NOT NULL, autor INT NOT NULL, PRIMARY KEY (id) );") != -1)
//            System.out.println("Tabela utworzona");
//        else
//            System.out.println("Tabela nie utworzona!");
//    }


    /**
     * Metoda służy do połączenia z MySQL bez wybierania konkretnej bazy
     *
     * @return referencja do uchwytu bazy danych
     */
    public static Connection getConnection(String adres, int port) {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "");

        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + adres + ":" + port + "/",
                    connectionProps);
        } catch (SQLException e) {
            System.out.println("Brak komunikacji zwrotnej z serwera!!!");
            e.printStackTrace();
        }
        System.out.println("Connected to database");
        return conn;
    }

    public static int executeUpdate(Statement s, String sql) {
        try {
            return s.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
