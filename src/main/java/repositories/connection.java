/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author imanudd
 */
public class connection {
    public Connection connect_to_db(String dbName, String user, String pass){
        Connection conn=null;
            try{
                Class.forName("org.postgresql.Driver");
                    conn=DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbName,user,pass);
                    if (conn != null){
                        System.out.println("connected");
                    }else{
                        System.out.println("failed");
                    }
            } catch (Exception e){
                System.out.println(e);
            }
        return conn;
    }
}
