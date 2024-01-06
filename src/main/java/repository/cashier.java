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

    public ResultSet get(Connection conn) throws SQLException {
        pst = conn.prepareStatement(constant.SELECT_ALL_CASHIERS);
        rs = pst.executeQuery();

        return rs;
    }

    public boolean insert(Connection conn, HashMap<String,String> cashier)throws SQLException {
        pst = conn.prepareStatement(constant.INSERT_NEW_CASHIER);
        pst.setString(1,cashier.get("name"));
        pst.setString(2, cashier.get("phoneNumber"));
        pst.setString(3, cashier.get("address"));

        int status = pst.executeUpdate();

        return (status==1);
    }

    public boolean delete(Connection conn, Integer id)throws SQLException{
        pst = conn.prepareStatement(constant.DELETE_CASHIER);
        pst.setInt(1,id);

        int status = pst.executeUpdate();

        return (status==1);
    }

    public boolean update(Connection conn, HashMap<String,String> cashier, int id) throws SQLException{
        pst = conn.prepareStatement(constant.UPDATE_CASHIER);
        pst.setString(1,cashier.get("name"));
        pst.setString(2, cashier.get("phoneNumber"));
        pst.setString(3, cashier.get("address"));
        pst.setInt(4,id);

        int status = pst.executeUpdate();

        return (status==1);
    }
}
