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
    private JButton addTest;
    private JButton deleteTest;
    private JButton addTestee;
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
                String nameTest = JOptionPane.showInputDialog(null, "Введите количество баллов");
                String resultTest = JOptionPane.showInputDialog(null, "Введите испытуемого");
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
            String groupName = JOptionPane.showInputDialog(null, "Введите название группы");
            if (groupName != null && !groupName.isEmpty()) {
                try {
                    int groupId = DBWorker.getGroupId(groupName);
                    if (groupId == -1) {
                        // Если группа не найдена, создаем новую
                        DBWorker.addGroup(groupName);
                        groupId = DBWorker.getGroupId(groupName);
                    }
                    Testee testee = new Testee(groupId);
                    DBWorker.addGroup(String.valueOf(testee));
                    model.setTestees(DBWorker.getAllTestees()); // Обновляем модель таблицы
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JPanel butPane = new JPanel();
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