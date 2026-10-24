import java.sql.SQLException;

/**
 * Лабораторная работа 2, часть 4 (сложный вариант).
 * ООП: абстрактный класс Student (имя, возраст) и класс-наследник Worker (зарплата).
 * Каждый пункт меню - отдельный класс-наследник PunktMenu.
 */
public class Main {
    public static void main(String[] args) {
        Baza b = new Baza("id SERIAL PRIMARY KEY, name VARCHAR(100), age INT, salary NUMERIC(14,2), rezultat TEXT");
        if (!b.podkl()) return;

        PunktMenu[] menu = {
                new VyvodTablic(b),
                new SozdanieTablicy(b),
                new VvodPoley(b),
                new SohranenieRezultatov(b),
                new EksportExcel(b)
        };

        while (true) {
            System.out.println();
            System.out.println("========== МЕНЮ ==========");
            for (int i = 0; i < menu.length; i++) {
                System.out.println((i + 1) + ". " + menu[i].nazvanie());
            }
            System.out.println("0. Выход");
            System.out.print("Ваш выбор: ");

            int vib = b.chitatVybor();
            if (vib == 0) {
                b.zakr();
                System.out.println("Работа завершена");
                return;
            }
            if (vib < 1 || vib > menu.length) {
                System.out.println("Нет такого пункта меню, введите число от 0 до " + menu.length);
                continue;
            }
            try {
                menu[vib - 1].vypolnit();
            } catch (SQLException e) {
                System.out.println("Ошибка работы с базой данных: " + e.getMessage());
            }
        }
    }
}
