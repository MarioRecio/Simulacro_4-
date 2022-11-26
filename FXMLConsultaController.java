/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simulacro.pkg4;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Nacho
 */
public class FXMLConsultaController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField consulta;
    @FXML
    private TextArea resultado;

    DBUtil db = new DBUtil();
    DBUtil db1 = new DBUtil();
    ResultSet datos;
    ResultSet nombreColumnas;
    Statement ps;
    String contenidoInicial = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        try {

            ps = db.getConexion().createStatement(datos.TYPE_SCROLL_INSENSITIVE, datos.CONCUR_UPDATABLE);

            datos = ps.executeQuery("SELECT * FROM city");
            consulta.setText("SELECT * FROM city");

            while (datos.next()) {
                contenidoInicial += datos.getString(1) + "\n";
                contenidoInicial += "\t" + datos.getString(2) + "\n";
                contenidoInicial += "\t" + datos.getString(3) + "\n";
                contenidoInicial += "\t" + datos.getString(4) + "\n";
                contenidoInicial += "\t" + datos.getString(5) + "\n";
            }
            resultado.setText(contenidoInicial);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLConsultaController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            db.cerrarConexion();
        }

    }

    @FXML
    private void ejecutar(ActionEvent event) throws SQLException {
        ResultSet datos1 = null;

        Statement ps1;

        String sql = consulta.getText();
        String contenido = "";

        if (sql.toLowerCase().startsWith("select *")) {
            resultado.setText(contenidoInicial);
        } else if (sql.toLowerCase().startsWith("select")) {
            ps = db.getConexion().createStatement(datos.TYPE_SCROLL_INSENSITIVE, datos.CONCUR_UPDATABLE);
            nombreColumnas = ps.executeQuery("SHOW COLUMNS FROM city");

            ps1 = db1.getConexion().createStatement(datos1.TYPE_SCROLL_INSENSITIVE, datos1.CONCUR_UPDATABLE);
            datos1 = ps1.executeQuery(sql);

            int columna = datos1.getMetaData().getColumnCount();
            int columnaNombres = nombreColumnas.getMetaData().getColumnCount();

            ArrayList<String> columnas = new ArrayList<>();

            nombreColumnas.first();
            for (int i = 1; i <= columnaNombres; i++) {
                if (!nombreColumnas.isAfterLast()) {
                    columnas.add(nombreColumnas.getString(1));
                    nombreColumnas.next();
                }

            }

            String[] palabras = sql.split(" ");

            int cont = 1;
            nombreColumnas.first();
            datos1.first();
            for (int a = 0; a < palabras.length; a++) {
                String p = palabras[a];
                p = p.replaceAll(",", "");
                for (int z = 0; z < columnas.size(); z++) {
                    String c = columnas.get(z);
                    if (p.equalsIgnoreCase(c)) {
                        System.out.println(c);
                        contenido += c + "\n";
                        while (!datos1.isAfterLast()) {
                            contenido += "\t" + datos1.getString(cont) + "\n";

                            datos1.next();
                        }
                        datos1.first();
                        cont++;
                    }
                }
            }

            resultado.setText(contenido);
        } else{
            try{
            PreparedStatement stmt=db.getConexion().prepareStatement(sql);
            stmt.executeUpdate();
            } catch(SQLException e){
                resultado.setText(e.toString());
            }
        }

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

}
