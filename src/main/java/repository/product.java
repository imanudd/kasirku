package repository;

import java.sql.*;

public class product {
    Connection conn;
    PreparedStatement pst;
    Statement statement;
    ResultSet rs;
    int state;
    public ResultSet getItemsByCategory(Connection conn, int id)throws Exception{
        pst = conn.prepareStatement(constant.SELECT_PRODUCT_BY_CATEGORY);
        pst.setInt(1,id);

        return rs = pst.executeQuery();
    }

    public ResultSet getProductData(Connection conn, int id)throws Exception{
        pst = conn.prepareStatement(constant.SELECT_PRODUCT_DATA);
        pst.setInt(1,id);

        return rs = pst.executeQuery();
    }

    public ResultSet getItemsByID(Connection conn, int id)throws Exception{
        pst = conn.prepareStatement(constant.SELECT_PRODUCT_BY_ID);
        pst.setInt(1,id);

        return rs = pst.executeQuery();
    }

    public boolean updateSellingStock(Connection conn,Integer stockSold, Integer productID) throws Exception{
        pst = conn.prepareStatement(constant.UPDATE_SELLING_STOCK);
        pst.setInt(1,stockSold);
        pst.setInt(2,productID);

        int status = pst.executeUpdate();

        return (status==1);
    }

    public Boolean updateReturnStock(Connection conn, Integer productID, Integer stockReturn){
        try {
            pst = conn.prepareStatement(constant.UPDATE_RETURN_STOCK);
            pst.setInt(1,stockReturn);
            pst.setInt(2,productID);

            int status = pst.executeUpdate();
            return (status==1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean addNewProduct(Connection conn, dto.product product){
        try {
            pst = conn.prepareStatement(constant.INSERT_PRODUCT);
            pst.setInt(1,product.getId());
            pst.setInt(2, product.getCategoryID());
            pst.setString(3,product.getProductName());
            pst.setInt(4,product.getTotal());
            pst.setInt(5,product.getPrice());
            pst.setInt(6,product.getSupplierID());

            state = pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (state==1);
    }

    public Boolean updateProductInfo(Connection conn, dto.product product){
        try {
            pst = conn.prepareStatement(constant.UPDATE_PRODUCT);
            pst.setString(1,product.getProductName());
            pst.setInt(2,product.getTotal());
            pst.setInt(3,product.getPrice());
            pst.setInt(4,product.getId());

            state = pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (state==1);
    }

    public boolean delete(Connection conn, Integer id){
        System.out.println("id : "+id);
        try {
            pst = conn.prepareStatement(constant.DELETE_PRODUCT);
            pst.setInt(1,id);
            state = pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (state==1);
    }
}
