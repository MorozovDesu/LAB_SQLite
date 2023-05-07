package MD;

import MD.model.Testee;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TesteesTableModel extends AbstractTableModel {
    private List<Testee> data;

    public TesteesTableModel(List<Testee> testees){
        data = testees;
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
        Testee st = data.get(rowIndex);
        switch (columnIndex){
            case 0: return st.getNameTest();
            case 1: return st.getNameTestee();
            case 2: return st.getResultTest();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0: return "Тест";
            case 1: return "Результаты";
            case 2: return "Испытуемый";
        }
        return "";
    }

    public Testee getTestees(int selectedRow) {
        return data.get(selectedRow);
    }

    public void setTestees(List<Testee> allTestees) {
        this.data = allTestees;
        fireTableDataChanged(); // Сообщаем таблице, что данные изменились
    }
}
