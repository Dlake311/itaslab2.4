/**
 * Класс-наследник Worker: получает имя и возраст от Student
 * и добавляет своё закрытое поле salary (предполагаемая зарплата).
 */
public class Worker extends Student {
    private double salary;

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String opisanie() {
        return "Студент " + getName() + ", возраст " + getAge()
                + ", предполагаемая зарплата " + String.format("%.2f", getSalary()) + " руб.";
    }
}
