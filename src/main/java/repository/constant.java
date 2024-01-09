package repository;

public class constant {
    //Cashier
    public static final String SELECT_ALL_CASHIERS = "SELECT * FROM cashiers";
    public static final String INSERT_NEW_CASHIER = "INSERT INTO cashiers (name,phone_number,address) Values(?,?,?)";
    public static final  String DELETE_CASHIER = "DELETE FROM cashiers WHERE id=?";
    public static final  String UPDATE_CASHIER = "UPDATE cashiers SET name=?,phone_number=?,address=? WHERE id=?";

    //product
    public static final String SELECT_PRODUCT_BY_CATEGORY = "SELECT * FROM products WHERE category_id=? AND total > 0";
    public static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM products WHERE id=?";
    public static final String UPDATE_SELLING_STOCK = "UPDATE products SET total=total-? WHERE id=?";
    public static final String UPDATE_RETURN_STOCK = "UPDATE products SET total=total+? WHERE id=?";

    //categoty
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
}





