package MD.view;

import MD.DB.DBWorker;
import MD.model.Testee;
import MD.TesteesTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.sql.SQLException;

public class MainWindow extends JFrame {
    private JTable testingTable;
    private JButton addTestee;
    private JButton deleteTestee;
    private TesteesTableModel model;

    public MainWindow() {
        super("Тестирование");
        init();
    }
    private void init() {
        DBWorker.initDB();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DBWorker.closeDB();
            }
        });

        testingTable = new JTable();
        try {
            model = new TesteesTableModel(DBWorker.getAllStudents());
            testingTable.setModel(model);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        Container contentPane = this.getContentPane();
        contentPane.add(new JScrollPane(testingTable), BorderLayout.CENTER);

        addTestee = new JButton("Добавить тест и результаты");
        addTestee.addActionListener(e -> {
            try {
                String nameTestee = JOptionPane.showInputDialog(null, "Введите название теста");
                String nameTest = JOptionPane.showInputDialog(null, "Введите количество баллов");
                String resultTest = JOptionPane.showInputDialog(null, "Введите испытуемого");
                Testee testee = new Testee(nameTestee, nameTest, resultTest);
                DBWorker.addStudent(testee);
                List<Testee> allTestees = DBWorker.getAllStudents();
                model.setTestees(allTestees); // Обновляем модель таблицы
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        deleteTestee = new JButton("Удалить");
        deleteTestee.addActionListener(e -> {
            int selectedRow = testingTable.getSelectedRow();
            if(selectedRow == -1) {
                // Если строка не выбрана, выводим сообщение
                JOptionPane.showMessageDialog(this, "Выберите строчку для удаления");
                return;
            }

            try {
                DBWorker.deleteStudent(model.getTestees(selectedRow));
                model.setTestees(DBWorker.getAllStudents());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        JPanel butPane = new JPanel();
        butPane.add(addTestee);
        butPane.add(deleteTestee);

        contentPane.add(butPane, BorderLayout.SOUTH);

        this.setLocationByPlatform(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.pack();
        this.setVisible(true);
    }
}