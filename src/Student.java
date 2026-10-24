/**
 * Абстрактный класс Student: закрытые поля name (имя) и age (возраст)
 * и открытые методы для их записи и чтения.
 */
public abstract class Student {
    private String name;
    private int age;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    // Итоговое описание - каждый наследник формирует его по-своему
    public abstract String opisanie();
}
