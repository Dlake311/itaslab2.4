import java.sql.ResultSet;
import java.sql.SQLException;

/** Пункт меню 1: вывод всех таблиц из базы данных. */
public class VyvodTablic extends PunktMenu {

    public VyvodTablic(Baza b) {
        super(b);
    }

    @Override
    public String nazvanie() {
        return "Вывести все таблицы из базы данных PostgreSQL";
    }

    @Override
    public void vypolnit() throws SQLException {
        ResultSet rs = b.stat.executeQuery("SELECT table_name FROM information_schema.tables "
                + "WHERE table_schema = 'public' AND table_type = 'BASE TABLE' ORDER BY table_name");
        System.out.println("\n=== Список всех таблиц в базе данных ===");
        int count = 0;
        while (rs.next()) {
            count++;
            System.out.println(count + ". " + rs.getString("table_name"));
        }
        rs.close();
        if (count == 0) System.out.println("Таблиц не найдено");
        else System.out.println("Всего таблиц: " + count);
    }
}
