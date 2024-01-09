package repository;

import java.sql.*;
import java.util.HashMap;
import java.util.Date;

public class transaction {

    Connection conn;
    PreparedStatement pst;
    Statement statement;
    ResultSet rs;

    public boolean insertDetailTransaction(Connection conn,HashMap<String,Integer> detailTransaction) throws Exception{
        pst = conn.prepareStatement(constant.INSERT_DETAIL_TRANSACTION);
        pst.setInt(1, detailTransaction.get("detailtransactionID"));
        pst.setInt(2, detailTransaction.get("transactionID"));
        pst.setInt(3,detailTransaction.get("productID"));
        pst.setInt(4, detailTransaction.get("qty"));
        pst.setInt(5, detailTransaction.get("totalPrice"));

        int status = pst.executeUpdate();

        return (status==1);
    }

    public Boolean updateGrandTotal(Connection conn, Integer trxID, Integer grandTotal) throws Exception{
        pst = conn.prepareStatement(constant.UPDATE_GRAND_TOTAL);
        pst.setInt(1,grandTotal);
        pst.setInt(2,trxID);

        int status = pst.executeUpdate();

        return (status == 1);
    }

    public boolean upsertTransaction(Connection conn,HashMap<String,Integer> transaction) throws Exception{
        long millis=System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(millis);

        pst = conn.prepareStatement(constant.UPSERT_TRANSACTION);
        pst.setInt(1, transaction.get("transactionID"));
        pst.setInt(2,transaction.get("grandTotal"));
        pst.setDate(3, currentDate);
        pst.setInt(4, transaction.get("cashierID"));
        pst.setInt(5,transaction.get("grandTotal"));


        int status = pst.executeUpdate();

        return (status==1);
    }

    public ResultSet getDetailTransaction(Connection conn, int transactionID) throws SQLException {
        pst = conn.prepareStatement(constant.GET_DETAIL_TRANSACTION);
        pst.setInt(1,transactionID);
        rs = pst.executeQuery();

        return rs;
    }

    public Boolean deleteTransaction(Connection conn,Integer id)throws Exception{
        pst = conn.prepareStatement(constant.DELETE_DETAIL_TRANSACTION);
        pst.setInt(1,id);

        int status = pst.executeUpdate();

        return (status==1);
    }

    public ResultSet getTransaction(Connection conn, Integer trxID)throws Exception{
        pst = conn.prepareStatement(constant.SELECT_TRANSACTION_BY_ID);
        pst.setInt(1,trxID);
        rs = pst.executeQuery();

        return rs;
    }
}
