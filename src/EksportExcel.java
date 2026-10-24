import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Пункт меню 5: выгрузка всех данных из БД в Excel (CSV) и вывод их в консоль. */
public class EksportExcel extends PunktMenu {

    public EksportExcel(Baza b) {
        super(b);
    }

    @Override
    public String nazvanie() {
        return "Сохранить результаты из PostgreSQL в Excel и вывести их в консоль";
    }

    @Override
    public void vypolnit() throws SQLException {
        if (!b.estTabl()) return;
        File file = new File(b.papkaDlyaFaila(), b.tablename + ".csv");
        ResultSet rs = b.stat.executeQuery("SELECT * FROM " + b.tablename + " ORDER BY id");
        int count = 0;

        // CSV с BOM и разделителем ';' - Excel открывает его с правильной кодировкой
        try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            w.write('﻿');
            w.write("id;Имя;Возраст;Зарплата;Результат\n");
            while (rs.next()) {
                count++;
                String sal = rs.getBigDecimal("salary").toPlainString().replace('.', ',');
                String rez = rs.getString("rezultat");
                w.write(rs.getInt("id") + ";" + b.csv(rs.getString("name")) + ";" + rs.getInt("age") + ";"
                        + sal + ";" + b.csv(rez == null ? "" : rez) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла: " + e.getMessage());
            return;
        } finally {
            rs.close();
        }

        System.out.println("\n=== Содержимое таблицы '" + b.tablename + "' ===");
        SohranenieRezultatov.pechatTablicy(b);
        if (count == 0) System.out.println("Таблица пуста");
        System.out.println("\nДанные сохранены в Excel-файл: " + file.getAbsolutePath() + " (записей: " + count + ")");
        b.otkrytFail(file);
    }
}
