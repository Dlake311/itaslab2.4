import java.awt.Desktop;
import java.io.File;
import java.sql.*;
import java.util.Scanner;

/**
 * Работа с базой данных PostgreSQL и общие вспомогательные методы
 * (ввод с клавиатуры, путь для Excel-файла и т.п.).
 * Один объект этого класса используют все пункты меню.
 */
public class Baza {
    // Параметры подключения к PostgreSQL (при необходимости поменяйте под себя)
    static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASSWORD = "postgres";

    final Scanner scan = new Scanner(System.in);
    final String struktura; // описание столбцов таблицы для CREATE TABLE
    Connection conn;
    Statement stat;
    String tablename;       // таблица, с которой сейчас работаем

    public Baza(String struktura) {
        this.struktura = struktura;
    }

    boolean podkl() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stat = conn.createStatement();
            System.out.println("Подключение к PostgreSQL прошло успешно");
            return true;
        } catch (SQLException e) {
            System.out.println("Не удалось подключиться к базе данных: " + e.getMessage());
            System.out.println("Проверьте, что PostgreSQL запущен и логин/пароль в классе Baza верные");
            return false;
        }
    }

    void zakr() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException ignored) {
        }
    }

    // Таблица должна быть создана/выбрана (пункт 2) до остальных действий
    boolean estTabl() {
        if (tablename == null) {
            System.out.println("Сначала создайте или выберите таблицу (пункт 2)");
            return false;
        }
        return true;
    }

    // ---------- Ввод с клавиатуры ----------

    int chitatVybor() {
        if (!scan.hasNextLine()) return 0; // ввод закончился - выходим
        String s = scan.nextLine().trim();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    int chitatCeloe(String text) {
        while (true) {
            System.out.print(text);
            String s = scan.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число");
            }
        }
    }

    // ---------- Excel ----------

    // Рабочий стол, если он есть, иначе папка проекта
    File papkaDlyaFaila() {
        File desktop = new File(System.getProperty("user.home"), "Desktop");
        if (desktop.isDirectory()) return desktop;
        File oneDrive = new File(System.getProperty("user.home"), "OneDrive" + File.separator + "Desktop");
        if (oneDrive.isDirectory()) return oneDrive;
        return new File(System.getProperty("user.dir"));
    }

    // Открываем файл программой, связанной с .csv (обычно Excel)
    void otkrytFail(File file) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(file);
                System.out.println("Файл открыт в Excel");
                return;
            }
        } catch (Exception ignored) {
        }
        System.out.println("Откройте файл в Excel вручную");
    }

    // Текст может содержать ';' и кавычки - экранируем по правилам CSV
    String csv(String s) {
        if (s == null) return "";
        if (s.contains(";") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}
