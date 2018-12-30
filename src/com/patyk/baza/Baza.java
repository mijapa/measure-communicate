package com.patyk.baza;

import java.sql.*;

public class Baza {
    public static void main(String[] args) {
        System.out.println("MAIN");
        if (ladujSterownik());
        else
            System.exit(1);

        java.sql.Connection connection = connectToDatabase("localhost:3306", "bank2018", "root", "");

        // WYKONYWANIE OPERACJI NA BAZIE DANYCH
        System.out.println("Pobieranie danych z bazy:");
        String sql = "select * from klient";
        Statement s = createStatement(connection);
        ResultSet r = executeQuery(s, sql);
        printDataFromQuery(r);
        closeConnection(connection, s);
    }

    /**
     * Metoda ładuje sterownik jdbc
     *
     * @return true/false
     */
    public static boolean ladujSterownik() {
        // LADOWANIE STEROWNIKA
        System.out.print("Sprawdzanie sterownika:");
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.print(" sterownik OK");
            return true;
        } catch (Exception e) {
            System.out.println("Blad przy ladowaniu sterownika bazy!");
            e.printStackTrace();
            return false;
        }
    }

    public static Connection connectToDatabase(String adress, String dataBaseName, String userName, String password) {
        System.out.print("\nLaczenie z baza danych:");
        String baza = "jdbc:mysql://" + adress + "/" + dataBaseName;
        // objasnienie opisu bazy:
        // jdbc: - mechanizm laczenia z baza (moze byc inny, np. odbc)
        // mysql: - rodzaj bazy
        // adress - adres serwera z baza (moze byc tez w nazwy)
        // dataBaseName - nazwa bazy (poniewaz na serwerze moze byc kilka roznych
        // baz...)
        java.sql.Connection connection = null;
        try {
            connection = DriverManager.getConnection(baza, userName, password);
        } catch (SQLException e) {
            System.out.println("Blad przy łączeniu do bazy!");
            e.printStackTrace();
            System.exit(1);
        }
        if (connection != null)
            System.out.print(" polaczenie OK\n");
        else
            System.exit(1);
        return connection;
    }

    /**
     * Zamykanie połączenia z bazą danych
     *
     * @param connection
     *            - połączenie z bazą
     * @param s
     *            - obiekt przesyłający zapytanie do bazy
     */
    public static void closeConnection(Connection connection, Statement s) {
        System.out.print("\nZamykanie polaczenia z bazaą:");
        try {
            s.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Bląd przy zamykaniu polączenia " + e.toString());
            System.exit(4);
        }
        System.out.print(" zamknięcie OK");
    }

    /**
     * tworzenie obiektu Statement przesyłającego zapytania do bazy connection
     *
     * @param connection
     *            - połączenie z bazą
     * @return obiekt Statement przesyłający zapytania do bazy
     */
    public static Statement createStatement(Connection connection) {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ;
        return null;
    }

    /**
     * Wykonanie kwerendy i przesłanie wyników do obiektu ResultSet
     *
     * @param s
     *            - Statement
     * @param sql
     *            - zapytanie
     * @return wynik
     */
    public static ResultSet executeQuery(Statement s, String sql) {
        try {
            return s.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Wyświetla dane uzyskane zapytaniem select
     *
     * @param r
     *            - wynik zapytania
     */
    public static void printDataFromQuery(ResultSet r) {
        ResultSetMetaData rsmd;
        try {
            rsmd = r.getMetaData();

            int numcols = rsmd.getColumnCount(); // pobieranie liczby kolumn

            // wyswietlanie nazw kolumn:
            for (int i = 1; i <= numcols; i++) {
                System.out.print("\t" + rsmd.getColumnLabel(i) + "\t|");
            }
            System.out.print("\n____________________________________________________________________________\n");
            /**
             * r.next() - przejście do kolejnego rekordu (wiersza) otrzymanych wyników
             */
            // wyswietlanie kolejnych rekordow:
            while (r.next()) {
                for (int i = 1; i <= numcols; i++) {
                    Object obj = r.getObject(i);
                    if (obj != null)
                        System.out.print("\t" + obj.toString() + "\t|");
                    else
                        System.out.print("\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Bląd odczytu z bazy! " + e.toString());
            System.exit(3);
        }
    }
}
