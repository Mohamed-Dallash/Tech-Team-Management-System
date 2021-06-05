package Tasks_Panel.View_Tasks_Panel;

import Classes.Projects;
import Classes.Tasks;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;


public class Controller_Tasks implements Initializable {

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_Update;

    @FXML
    private Button btn_delete;

    @FXML
    private TextField txt_task_id;

    @FXML
    private TextField txt_employee_id;

    @FXML
    private TextField txt_task_name;

    @FXML
    private TextField txt_task_description;

    @FXML
    private DatePicker txt_deadline_date;
    
    @FXML
    private TableView<Tasks> table_tasks;

    @FXML
    private TableColumn<Tasks, Integer> col_task_id;

    @FXML
    private TableColumn<Tasks, Integer> col_employee_id;

    @FXML
    private TableColumn<Tasks, String> col_task_name;

    @FXML
    private TableColumn<Tasks, String> col_task_description;

    @FXML
    private TableColumn<Tasks, Date> col_deadline_date;

    @FXML
    private TableColumn<Tasks, String> col_task_status;
    
    @FXML
    private TextField filterField;

  
    

    ObservableList<Tasks> listM =  FXCollections.observableArrayList( new ArrayList<Tasks>() );
    ObservableList<Tasks> dataList;
    ObservableList<Tasks> temp;

    
    int index = -1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    
            
     public void Add_Tasks(){
         Tasks.Add_Task(txt_employee_id.getText(),txt_task_name.getText(),txt_task_description.getText(),txt_deadline_date.getValue().toString());
         Refresh_Tasks();
         Search_Task();
         resetValues();
    }
    @FXML
     void getSelected (MouseEvent event) {   
         index = table_tasks.getSelectionModel().getSelectedIndex();
         if(index <= -1) {
             return;
         }
         txt_task_id.setText(col_task_id.getCellData(index).toString());
         txt_employee_id.setText(col_employee_id.getCellData(index).toString());
         txt_task_name.setText(col_task_name.getCellData(index).toString());
         txt_task_description.setText(col_task_description.getCellData(index).toString());
         DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
         txt_deadline_date.setValue(LocalDate.from(fmt.parse(col_deadline_date.getCellData(index).toString())));
     }
     public void Update_Tasks(){
         Tasks.update_Task_Manager(txt_task_name.getText(),txt_task_description.getText(),txt_deadline_date.getValue().toString(),txt_task_id.getText());
         Refresh_Tasks();
         Search_Task();
         resetValues();
     }
     public void Delete_Tasks() {
         Tasks.delete_Task(txt_task_id.getText());
         resetValues();
         Refresh_Tasks();
     }
     
     public void Refresh_Tasks() {
        col_task_id.setCellValueFactory(new PropertyValueFactory<Tasks,Integer>("task_id"));
        col_employee_id.setCellValueFactory(new PropertyValueFactory<Tasks,Integer>("employee_id"));
        col_task_name.setCellValueFactory(new PropertyValueFactory<Tasks,String>("task_name"));
        col_task_description.setCellValueFactory(new PropertyValueFactory<Tasks,String>("task_description"));
        col_deadline_date.setCellValueFactory(new PropertyValueFactory<Tasks,Date>("deadline_date"));
        col_task_status.setCellValueFactory(new PropertyValueFactory<Tasks,String>("task_status"));
        
        listM = Tasks.getDataTasks();
        table_tasks.setItems(listM);
     }
     
     void Search_Task() {
        col_task_id.setCellValueFactory(new PropertyValueFactory<Tasks,Integer>("task_id"));
        col_employee_id.setCellValueFactory(new PropertyValueFactory<Tasks,Integer>("employee_id"));
        col_task_name.setCellValueFactory(new PropertyValueFactory<Tasks,String>("task_name"));
        col_task_description.setCellValueFactory(new PropertyValueFactory<Tasks,String>("task_description"));
        col_deadline_date.setCellValueFactory(new PropertyValueFactory<Tasks,Date>("deadline_date"));
        col_task_status.setCellValueFactory(new PropertyValueFactory<Tasks,String>("task_status"));
        
        dataList = listM;
        table_tasks.setItems(dataList);
        FilteredList <Tasks> filteredData = new FilteredList<> (dataList, b -> true);
        filterField.textProperty().addListener((observable,oldValue,newValue) -> { filteredData.setPredicate( task ->{
            if(newValue == null || newValue.isEmpty()) {
                return true;
            }
            
            String lowerCaseFilter = newValue.toLowerCase();
            
            if (task.getTask_name().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true;
            }  else if(task.getTask_description().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true;
            }
            else if (String.valueOf(task.getTask_status()).indexOf(lowerCaseFilter) != -1) {
                return true;
            }
            else if (String.valueOf(task.getDeadline_date()).indexOf(lowerCaseFilter) != -1) {
                return true;
            }
            
            else 
                return false;

        });
            
        });
        
        SortedList<Tasks> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_tasks.comparatorProperty());
        table_tasks.setItems(sortedData);
    }
     
    public void resetValues(){
        txt_task_id.setText("");
        txt_task_name.setText("");
        txt_employee_id.setText("");
        txt_task_description.setText("");
        txt_deadline_date.setValue(null);
    }


     @Override
    public void initialize(URL url, ResourceBundle rb) {
       resetValues();
       Refresh_Tasks();
       Search_Task();
    }

    public void DownloadTasks(ActionEvent actionEvent) throws IOException {
        listM= Tasks.getDataTasks();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Tasks Sheet");
        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Description");
        header.createCell(3).setCellValue("Employee ID");
        header.createCell(4).setCellValue("Delivery Date");
        header.createCell(5).setCellValue("Status");


        int idx=1;
        for (Tasks p: listM){
            XSSFRow row= sheet.createRow(idx);
            row.createCell(0).setCellValue(p.getTask_id());
            row.createCell(1).setCellValue(p.getTask_name());
            row.createCell(2).setCellValue(p.getTask_description());
            row.createCell(3).setCellValue(p.getEmployee_id());
            row.createCell(4).setCellValue(p.getDeadline_date());
            row.createCell(5).setCellValue(p.getTask_status());
            idx++;

        }
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop/Tasks Sheet.xlsx";

        try {
            FileOutputStream file = new FileOutputStream(desktopPath);
            wb.write(file);
            file.close();
            System.out.println("done");
            Alert notFound = new Alert(Alert.AlertType.INFORMATION);
            notFound.setContentText("The file is Successfully saved in your Desktop");
            notFound.setHeaderText("Success");
            notFound.showAndWait();
        } catch (Exception e){
            Alert notFound = new Alert(Alert.AlertType.ERROR);
            notFound.setContentText("The file is open by another program. Please try again");
            notFound.setHeaderText("Error");
            notFound.showAndWait();
        }
    }
}
