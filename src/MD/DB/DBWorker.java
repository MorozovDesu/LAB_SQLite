package MD.DB;

import MD.model.Testee;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBWorker {
    public static final String PATH_TO_DB_FILE = "my.db";
    public static final String URL = "jdbc:sqlite:" + PATH_TO_DB_FILE;
    public static Connection conn;
    public static void initDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(URL);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println(meta.getDriverName());
                createDB();
            }
        } catch (SQLException ex) {
            System.out.println("Ошибка подключения к БД: " + ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void closeDB()  {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("БД закрылась");
    }
    public static void addGroup(String title) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO groups(`title`)"+"VALUES(?);");
            statement.setObject(1, title);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void addTest(Testee testee) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO students(`name_test`,`results`,`group_id`) VALUES(?,?,?)")) {
            // Получаем id группы по ее названию (title)
            String groupName = testee.getNameTestee();
            PreparedStatement groupStatement = conn.prepareStatement("SELECT id FROM groups WHERE title = ?");
            groupStatement.setString(1, groupName);
            ResultSet groupResult = groupStatement.executeQuery();
            int groupId = -1;
            if (groupResult.next()) {
                groupId = groupResult.getInt("id");
            }
            groupResult.close();
            groupStatement.close();

            statement.setObject(1, testee.getNameTest());
            statement.setObject(2, testee.getResultTest());
            statement.setObject(3, groupId);
            statement.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createDB()  {
        try {
            Statement statement = conn.createStatement();
            statement.execute("CREATE TABLE if not exists 'groups' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'title' text);");
            System.out.println("Таблица создана или уже существует.");
            statement.execute("CREATE TABLE if not exists 'students' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name_test' text,'results' text,'group_id' INTEGER NOT NULL, FOREIGN KEY (group_id) REFERENCES groups (id));");
            System.out.println("Таблица создана или уже существует.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getGroups() {
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM groups");
            while (resultSet.next()){
                System.out.println(resultSet.getString(2));
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    public static List<Testee> getAllTestees() throws SQLException {
        Statement statement = conn.createStatement();
        List<Testee> list = new ArrayList<Testee>();
        ResultSet resultSet = statement.executeQuery("SELECT students.id, students.name_test, students.results, students.group_id, groups.title FROM students JOIN groups ON groups.id = students.group_id");
        while (resultSet.next()) {
            list.add(new Testee(resultSet.getInt("id"),resultSet.getString("name_test"), resultSet.getString("results"), getGroupName(resultSet.getInt("group_id"))));
        }
        resultSet.close();
        statement.close();
        return list;
    }
    public static String getGroupName(int grId) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT title FROM groups WHERE groups.id ="+grId);
        String grName = "";
        grName = resultSet.getString(1);
        resultSet.close();
        statement.close();
        return grName;
    }
    public static void deleteTestee(Testee testee) throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute("DELETE FROM students WHERE students.id ="+ testee.getId());
        System.out.println("deleted!");
        statement.close();
    }
    public static List<Pair<String, List<String>>> getAllTesteeGroups() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT groups.title, students.name_test FROM groups LEFT JOIN students ON groups.id = students.group_id ORDER BY groups.id");
        List<Pair<String, List<String>>> groups = new ArrayList<>();
        String currentGroupTitle = null;
        List<String> currentGroupTestees = new ArrayList<>();
        while (resultSet.next()) {
            String groupTitle = resultSet.getString(1);
            String testeeName = resultSet.getString(2);
            if (!groupTitle.equals(currentGroupTitle)) {
                // Start a new group
                if (currentGroupTitle != null) {
                    groups.add(new Pair<>(currentGroupTitle, currentGroupTestees));
                }
                currentGroupTitle = groupTitle;
                currentGroupTestees = new ArrayList<>();
            }
            if (testeeName != null) {
                currentGroupTestees.add(testeeName);
            }
        }
        if (currentGroupTitle != null) {
            groups.add(new Pair<>(currentGroupTitle, currentGroupTestees));
        }
        resultSet.close();
        statement.close();
        return groups;
    }
}