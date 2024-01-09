package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class category {
    Connection conn;
    PreparedStatement pst;
    Statement statement;
    ResultSet rs;
    public ResultSet getData(Connection conn) throws Exception{
        pst = conn.prepareStatement(constant.SELECT_ALL_CATEGORIES);

        return rs = pst.executeQuery();
    }
}
