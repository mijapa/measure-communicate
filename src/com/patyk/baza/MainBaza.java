package com.patyk.baza;

import java.sql.Connection;
import java.sql.Statement;

import static com.patyk.baza.Baza.createStatement;
import static com.patyk.baza.CreateDatabase.executeUpdate;
import static com.patyk.baza.CreateDatabase.getConnection;

public class MainBaza {
    public static final String DATABASE_NAME = "nowaBaza";

    public static void main(String[] args) {
        database();

    }

    public static void database() {
        Connection con = getConnection("localhost", 3306);
        Statement st = createStatement(con);

        if (executeUpdate(st, "USE " + DATABASE_NAME + ";") != -1)
            System.out.println("Baza wybrana");
        else {
            System.out.println("Baza niewybrana!");
            System.out.println("Tworzę nową Bazę");

            if (executeUpdate(st, "create Database " + DATABASE_NAME + " ;") != -1)
                System.out.println("Baza utworzona");
            else {
                System.out.println("Baza nieutworzona!");
                System.exit(1);
            }
        }
    }
}
