import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField idTF;
    @FXML
    private TextField ciudadTF;
    @FXML
    private TextField codigoTF;
    @FXML
    private TextField distritoTF;
    @FXML
    private TextField poblacionTF;

    CiudadModel cm = new CiudadModel();

    ResultSet datos;

    String idTEMP = "";
    String ciudadTEMP = "";
    String codigoTEMP = "";
    String distritoTEMP = "";
    String poblacionTEMP = "";

    int pulsarIzquierda = 0;

    DBUtil db = new DBUtil();
    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            
            leerConfig();

            Statement ps = db.getConexion().createStatement(datos.TYPE_SCROLL_INSENSITIVE, datos.CONCUR_UPDATABLE);

            datos = ps.executeQuery("SELECT * FROM city");

            datos.next();

            idTF.setText(datos.getString(1));
            ciudadTF.setText(datos.getString(2));
            codigoTF.setText(datos.getString(3));
            distritoTF.setText(datos.getString(4));
            poblacionTF.setText(datos.getString(5));
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }

    @FXML
    private void atras(ActionEvent event) throws SQLException {
        pulsarIzquierda = 1;
        if (!datos.isFirst()) {
            datos.previous();
            idTF.setText(datos.getString(1));
            ciudadTF.setText(datos.getString(2));
            codigoTF.setText(datos.getString(3));
            distritoTF.setText(datos.getString(4));
            poblacionTF.setText(datos.getString(5));

            datos.previous();//le falta a luis y le dara fallo porq cuando vuelve atras no actualiza los temporales
            idTEMP = idTF.getText();
            ciudadTEMP = ciudadTF.getText();
            codigoTEMP = codigoTF.getText();
            distritoTEMP = distritoTF.getText();
            poblacionTEMP = poblacionTF.getText();
            datos.next();

        }

    }

    @FXML
    private void delante(ActionEvent event) throws SQLException {

        if (!ciudadTEMP.equals(ciudadTF.getText()) || !codigoTEMP.equals(codigoTF.getText()) || !distritoTEMP.equals(distritoTF.getText()) || !poblacionTEMP.equals(poblacionTF.getText())) {
            if (!ciudadTEMP.equals("") && !codigoTEMP.equals("") && !distritoTEMP.equals("") && !poblacionTEMP.equals("")) {//l falta esta condicion y si esta no se cumple tambien falla
                System.out.println("update");
                datos.updateString("Name", ciudadTF.getText());
                datos.updateString("CountryCode", codigoTF.getText());
                datos.updateString("District", distritoTF.getText());
                datos.updateString("Population", poblacionTF.getText());
                datos.updateRow();
                
                
            }

        }

        if (idTF.getText().equals("") && !idTEMP.equals("")) {
            System.out.println("delete");//el delete no lo tiene aqui y tiene que estar aqui y le falta la segunda condicion(lo tenia el el else del afterlast)

            datos.deleteRow();

        }

        datos.next();

        if (datos.isAfterLast()) {

            if (idTEMP.equals("") && !ciudadTF.getText().equals("") && pulsarIzquierda == 0) {
                System.out.println("insert");
                datos.moveToInsertRow();
                datos.updateString("Name", ciudadTF.getText());
                datos.updateString("CountryCode", codigoTF.getText());
                datos.updateString("District", distritoTF.getText());
                datos.updateString("Population", poblacionTF.getText());
                datos.insertRow();
                datos.next();

            }

            idTF.setText("");
            ciudadTF.setText("");
            codigoTF.setText("");
            distritoTF.setText("");
            poblacionTF.setText("");

        } else {

            idTF.setText(datos.getString(1));
            ciudadTF.setText(datos.getString(2));
            codigoTF.setText(datos.getString(3));
            distritoTF.setText(datos.getString(4));
            poblacionTF.setText(datos.getString(5));

        }
        idTEMP = idTF.getText();
        ciudadTEMP = ciudadTF.getText();
        codigoTEMP = codigoTF.getText();
        distritoTEMP = distritoTF.getText();
        poblacionTEMP = poblacionTF.getText();
        pulsarIzquierda = 0;
    }

    @FXML
    private void consulta(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXMLConsulta.fxml"));
            this.rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            //Logger.getLogger(FXMLMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void leerConfig() throws FileNotFoundException, IOException{
        File f = new File(DBUtil.getDBUtil().ruta);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        
        String linea="";
        ArrayList<String> configuracion = new ArrayList<>();
        
        while((linea=br.readLine())!=null){
            String[] palabras =linea.split(":");
            configuracion.add(palabras[1]);
        }
        
        DBUtil.getDBUtil().nombreUsuario=configuracion.get(0);
        DBUtil.getDBUtil().password=configuracion.get(1);
        DBUtil.getDBUtil().puerto=configuracion.get(2);
        DBUtil.getDBUtil().host=configuracion.get(3);
        
    }

    @FXML
    private void configuracion(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXMLConfig.fxml"));
            this.rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            //Logger.getLogger(FXMLMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
