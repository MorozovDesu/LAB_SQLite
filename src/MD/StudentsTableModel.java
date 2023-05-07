package MD;

import MD.model.Student;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class StudentsTableModel extends AbstractTableModel {
    private List<Student> data;

    public StudentsTableModel(List<Student> students){
        data = students;

    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student st = data.get(rowIndex);
        switch (columnIndex){
            case 0: return st.getLastname();
            case 1: return st.getName();
            case 2: return st.getGroup();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0: return "Фамилия";
            case 1: return "Имя";
            case 2: return "Группа";
        }
        return "";
    }

    public Student getStudent(int selectedRow) {
        return data.get(selectedRow);
    }

    public void setStudents(List<Student> allStudents) {
        this.data = allStudents;
        fireTableDataChanged(); // Сообщаем таблице, что данные изменились
    }
}