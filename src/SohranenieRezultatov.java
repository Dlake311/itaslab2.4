import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Пункт меню 4: для всех записей таблицы создаёт объекты Worker (через сеттеры),
 * формирует итог "имя, возраст, предполагаемая зарплата" (через геттеры),
 * сохраняет итог в БД и выводит все результаты в консоль.
 */
public class SohranenieRezultatov extends PunktMenu {

    public SohranenieRezultatov(Baza b) {
        super(b);
    }

    @Override
    public String nazvanie() {
        return "Сохранение всех результатов в PostgreSQL с последующим выводом в консоль";
    }

    @Override
    public void vypolnit() throws SQLException {
        if (!b.estTabl()) return;

        // 1. Читаем все записи и превращаем каждую в объект Worker
        List<Integer> ids = new ArrayList<>();
        List<Student> studenty = new ArrayList<>(); // Worker хранится как Student - полиморфизм
        ResultSet rs = b.stat.executeQuery("SELECT * FROM " + b.tablename + " ORDER BY id");
        while (rs.next()) {
            Worker w = new Worker();
            w.setName(rs.getString("name"));
            w.setAge(rs.getInt("age"));
            w.setSalary(rs.getDouble("salary"));
            ids.add(rs.getInt("id"));
            studenty.add(w);
        }
        rs.close();

        if (studenty.isEmpty()) {
            System.out.println("В таблице нет данных - сначала введите их (пункт 3)");
            return;
        }

        // 2. Сохраняем итог по каждому студенту
        PreparedStatement ps = b.conn.prepareStatement("UPDATE " + b.tablename + " SET rezultat = ? WHERE id = ?");
        for (int i = 0; i < studenty.size(); i++) {
            ps.setString(1, studenty.get(i).opisanie());
            ps.setInt(2, ids.get(i));
            ps.executeUpdate();
        }
        ps.close();
        System.out.println("Результаты сохранены в таблицу '" + b.tablename + "' (записей: " + studenty.size() + ")");

        // 3. Выводим результаты, прочитав их из БД
        System.out.println("\n--- Результаты (данные из БД) ---");
        pechatTablicy(b);
    }

    static void pechatTablicy(Baza b) throws SQLException {
        ResultSet r = b.stat.executeQuery("SELECT * FROM " + b.tablename + " ORDER BY id");
        System.out.printf("%-4s %-20s %-8s %-14s %s%n", "id", "Имя", "Возраст", "Зарплата", "Результат");
        while (r.next()) {
            String rez = r.getString("rezultat");
            System.out.printf("%-4d %-20s %-8d %-14s %s%n", r.getInt("id"), r.getString("name"),
                    r.getInt("age"), r.getBigDecimal("salary"), rez == null ? "(не сформирован)" : rez);
        }
        r.close();
    }
}
