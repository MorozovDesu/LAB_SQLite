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
        List<Testee> testees = DBWorker.getAllTestees();
        System.out.println("all:");
        System.out.println(testees);
        testees = DBWorker.getAllTestees();
        for (Testee tst : testees) {
            System.out.println(tst);
        }
    }
    public static List<Testee> getAllTestee() throws SQLException {
        return DBWorker.getAllTestees();
    }
}
