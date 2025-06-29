package dto;

public class ProductDTO {

    private int productID;
    private String productName;
    private String productImage;
    private String description;
    private String size;
    private String color; // ✅ Đã thêm
    private double price;
    private String status;
    private int stock;
    private int cateID;
    private String cateName;

    public ProductDTO() {
    }

    public ProductDTO(int productID, String productName, String productImage, String description,
            String size, double price, String status, int stock, int cateID) {
        this.productID = productID;
        this.productName = productName;
        this.productImage = productImage;
        this.description = description;
        this.size = size;
        this.price = price;
        this.status = status;
        this.stock = stock;
        this.cateID = cateID;
    }

    public ProductDTO(int productID, String productName, String description, int cateID, String status) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.cateID = cateID;
        this.status = status;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {                      // ✅ Getter
        return color;
    }

    public void setColor(String color) {            // ✅ Setter
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCateID() {
        return cateID;
    }

    public void setCateID(int cateID) {
        this.cateID = cateID;
    }
}
