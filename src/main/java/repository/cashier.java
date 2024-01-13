/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.sql.*;
import java.util.HashMap;

/**
 *
 * @author imanudd
 */
public class cashier {
    private final config configDB = new config();
    Connection conn;
    PreparedStatement pst;
    Statement statement;
    ResultSet rs;
    int state;

    dto.cashier cashierObj;

    public ResultSet get(Connection conn) {
        try {
            pst = conn.prepareStatement(constant.SELECT_ALL_CASHIERS);
            return  pst.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//    HashMap<String,String> cashier
    public boolean insert(Connection conn, dto.cashier cashier) {
        try {
            pst = conn.prepareStatement(constant.INSERT_NEW_CASHIER);
            pst.setString(1,cashier.name);
            pst.setString(2, cashier.phoneNumber);
            pst.setString(3, cashier.address);

            state = pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return (state==1);
    }

    public boolean delete(Connection conn, Integer id){
        try {
            pst = conn.prepareStatement(constant.DELETE_CASHIER);
            pst.setInt(1,id);
            state = pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (state==1);
    }

    public boolean update(Connection conn, dto.cashier cashier){
        try {
            pst = conn.prepareStatement(constant.UPDATE_CASHIER);
            pst.setString(1,cashier.name);
            pst.setString(2, cashier.phoneNumber);
            pst.setString(3, cashier.address);
            pst.setInt(4,cashier.id);

            state = pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (state==1);
    }
}
