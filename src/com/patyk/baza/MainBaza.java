package com.patyk.baza;

import java.sql.Connection;
import java.sql.Statement;

import static com.patyk.baza.Baza.closeConnection;
import static com.patyk.baza.Baza.createStatement;
import static com.patyk.baza.CreateDatabase.executeUpdate;
import static com.patyk.baza.CreateDatabase.getConnection;

public class MainBaza {
    public static final String DATABASE_NAME = "IntDom";
    public static final String CZUJNIKI = "czujniki";

    /**
     * Metoda próbuje wybrać bazę danych, jeśli taka nie istniej tworzy ją
     *
     * @param danePolaczeniaBaza
     * @return
     */
    public static int tryToConnectOrCreateDatabase(DanePolaczeniaBaza danePolaczeniaBaza) {
        Connection con = getConnection(danePolaczeniaBaza);
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
                    else {
                        System.out.println("Baza niewybrana!");
                        System.exit(1);
                    }
                } else {
                    System.out.println("Baza nieutworzona!");
                    System.exit(1);
                }
                if (executeUpdate(st, "CREATE TABLE " + CZUJNIKI + " (id INT NOT NULL, wart FLOAT NOT NULL,  milisDate INT8 NOT NULL);") != -1)
                    System.out.println("Tabela utworzona");
                else {
                    System.out.println("Tabela nie utworzona!");
                    System.exit(1);
                }
                if (executeUpdate(st, "ALTER TABLE `" + CZUJNIKI + "` ADD PRIMARY KEY( `id`, `milisDate`);") != -1)
                    System.out.println("Klucz główny dodany");
                else {
                    System.out.println("Klucz główny nie dodany!");
                    System.exit(1);
                }
                if (executeUpdate(st, "ALTER TABLE `IntDom`.`" + CZUJNIKI + "` ADD INDEX `milisDate_ind` (`milisDate`);") != -1)
                    System.out.println("Indeks milisDate_ind dodany");
                else {
                    System.out.println("Indeks nie dodany!");
                    return -1;
                }
            }
        }
        return 0;
    }

    /**
     * Metoda łączy się z bazą, wstawia nową wartość do tabeli
     * @param id
     * @param wartosc
     * @param milisDate
     * @param danePolaczeniaBaza
     * @return
     */
    public static int databaseInsert(Integer id, Float wartosc, Long milisDate, DanePolaczeniaBaza danePolaczeniaBaza) {
        Connection con = getConnection(danePolaczeniaBaza);
        Statement st = createStatement(con);
        if (st != null) {
            if (executeUpdate(st, "USE " + DATABASE_NAME + ";") != -1)
                System.out.println("Baza wybrana");
            else {
                System.out.println("Baza niewybrana!");
                System.exit(1);
            }
            if (executeUpdate(st, "INSERT INTO `" + CZUJNIKI + "` (`id`, `wart`, `milisDate`) VALUES ('" + id + "', '" + wartosc + "', '" + milisDate + "')") != -1) {
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
