package MD;

import MD.DB.DBWorker;
import MD.model.Student;
import MD.view.MainWindow;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static MD.DB.DBWorker.conn;

public class Main {
    public static void main(String[] args) throws SQLException {
        new MainWindow();
        DBWorker.initDB();
        DBWorker.addGroup("istb1");
//        DBWorker.addGroup("istb2");
//        DBWorker.addGroup("istb3");
//        DBWorker.addGroup("istb9");
        DBWorker.getGroups();

        //DBWorker.closeDB();

        List<Student> students = DBWorker.getAllStudents();
        System.out.println("all:");
        System.out.println(students);
        //System.out.println(DBWorker.getGroupId("АСУб-19"));
        students = DBWorker.getAllStudents();
        for (Student stud : students) {
            System.out.println(stud);
        }
    }
    public static List<Student> getAllStudents() throws SQLException {
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
