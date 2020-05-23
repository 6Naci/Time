package JavaFx;

import Connection.ConnectionClass;
import Model.department;
import Model.employee;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {

    @FXML
    private TableView<ObservableList<String>> calendarView;

    @FXML
    private Label textLabel1;

    @FXML
    private TableView<department> tableDepartment;

    @FXML
    private TableColumn<department, Integer> IdColumnDep;

    @FXML
    private TableColumn<department, String> nameColumnDep;  // столбец для вывода названия отдела

    @FXML
    private TableView<employee> tableEmployee;

    @FXML
    private TableColumn<employee, String> nameColumn;

    @FXML
    private TableColumn<employee, Integer> IdColumn;

    @FXML
    private TableColumn<employee, Boolean> remotework;

    @FXML
    private TableColumn<employee, String> namePosColumn;

    @FXML
    private ComboBox<String> monthList;

    final String[] monthNames = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    public String nameDep = null; //название департамента

    @FXML
    private void initialize() {

        ObservableList<String> months = FXCollections.observableArrayList(monthNames);
        monthList.setItems(months);
        monthList.setValue(monthNames[0]);
        monthList.setVisibleRowCount(5); // Set the Limit of visible months to 5

        AtomicInteger kol = new AtomicInteger(0); // счетчик для помощи в заполнении индексов

        //monthList.setOnAction(event -> textLabel1.setText(monthList.getValue())); // получаем выбранный элемент

        ConnectionClass connectionClass = new ConnectionClass();

        try {
            Connection connection = connectionClass.getConnection();
            // определяем фабрику для столбца с привязкой к свойству name
            IdColumnDep.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumnDep.setCellValueFactory(new PropertyValueFactory<>("name"));

            connectionClass.getTableDepartment(tableDepartment);
            connection.close();

            TableView.TableViewSelectionModel<department> selectionModel = tableDepartment.getSelectionModel();

            selectionModel.selectedItemProperty().addListener((val, oldVal, newVal) -> {

                try {
                    Connection connection2 = connectionClass.getConnection();
                    if (newVal != null) {

                        calendarView.getColumns().clear();
                        calendarView.getItems().clear();

                        nameDep = newVal.getName();
                        connectionClass.getTableEmployee(tableEmployee, nameDep);

                        // устанавливаем тип и значение которое должно хранится в колонке
                        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fio"));
                        namePosColumn.setCellValueFactory(new PropertyValueFactory<>("namePos"));

                        TableColumn<employee, Void> indexCol = new TableColumn<>("№");
                        indexCol.setSortable(false);
                        indexCol.setResizable(false);

                        indexCol.setPrefWidth(20);

                        if (kol.get() == 0) {
                            kol.addAndGet(1);
                        } else {
                            tableEmployee.getColumns().remove(0);
                        }
                        // cell factory to display the index:
                        indexCol.setCellFactory(col -> {

                            // just a default table cell:
                            TableCell<employee, Void> cell = new TableCell<>();

                            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                                if (cell.isEmpty()) {
                                    return null;
                                } else {
                                    return Integer.toString(cell.getIndex() + 1);
                                }
                            }, cell.emptyProperty(), cell.indexProperty()));
                            return cell;
                        });
                        tableEmployee.getColumns().add(0, indexCol);
                    }
                    connection2.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            //
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onActionView(ActionEvent actionEvent) throws SQLException {

        if (nameDep == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Выберите департамент!");
            alert.showAndWait();

        } else {

            calendarView.getColumns().clear();
            calendarView.getItems().clear();

            Calendar calendar = new GregorianCalendar();

            int month = Arrays.asList(monthNames).indexOf(monthList.getValue());  // получение индекса по объекту для объекта calendar);
            calendar.set(2020, month, 1);
            int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  // количество дней в месяце

            for (int i = 0; i < days; i++) {
                final int finalI = i;
                TableColumn<ObservableList<String>, String> dataColumn = new TableColumn<>("" + calendar.get(Calendar.DAY_OF_MONTH));
                dataColumn.setSortable(false);
                dataColumn.setResizable(false);
                dataColumn.setCellValueFactory(
                        param -> new SimpleStringProperty(param.getValue().get(finalI))
                );

                dataColumn.prefWidthProperty().bind(calendarView.widthProperty().divide(calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1));
                calendarView.getColumns().add(dataColumn);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            String monthSql = month < 9 ? "0" + (month + 1) : "" + (month + 1); //месяц для запроса

            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();

            try {
                ArrayList<Integer> idEmpl = connectionClass.getTableIdEmployeeDep(nameDep);

                for (Integer integer : idEmpl) {
                    connectionClass.getRowCalendarEmployee(calendarView, integer, monthSql, days, nameDep);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            connection.close();
        }

    }
}
