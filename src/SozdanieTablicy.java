import java.sql.SQLException;

/** Пункт меню 2: создание таблицы в базе данных (или выбор существующей). */
public class SozdanieTablicy extends PunktMenu {

    public SozdanieTablicy(Baza b) {
        super(b);
    }

    @Override
    public String nazvanie() {
        return "Создать таблицу в базе данных PostgreSQL";
    }

    @Override
    public void vypolnit() throws SQLException {
        String name;
        while (true) {
            System.out.print("Введите название таблицы (латиница, цифры, _): ");
            name = b.scan.nextLine().trim().toLowerCase();
            if (name.matches("[a-z_][a-z0-9_]*")) break;
            System.out.println("Некорректное название таблицы");
        }
        b.stat.executeUpdate("CREATE TABLE IF NOT EXISTS " + name + " (" + b.struktura + ")");
        b.tablename = name;
        System.out.println("Таблица '" + name + "' создана (или уже существовала) и выбрана для работы");
    }
}
