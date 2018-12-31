package com.patyk.baza;

import java.sql.Connection;
import java.sql.Statement;

import static com.patyk.baza.Baza.closeConnection;
import static com.patyk.baza.Baza.createStatement;
import static com.patyk.baza.CreateDatabase.executeUpdate;
import static com.patyk.baza.CreateDatabase.getConnection;

public class MainBaza {
    public static final String DATABASE_NAME = "IntDom";

    public static void main(String[] args) {
        databaseInsert(0, (float) 20.0);

    }

    public static int databaseInsert(Integer id, Float wartosc) {
        Connection con = getConnection("localhost", 3306);
        Statement st = createStatement(con);
        if (st != null) {
            if (executeUpdate(st, "USE " + DATABASE_NAME + ";") != -1)
                System.out.println("Baza wybrana");
            else {
                System.out.println("Baza niewybrana!");
                System.out.println("Tworzę nową Bazę");

                if (executeUpdate(st, "create Database " + DATABASE_NAME + " ;") != -1) {
                    System.out.println("Baza utworzona");
                    if (executeUpdate(st, "USE " + DATABASE_NAME + ";") != -1)
                        System.out.println("Baza wybrana");
                    else
                        System.out.println("Baza niewybrana!");
                } else {
                    System.out.println("Baza nieutworzona!");
                    System.exit(1);
                }
                if (executeUpdate(st, "CREATE TABLE czujniki ( id INT NOT NULL, wart FLOAT NOT NULL, time TIME NOT NULL, date DATE NOT NULL, sample INT NOT NULL AUTO_INCREMENT KEY);") != -1)
                    System.out.println("Tabela utworzona");
                else
                    System.out.println("Tabela nie utworzona!");
            }
            if (executeUpdate(st, "INSERT INTO `czujniki` (`id`, `wart`, `date`, `time`) VALUES ('" + id + "', '" + wartosc + "', CURRENT_DATE(), CURRENT_TIME())") != -1) {
                System.out.println("Nowa wartość wstawiona\n");
                closeConnection(con, st);
                return 0;
            } else {
                System.out.println("Nowa wartośc niewstawiona\n!");
                closeConnection(con, st);
                return -1;
            }
        }
        return -1;
    }
}
