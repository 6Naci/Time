package Connection;

import Model.department;
import Model.employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import java.sql.*;
import java.util.ArrayList;

public class ConnectionClass {

    //Database credentials (Параметры доступа к БД)
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/time";
    static final String USER = "postgres";
    static final String PASS = "admin";
    static final String DB_Driver = "org.postgresql.Driver";

    public static String info;

    public Connection connection;

    public Connection getConnection(){

        try {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            info = "Соединение с СУБД выполнено.";//System.out.println("Соединение с СУБД выполнено.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); //обработка ошибка Class.forName
            info = "JDBC драйвер для СУБД не найден!";//System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e){
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            info = "Ошибка SQL !";//System.out.println("Ошибка SQL !");
        }

        return connection ;
    }

    public void getTableDepartment(TableView<department> tableDepartment) throws SQLException{

        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM department";
        ResultSet resultSet = statement.executeQuery(sql);
        ArrayList<department> data =  new ArrayList<>();

        while (resultSet.next()) {
            department dep = new department();
            dep.setId(resultSet.getInt("iddepartment"));
            dep.setName(resultSet.getString("namedepartment"));
            data.add(dep);
        }
        ObservableList<department> departments = FXCollections.observableArrayList(data);

        tableDepartment.setItems(departments);

    }

    public ArrayList<Integer> getTableIdEmployeeDep(String nameDep) throws SQLException{
        ArrayList<Integer> idEmployee = new ArrayList<>();

        Statement statement = connection.createStatement();
        String sql ="SELECT e.idemployee\n" +
                "from employee e\n" +
                "         INNER JOIN department d on e.iddepartment = d.iddepartment\n" +
                "where namedepartment = '" + nameDep + "'";

        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            idEmployee.add(resultSet.getInt("idemployee"));
        }

        return  idEmployee;
    }

    public void getTableEmployee (TableView<employee> tableEmployee, String nameDep) throws SQLException{

        Statement statement = connection.createStatement();
        String sql = "SELECT e.idemployee, e.fio, d.namedepartment, p.nameposition, e.\"Аddress\", e.remotework\n" +
                "from employee e Inner Join position p on e.idposition = p.idposition\n" +
                "                INNER JOIN department d on e.iddepartment = d.iddepartment\n WHERE d.namedepartment = '" + nameDep + "'";
        ResultSet resultSet = statement.executeQuery(sql);

        ArrayList<employee> data = new ArrayList<>();

        while (resultSet.next()) {
            employee dep = new employee();
            dep.setId(resultSet.getInt("idemployee"));
            dep.setFio(resultSet.getString("fio"));
            dep.setNamePos(resultSet.getString("nameposition"));
            //dep.setNameDep(resultSet.getString("namedepartment"));
            //dep.setAddress(resultSet.getString('"' +"Аddress" + '"'));
            //dep.setRemotework(resultSet.getBoolean("remotework"));
            data.add(dep);
        }

        ObservableList<employee> employees = FXCollections.observableArrayList(data);

        tableEmployee.setItems(employees);

    }

    public void getRowCalendarEmployee(TableView<ObservableList<String>> calendarView, Integer integer, String monthSql, Integer days, String nameDep) throws SQLException{

        Statement statement = connection.createStatement();
        String sql = "SELECT cNew.idcalendar, e.fio, cNew.dateofrecording, cNew.abbreviation\n" +
                "from employee e\n" +
                "         INNER JOIN department d on e.iddepartment = d.iddepartment\n" +
                "         INNER JOIN (Select *\n" +
                "                     from calendar c\n" +
                "                              INNER JOIN mark m on c.idmark = m.idmark) cNew on e.idemployee = cNew.idemployee\n" +
                "where namedepartment = '" + nameDep + "'\n" +
                "  and e.idemployee = " + integer + "and dateofrecording between '2020-" + monthSql + "-01' and '2020-" + monthSql + "-" + days + "'\n" +
                "ORDER BY dateofrecording;";

        ResultSet resultSet = statement.executeQuery(sql);

        ArrayList<String> dataMark = new ArrayList<>();

        while (resultSet.next()) {
            dataMark.add(resultSet.getString("abbreviation"));
        }

        if (dataMark.size() < days) {
            for (int k = dataMark.size() - 1; k < days; k++) {
                dataMark.add("");
            }
        }

        ObservableList<String> calendarObs = FXCollections.observableArrayList();
        calendarObs.addAll(dataMark);
        calendarView.getItems().add(calendarObs);
    }
}
