import java.sql.SQLException;

/**
 * Базовый (абстрактный) класс пункта меню.
 * Каждый пункт меню - отдельный класс-наследник, который задаёт
 * своё название и своё действие.
 */
public abstract class PunktMenu {
    protected final Baza b;

    protected PunktMenu(Baza b) {
        this.b = b;
    }

    // Текст пункта в меню
    public abstract String nazvanie();

    // Что делает пункт
    public abstract void vypolnit() throws SQLException;
}
