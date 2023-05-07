package MD;

import MD.DB.DBWorker;
import MD.model.Testee;
import MD.view.MainWindow;

import java.sql.*;
import java.util.List;
//7. Предметная область: тестирование. Таблицы: испытуемые, тесты, результаты тестов.

public class Main {
    public static void main(String[] args) throws SQLException {
        new MainWindow();
        DBWorker.addGroup("Морозов");



        List<Testee> testees = DBWorker.getAllStudents();
        System.out.println("all:");
        System.out.println(testees);
        testees = DBWorker.getAllStudents();
        for (Testee tst : testees) {
            System.out.println(tst);
        }
    }
    public static List<Testee> getAllTestee() throws SQLException {
        return DBWorker.getAllStudents();
    }
//    public static List<Student> getAllStudents() {
//        // Statement используется для того, чтобы выполнить sql-запрос
//        try (Statement statement = conn.createStatement()) {
//            // В данный список будем загружать наши продукты, полученные из БД
//            List<Student> list = new ArrayList<Student>();
//            // В resultSet будет храниться результат нашего запроса,
//            // который выполняется командой statement.executeQuery()
//            ResultSet resultSet = statement.executeQuery("SELECT lastname, name, group_id FROM students");
//            // Проходимся по нашему resultSet и заносим данные в products
//            while (resultSet.next()) {
//                list.add(new Student(resultSet.getString("lastname"),resultSet.getString("name"),resultSet.getString("group_id")));
//            }
//            // Возвращаем наш список
//            return list;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // Если произошла ошибка - возвращаем пустую коллекцию
//            return Collections.emptyList();
//        }
//    }
}
