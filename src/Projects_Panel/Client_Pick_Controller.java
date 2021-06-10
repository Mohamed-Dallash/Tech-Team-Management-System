package Projects_Panel;

import Classes.Clients;
import Classes.Popup_Window;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import static Projects_Panel.Controller_Projects.client;


public class Client_Pick_Controller implements Initializable {

    @FXML
    private Button btn_select;

    @FXML
    private TableView<Clients> table_clients;

    @FXML
    private TableColumn<Clients, Integer> table_id;

    @FXML
    private TableColumn<Clients, String> table_name;

    @FXML
    private TableColumn<Clients, String> table_email;

    @FXML
    private TableColumn<Clients, String> table_num;

    @FXML
    private TableColumn<Clients, String> table_add;

    ObservableList<Clients> listP;
    int index = -1;

    @FXML
    void select_Employee() {
        index = table_clients.getSelectionModel().getSelectedIndex();
        if(index <= -1) {
            Popup_Window.error("Please Select a Client");
            return;
        }
        int id = Integer.parseInt(table_id.getCellData(index).toString()) ;
        String name = table_name.getCellData(index);
        String number = table_num.getCellData(index);
        String email = table_email.getCellData(index);
        String address = table_add.getCellData(index);
        client = new Clients(id,name,number,email,address);
        closeStage();
    }

    public void closeStage(){
        Stage stage;
        stage = (Stage) btn_select.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        table_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        table_email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        table_num.setCellValueFactory(new PropertyValueFactory<>("Num"));
        table_add.setCellValueFactory(new PropertyValueFactory<>("Address"));
        listP = Clients.getClients();
        table_clients.setItems(listP);
    }
}
