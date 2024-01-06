/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author imanudd
 */
public class config {
    public static Connection initDB(){
        Connection conn=null;
        try{
            Class.forName("org.postgresql.Driver");
            conn=DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+"kasirku","root","root");
            System.out.println("connected");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
