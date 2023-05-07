package MD.DB;

import MD.model.Student;

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
            statement.setObject(1, title);//data
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void addStudent(Student student) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO students(`lastname`,`name`,`group_id`) VALUES(?,?,?)")) {
            statement.setObject(1, student.getLastname());
            statement.setObject(2, student.getName());
            statement.setObject(3, student.getGroup());
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
            statement.execute("CREATE TABLE if not exists 'students' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'lastname' text,'name' text,'email' text, 'group_id' INTEGER NOT NULL, FOREIGN KEY (group_id) REFERENCES groups (id));");
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


    public static List<Student> getAllStudents() throws SQLException {
        Statement statement = conn.createStatement();
        List<Student> list = new ArrayList<Student>();
        ResultSet resultSet = statement.executeQuery("SELECT students.id, students.lastname, students.name, students.group_id, groups.title FROM students JOIN groups ON groups.id = students.group_id");
        while (resultSet.next()) {
            list.add(new Student(resultSet.getInt("id"),resultSet.getString("lastname"), resultSet.getString("name"), getGroupName(resultSet.getInt("group_id"))));
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

    public static void deleteStudent(Student student) throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute("DELETE FROM students WHERE students.id ="+student.getId());
        System.out.println("deleted!");
        statement.close();
    }

    public static int getGroupId(String grName) throws SQLException {

        Statement statement = null;
        ResultSet resultSet = null;
        int groupId = -1;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT id FROM groups WHERE title ='" + grName + "'");
            if (resultSet.next()) {
                groupId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting group ID from database: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                // log the exception or print error message
            }
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                // log the exception or print error message
            }
        }
        return groupId;
    }

}
