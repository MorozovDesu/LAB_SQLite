package MD.view;

import MD.DB.DBWorker;
import MD.model.Student;
import MD.StudentsTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class MainWindow extends JFrame {
    private JTable studTable;
    private JButton addStudent;
    private JButton deleteStudent;
    private StudentsTableModel model;

    public MainWindow() {
        super("Студенты");
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
        studTable = new JTable();
        try {
            model = new StudentsTableModel(DBWorker.getAllStudents());
            studTable.setModel(model);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        Container contentPane = this.getContentPane();
        contentPane.add(new JScrollPane(studTable), BorderLayout.CENTER);
        addStudent = new JButton("Добавить студента");

        deleteStudent = new JButton("Удалить студента");
        deleteStudent.addActionListener(e -> {
            try {
                DBWorker.deleteStudent(model.getStudent(studTable.getSelectedRow()));
                studTable.setModel(new StudentsTableModel(DBWorker.getAllStudents()));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        JPanel butPane = new JPanel();
        butPane.add(addStudent);
        butPane.add(deleteStudent);

        contentPane.add(butPane, BorderLayout.SOUTH);

        this.setLocationByPlatform(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.pack();
        this.setVisible(true);
    }
}