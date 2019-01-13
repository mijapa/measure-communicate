package com.patyk.baza;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class CreateDatabase {

    /**
     * Metoda służy do połączenia z MySQL bez wybierania konkretnej bazy
     *
     * @return referencja do uchwytu bazy danych
     */
    public static Connection getConnection(DanePolaczeniaBaza danePolaczeniaBaza) {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", danePolaczeniaBaza.getUser());
        connectionProps.put("password", danePolaczeniaBaza.getPassword());

        try {
            conn = DriverManager.getConnection("jdbc:mysql://" +
                            danePolaczeniaBaza.getAdresBazy() +
                            ":" + danePolaczeniaBaza.getPortBazy() + "/",
                    connectionProps);
        } catch (SQLException e) {
            System.out.println("Brak komunikacji zwrotnej z serwera!!!");
            e.printStackTrace();
        }
        System.out.println("Connected to database");
        return conn;
    }

    /**
     * Metoda wykonuje polecenie sql
     *
     * @param s
     * @param sql
     * @return
     */
    public static int executeUpdate(Statement s, String sql) {
        try {
            return s.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
