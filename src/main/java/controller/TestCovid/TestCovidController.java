package controller.TestCovid;

import entity.KhaiBao;
import entity.NhanKhau;
import entity.TestCovid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import repository.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;


public class TestCovidController implements Initializable {
    @FXML
    private TableView<TestCovid>table;
    @FXML
    private TableColumn<TestCovid,Integer>  idColumn;
    @FXML
    private TableColumn<TestCovid,String>  hoTenColumn;
    @FXML
    private TableColumn<TestCovid, Date>  thoiDiemTestColumn;
    @FXML
    private TableColumn<TestCovid,String>  hinhThucColumn;
    @FXML
    private TableColumn<TestCovid,String>  CMNDColumn;
    @FXML
    private TableColumn<TestCovid,String>  ketQuaColumn;
    @FXML
    private Button themTest;
    @FXML
    private ComboBox<String> truongTraCuuF;
    @FXML
    private TextField duLieuF;
    @FXML
    private ImageView confirmF;
    @FXML
    ObservableList<TestCovid>  testList = FXCollections.observableArrayList();
    ObservableList<TestCovid>  searchList = FXCollections.observableArrayList();
    private int id_NK;
    static NhanKhauRepository NhanKhauRepo = new NhanKhauRepositoryImpl();
    static TestCovidRepository TestRepo = new TestCovidRepositoryImpl();

    public void add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/TestCovid/themTest.fxml"));
        Parent them_test = loader.load();
        Scene scene = new Scene(them_test);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.setTitle("Th??m khai b??o");
        stage.setScene(scene);
        stage.show();
    }

    public void delete(ActionEvent event){
        TestCovid test = table.getSelectionModel().getSelectedItem();
        if (test == null) {
            Alert m = new Alert(Alert.AlertType.INFORMATION);
            m.setTitle("Th??ng b??o!");
            m.setHeaderText("Kh??ng c?? b???n ghi n??o ???????c ch???n.");
            m.setContentText("Vui l??ng ch???n l???i.");
            m.show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xo??");
        alert.setHeaderText("B???n c?? mu???n xo?? kh??ng?");
        alert.setContentText("C?? ho???c Kh??ng:");

        ButtonType buttonYes = new ButtonType("Yes",ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("No",ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(buttonYes,buttonNo);

        Optional<ButtonType> result = alert.showAndWait();

        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        alert1.setTitle("Th??ng b??o!");

        if(result.get().getButtonData() == ButtonBar.ButtonData.YES){
            int idTest = test.getId();
            TestRepo.delete_test(idTest);
            testList.remove(test);
            alert1.setContentText("Th??nh c??ng");
            alert1.show();
        }
        else if(result.get().getButtonData() == ButtonBar.ButtonData.NO){
            alert1.setContentText("Th???t b???i");
            alert1.show();
        }
    }

    @SneakyThrows
    @FXML
    private void findF(MouseEvent event) {
        searchList.clear();
        String search_text = duLieuF.getText().trim().toLowerCase();
        String truongTraCuu = truongTraCuuF.getValue();
        try {
            if (truongTraCuu.equals("H??? t??n")) {
                for (TestCovid a : testList) {
                    if ((a.getHoTen().toLowerCase()).contains(search_text)) {
                        TestCovid clone_test = new TestCovid();
                        clone_test.copy_test(a);
                        searchList.add(clone_test);
                    }
                }
                table.setItems(searchList);
            } else if (truongTraCuu.equals("Ch???ng minh nh??n d??n")) {
                for (TestCovid a : testList) {
                    if ((a.getCMND().toLowerCase()).contains(search_text)) {
                        TestCovid clone_test = new TestCovid();
                        clone_test.copy_test(a);
                        searchList.add(clone_test);
                    }
                }
                table.setItems(searchList);
            } else if (truongTraCuu.equals("Th???i ??i???m Test")) {
                for (TestCovid a : testList) {
                    if ((String.valueOf(a.getThoiDiemTest())).contains(search_text)) {
                        TestCovid clone_test = new TestCovid();
                        clone_test.copy_test(a);
                        searchList.add(clone_test);
                    }
                }
                table.setItems(searchList);
            } else if (truongTraCuu.equals("H??nh th???c Test")) {
                for (TestCovid a : testList) {
                    if ((a.getHinhThucTest().toLowerCase()).contains(search_text)) {
                        TestCovid clone_test = new TestCovid();
                        clone_test.copy_test(a);
                        searchList.add(clone_test);
                    }
                }
                table.setItems(searchList);
            } else if (truongTraCuu.equals("K???t qu???")) {
                for (TestCovid a : testList) {
                    if ((a.getKetQua().toLowerCase()).contains(search_text)) {
                        TestCovid clone_test = new TestCovid();
                        clone_test.copy_test(a);
                        searchList.add(clone_test);
                    }
                }
                table.setItems(searchList);
            }
        }catch(NullPointerException ex){
            Alert m = new Alert(Alert.AlertType.INFORMATION);
            m.setTitle("Th??ng b??o!");
            m.setHeaderText("Ch??a ch???n tr?????ng t??m ki???m!");
            m.setContentText("M???i ch???n l???i!");
            m.show();
            return;
        }
    }

    ObservableList<String> listTruongTraCuu = FXCollections.observableArrayList("H??? t??n","Ch???ng minh nh??n d??n","H??nh th???c Test","Th???i ??i???m Test","K???t qu???");
    @FXML
    private void loadData() {
        testList.clear();
        testList.addAll(TestRepo.loadDataTestController());
        table.setItems(testList);
        truongTraCuuF.valueProperty().set(null);
        duLieuF.setText("");
        truongTraCuuF.setItems(listTruongTraCuu);
    }

    private void initCol(){
        idColumn.setCellValueFactory(new PropertyValueFactory<TestCovid,Integer>("id"));
        hoTenColumn.setCellValueFactory(new PropertyValueFactory<TestCovid,String>("hoTen"));
        thoiDiemTestColumn.setCellValueFactory(new PropertyValueFactory<TestCovid, Date>("thoiDiemTest"));
        hinhThucColumn.setCellValueFactory(new PropertyValueFactory<TestCovid,String>("hinhThucTest"));
        CMNDColumn.setCellValueFactory(new PropertyValueFactory<TestCovid,String>("CMND"));
        ketQuaColumn.setCellValueFactory(new PropertyValueFactory<TestCovid,String>("ketQua"));
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

}


