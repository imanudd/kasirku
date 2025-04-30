package dto;

public class product {
    Integer id,categoryID,total,price,supplierID;
    String productName;

    public product(Integer id, Integer categoryID, Integer total, Integer price, Integer supplierID, String productName) {
        this.id = id;
        this.categoryID = categoryID;
        this.total = total;
        this.price = price;
        this.supplierID = supplierID;
        this.productName = productName;
    }

    public product(){
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


}
