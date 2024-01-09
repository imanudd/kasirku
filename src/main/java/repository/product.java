package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class product {
    Connection conn;
    PreparedStatement pst;
    Statement statement;
    ResultSet rs;
    public ResultSet getItemsByCategory(Connection conn, int id)throws Exception{
        pst = conn.prepareStatement(constant.SELECT_PRODUCT_BY_CATEGORY);
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

    public Boolean updateReturnStock(Connection conn, Integer productID, Integer stockReturn)throws Exception{
        pst = conn.prepareStatement(constant.UPDATE_RETURN_STOCK);
        pst.setInt(1,stockReturn);
        pst.setInt(2,productID);

        int status = pst.executeUpdate();

        return (status==1);
    }
}
