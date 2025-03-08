package repository;

import java.sql.*;

public class supplier {
    Connection conn;
    PreparedStatement pst;
    Statement statement;
    ResultSet rs;

    public ResultSet getData(Connection conn) {
        try {
            pst = conn.prepareStatement(constant.SELECT_ALL_SUPPLIERS);
            return rs = pst.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Boolean addNewSupplier(Connection conn, dto.supplier supplier){
        try {
            pst = conn.prepareStatement(constant.ADD_NEW_SUPPLIER);
            pst.setString(1, supplier.getName());
            pst.setString(2,supplier.getAddress());
            int state = pst.executeUpdate();

            return (state == 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean updateSupplier(Connection conn, dto.supplier supplier){
        try {
            pst = conn.prepareStatement(constant.UPDATE_SUPPLIER);
            pst.setString(1,supplier.getName());
            pst.setString(2,supplier.getAddress());
            pst.setInt(3,supplier.getId());

            int state = pst.executeUpdate();
            return (state==1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Connection conn, Integer id){
        try {
            pst = conn.prepareStatement(constant.DELETE_SUPPLIER);
            pst.setInt(1,id);
            int state = pst.executeUpdate();
            return (state==1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}



