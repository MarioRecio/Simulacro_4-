/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulacro.pkg4;

import java.sql.*;

/**
 *
 * @author Nacho
 */
public class DBUtil {

    public Connection conn;
    //public String cadenaConexion = "jdbc:mysql://"+DBUtil.getDBUtil().host+":"+DBUtil.getDBUtil().puerto+"/seccion4";
    public String nombreUsuario = "";
    public String password = "";
    public String puerto = "";
    public String host = "";
    public String error = "";//se puede utilizar para enviar los errores a traves de las clases
    public String ruta="config.txt";
    public boolean unaVez=true;

    public Connection getConexion() {

        try {
            String cadenaConexion = "jdbc:mysql://"+DBUtil.getDBUtil().host+":"+DBUtil.getDBUtil().puerto+"/world2";
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            this.conn = DriverManager.getConnection(cadenaConexion, DBUtil.getDBUtil().nombreUsuario, DBUtil.getDBUtil().password);
            return this.conn;
        } catch (SQLException e) {
            //e.printStackTrace();
            String prueba = e.toString();
            DBUtil.getDBUtil().error = prueba;
            return null;
        }
    }

    public void cerrarConexion() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DBUtil db;

    public DBUtil() {
    }

    public synchronized static DBUtil getDBUtil() {
        if (db == null) {
            db = new DBUtil();
        }
        return db;
    }

}