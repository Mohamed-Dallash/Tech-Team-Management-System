
package Classes;

import MySQL.MySQL_Connector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;


public class Tasks {
private int task_id,employee_id;
private String task_name,task_description;
private Date deadline_date;
private String task_status;

    public Tasks(int task_id, int employee_id, String task_name, String task_description, Date deadline_date, String task_status) {
        this.task_id = task_id;
        this.employee_id = employee_id;
        this.task_name = task_name;
        this.task_description = task_description;
        this.deadline_date = deadline_date;
        this.task_status = task_status;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public void setDeadline_date(Date deadline_date) {
        this.deadline_date = deadline_date;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public int getTask_id() {
        return task_id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public String getTask_description() {
        return task_description;
    }

    public Date getDeadline_date() {
        return deadline_date;
    }

    public String getTask_status() {
        return task_status;
    }



    private static ObservableList<String> Tasks_Status = FXCollections.observableArrayList("Not Done","In Progress", "Done");

    public static ObservableList<String> getTasks_Status() {
        return Tasks_Status;
    }

    public static ObservableList<Tasks> getDataTasks(){
        Connection conn = MySQL_Connector.ConnectDB();
        ObservableList<Tasks> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Tasks");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                list.add(new Tasks(Integer.parseInt(rs.getString("task_id")),Integer.parseInt(rs.getString("employee_id")),rs.getString("task_name"),rs.getString("task_description"),rs.getDate("deadline_date"),rs.getString("task_status")));

            }
        } catch (Exception e) {
            System.out.println("couldn't get tasks data");
        }
        return list;
    }



    public static void Add_Task(String employee_id, String task_name, String task_description, String deadline_date){
        Connection conn = MySQL_Connector.ConnectDB();
        String sql = "INSERT INTO Tasks (employee_id,task_name,task_description,deadline_date) VALUES (?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1,employee_id);
            pst.setString(2,task_name);
            pst.setString(3,task_description);
            pst.setString(4, deadline_date);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Tasks Added Successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public static void delete_Task(String id) {
        Connection conn = MySQL_Connector.ConnectDB();
        String sql = "DELETE FROM Tasks WHERE task_id = '"+id+"'";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.execute();
            JOptionPane.showMessageDialog(null, "Task deleted successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public static void update_Task_Manager(String new_name, String new_description, String new_deadline, String id){
        try {
            Connection conn = MySQL_Connector.ConnectDB();
            String sql = "UPDATE Tasks SET task_name = '"+new_name+"', task_description = '"+new_description+"', deadline_date = '"+new_deadline+"' WHERE task_id = '"+id+"' ";

            PreparedStatement pst = conn.prepareCall(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Task Updated Successfully");
        }
        catch( Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void update_Task_status(String new_status){
        try {
            Connection conn = MySQL_Connector.ConnectDB();
            String sql = "UPDATE Tasks SET task_status = '"+new_status+"' WHERE task_id = '"+this.getTask_id()+"' ";

            PreparedStatement pst = conn.prepareCall(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Task Updated Successfully");
        }
        catch( Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }

}
