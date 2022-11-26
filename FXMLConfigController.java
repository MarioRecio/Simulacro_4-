/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simulacro.pkg4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import jdk.jfr.events.FileWriteEvent;

/**
 * FXML Controller class
 *
 * @author nacho
 */
public class FXMLConfigController implements Initializable {

    @FXML
    private TextField usuario;
    @FXML
    private TextField puerto;
    @FXML
    private TextField host;
    @FXML
    private TextField contraseña;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField ruta;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        usuario.setText(DBUtil.getDBUtil().nombreUsuario);
        contraseña.setText(DBUtil.getDBUtil().password);
        host.setText(DBUtil.getDBUtil().host);
        puerto.setText(DBUtil.getDBUtil().puerto);
        ruta.setText(DBUtil.getDBUtil().ruta);
    }

    @FXML
    private void atras(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            this.rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            //Logger.getLogger(FXMLMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void guardar(ActionEvent event) throws IOException {
        DBUtil.getDBUtil().nombreUsuario = usuario.getText();
        DBUtil.getDBUtil().password = contraseña.getText();
        DBUtil.getDBUtil().puerto = puerto.getText();
        DBUtil.getDBUtil().host = host.getText();
        

        File f = new File(DBUtil.getDBUtil().ruta);
        FileWriter fwr = new FileWriter(f);

        String contenido = "";

        contenido += "Usuario:" + DBUtil.getDBUtil().nombreUsuario + "\n";
        contenido += "Contraseña:" + DBUtil.getDBUtil().password + "\n";
        contenido += "Puerto:" + DBUtil.getDBUtil().puerto + "\n";
        contenido += "Host:" + DBUtil.getDBUtil().host + "\n";

        fwr.write(contenido);
        fwr.close();

        DBUtil db = new DBUtil();

        try {
            db.getConexion();
        } catch (Exception e) {
            DBUtil.getDBUtil().error=e.toString();
        }finally{
            db.cerrarConexion();
        }
        
        if(!DBUtil.getDBUtil().error.isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("¡¡¡ERROR!!!");
            a.setContentText(DBUtil.getDBUtil().error);
            a.showAndWait();
            DBUtil.getDBUtil().error="";
        } else{
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("¡¡¡EXITO!!!");
            a.setContentText("Los datos introducidos son correctos");
            a.showAndWait();
        }
        
        DBUtil.getDBUtil().ruta=ruta.getText();
        
    }

}
