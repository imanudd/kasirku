package repository;

public class constant {
    //supplier
    public static final String ADD_NEW_SUPPLIER = "INSERT INTO suppliers (name, address) VALUES(?,?)";
    public static final String UPDATE_SUPPLIER = "UPDATE suppliers SET name=?, address=? WHERE id=?";
    public static final String DELETE_SUPPLIER = "DELETE FROM suppliers WHERE id=?";
    //cashier
    public static final String SELECT_ALL_CASHIERS = "SELECT * FROM cashiers";
    public static final String INSERT_NEW_CASHIER = "INSERT INTO cashiers (name,phone_number,address) Values(?,?,?)";
    public static final  String DELETE_CASHIER = "DELETE FROM cashiers WHERE id=?";
    public static final  String UPDATE_CASHIER = "UPDATE cashiers SET name=?,phone_number=?,address=? WHERE id=?";

    //product
    public static final String DELETE_PRODUCT = "DELETE FROM products WHERE id=?";
    public static final String INSERT_PRODUCT = "INSERT INTO products VALUES (?,?,?,?,?,?)";
    public static final String SELECT_PRODUCT_DATA = "select p.id, p.name as product_name,c.name as category, total as last_stock, price, s.name as supplier_name from products as p\n" +
            "join categories as c on p.category_id = c.id\n" +
            "join suppliers as s on p.supplier_id = s.id\n" +
            "where p.category_id = ?";
    public static final String SELECT_PRODUCT_BY_CATEGORY = "SELECT * FROM products WHERE category_id=? AND total > 0";
    public static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM products WHERE id=?";
    public static final String UPDATE_SELLING_STOCK = "UPDATE products SET total=total-? WHERE id=?";
    public static final String UPDATE_RETURN_STOCK = "UPDATE products SET total=total+? WHERE id=?";
    public static final String UPDATE_PRODUCT = "UPDATE products SET name=?,total=?,price=? WHERE id=?";

    //category
    public static final String SELECT_ALL_CATEGORIES = "SELECT * FROM categories";

    //transaction
    public static final String INSERT_DETAIL_TRANSACTION = "INSERT INTO detail_transactions (id,transaction_id,product_id,quantity,total_price) Values(?,?,?,?,?)";
    public static final String UPSERT_TRANSACTION =
            "INSERT INTO transactions (id,total_price,created_at,cashier_id)" +
            "VALUES (?,?,?,?)"+
            "ON CONFLICT (id)"+
            "DO UPDATE SET total_price=?";

    public static final String GET_DETAIL_TRANSACTION = "SELECT * FROM detail_transactions as ds " +
            "JOIN products as p on ds.product_id = p.id " +
            "WHERE transaction_id = ?";

    public static final  String DELETE_DETAIL_TRANSACTION = "DELETE FROM detail_transactions WHERE id=?";

    public static final String UPDATE_GRAND_TOTAL = "UPDATE transactions SET total_price=? WHERE id=?";

    public static final String SELECT_TRANSACTION_BY_ID = "SELECT * FROM transactions WHERE id=?";
    
    //supplier
     public static final String SELECT_ALL_SUPPLIERS = "SELECT * FROM suppliers";

     //TransactionSQL
    public static final String TRANSACTION_UPDATE =
             "BEGIN TRANSACTION;\n" +
             "\n" +
             "UPDATE detail_transactions\n" +
             "SET quantity = ?, total_price = ? \n" +
             "WHERE id = ?;\n" +
             "\n" +
             "UPDATE transactions\n" +
             "SET total_price = total_price ? ?\n" +
             "WHERE id = ?;\n" +
             "\n" +
             "UPDATE products\n" +
             "SET total = total ? ?\n" +
             "WHERE id = ?;\n" +
             "\n" +
             "COMMIT;";
}






