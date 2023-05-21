package MD.view;

import MD.DB.DBWorker;
import MD.model.Testee;
import MD.TesteesTableModel;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.sql.SQLException;

public class MainWindow extends JFrame {
    private JTable testingTable;
    private JButton addTest;
    private JButton deleteTest;
    private JButton addTestee;
    JButton showTestees = new JButton("Показать группу");
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
            model = new TesteesTableModel(DBWorker.getAllTestees());
            testingTable.setModel(model);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        Container contentPane = this.getContentPane();
        contentPane.add(new JScrollPane(testingTable), BorderLayout.CENTER);

        addTest = new JButton("Добавить тест и результаты");
        addTest.addActionListener(e -> {
            try {
                String nameTestee = JOptionPane.showInputDialog(null, "Введите название теста");
                String resultTest = JOptionPane.showInputDialog(null, "Введите количество баллов");
                String nameTest = JOptionPane.showInputDialog(null, "Введите испытуемого");
                Testee testee = new Testee(nameTestee, nameTest, resultTest);
                DBWorker.addTest(testee);
                List<Testee> allTestees = DBWorker.getAllTestees();
                model.setTestees(allTestees); // Обновляем модель таблицы
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        deleteTest = new JButton("Удалить");
        deleteTest.addActionListener(e -> {
            int selectedRow = testingTable.getSelectedRow();
            if(selectedRow == -1) {
                // Если строка не выбрана, выводим сообщение
                JOptionPane.showMessageDialog(this, "Выберите строчку для удаления");
                return;
            }
            try {
                DBWorker.deleteTestee(model.getTestees(selectedRow));
                model.setTestees(DBWorker.getAllTestees());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        addTestee = new JButton("Добавить испытуемого");
        addTestee.addActionListener(e -> {
            String groupTitle = JOptionPane.showInputDialog(null, "Введите Фамилию студента");
            if (groupTitle != null && !groupTitle.isEmpty()) {
                DBWorker.addGroup(groupTitle);
                List<Testee> allTestees;
                try {
                    allTestees = DBWorker.getAllTestees();
                    model.setTestees(allTestees); // Обновляем модель таблицы
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        showTestees.addActionListener(e -> {
            try {
                List<Pair<String, List<String>>> allGroups = DBWorker.getAllTesteeGroups();
                StringBuilder sb = new StringBuilder();
                for (Pair<String, List<String>> group : allGroups) {
                    sb.append(group.getKey()).append(": ");
                    if (group.getValue().isEmpty()) {
                        sb.append("нет тестов");
                    } else {
                        sb.append(String.join(", ", group.getValue()));
                    }
                    sb.append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString(), "Испытуемые", JOptionPane.PLAIN_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        JPanel butPane = new JPanel();
        butPane.add(showTestees);
        butPane.add(addTest);
        butPane.add(deleteTest);
        butPane.add(addTestee);

        contentPane.add(butPane, BorderLayout.SOUTH);

        this.setLocationByPlatform(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.pack();
        this.setVisible(true);
    }
}