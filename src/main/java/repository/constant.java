package repository;

public class constant {
    //Cashier
    public static final String SELECT_ALL_CASHIERS = "SELECT * FROM kasir";
    public static final String INSERT_NEW_CASHIER = "INSERT INTO kasir (nama,no_telp,alamat) Values(?,?,?)";
    public static final  String DELETE_CASHIER = "DELETE FROM kasir WHERE id=?";
    public static final  String UPDATE_CASHIER = "UPDATE kasir SET nama=?,no_telp=?,alamat=? WHERE id=?";

    //transaction
}
