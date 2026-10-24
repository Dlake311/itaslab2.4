import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Пункт меню 3: ввод всех полей с клавиатуры, сохранение в БД и вывод в консоль. */
public class VvodPoley extends PunktMenu {

    public VvodPoley(Baza b) {
        super(b);
    }

    @Override
    public String nazvanie() {
        return "Ввод с клавиатуры значений ВСЕХ полей, сохранить их в PostgreSQL с последующим выводом в консоль";
    }

    @Override
    public void vypolnit() throws SQLException {
        if (!b.estTabl()) return;

        Worker w = new Worker();
        while (true) {
            System.out.print("Введите имя студента: ");
            String name = b.scan.nextLine().trim();
            if (!name.isEmpty()) {
                w.setName(name);
                break;
            }
            System.out.println("Имя не может быть пустым");
        }
        while (true) {
            int age = b.chitatCeloe("Введите возраст: ");
            if (age >= 14 && age <= 100) {
                w.setAge(age);
                break;
            }
            System.out.println("Возраст должен быть от 14 до 100");
        }
        while (true) {
            System.out.print("Введите предполагаемую зарплату: ");
            String s = b.scan.nextLine().trim().replace(',', '.').replace(" ", "");
            try {
                double sal = Double.parseDouble(s);
                if (sal >= 0 && !Double.isInfinite(sal)) {
                    w.setSalary(sal);
                    break;
                }
                System.out.println("Зарплата не может быть отрицательной");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число");
            }
        }

        PreparedStatement ps = b.conn.prepareStatement("INSERT INTO " + b.tablename
                + " (name, age, salary) VALUES (?,?,?) RETURNING id");
        ps.setString(1, w.getName());
        ps.setInt(2, w.getAge());
        ps.setDouble(3, w.getSalary());
        ResultSet rs = ps.executeQuery();
        rs.next();
        int id = rs.getInt(1);
        rs.close();
        ps.close();
        System.out.println("Данные сохранены в таблицу '" + b.tablename + "' (id = " + id + ")");

        // Читаем сохранённую запись обратно из БД и выводим
        PreparedStatement sel = b.conn.prepareStatement("SELECT * FROM " + b.tablename + " WHERE id = ?");
        sel.setInt(1, id);
        ResultSet r = sel.executeQuery();
        if (r.next()) {
            System.out.println("\n--- Запись из БД ---");
            System.out.println("id:        " + r.getInt("id"));
            System.out.println("Имя:       " + r.getString("name"));
            System.out.println("Возраст:   " + r.getInt("age"));
            System.out.println("Зарплата:  " + r.getBigDecimal("salary"));
        }
        r.close();
        sel.close();
    }
}
