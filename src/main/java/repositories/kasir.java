/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author imanudd
 */
public class kasir {
   public void getData(Connection conn,String tableName) throws Exception {
       Statement statement;
       ResultSet rs;
       
       try{
           String query = String.format("Select * From %s", tableName);
           statement = conn.createStatement();
           rs=statement.executeQuery(query);
           while(rs.next()){
               System.out.println(rs.getString(1));
               System.out.println(rs.getString(2));
               System.out.println(rs.getString(3));
               System.out.println(rs.getString(4));
           }
        }catch(SQLException e){
            System.out.println(e);
        }
   }
}
