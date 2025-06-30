package dto;

public class ProductVariantDTO {

    private int attributeID;  // ID duy nhất cho mỗi biến thể
    private int productID;
    private int colorID;
    private int sizeID;
    private double price;
    private int quantity;

    private String colorName;
    private String sizeName;

    // ========== Constructors ==========

    public ProductVariantDTO() {
    }

    public ProductVariantDTO(int productID, int colorID, int sizeID, double price, int quantity) {
        this.productID = productID;
        this.colorID = colorID;
        this.sizeID = sizeID;
        this.price = price;
        this.quantity = quantity;
    }

    public ProductVariantDTO(int attributeID, int productID, int colorID, int sizeID, double price, int quantity) {
        this.attributeID = attributeID;
        this.productID = productID;
        this.colorID = colorID;
        this.sizeID = sizeID;
        this.price = price;
        this.quantity = quantity;
    }

    // ========== Getters & Setters ==========

    public int getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(int attributeID) {
        this.attributeID = attributeID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getColorID() {
        return colorID;
    }

    public void setColorID(int colorID) {
        this.colorID = colorID;
    }

    public int getSizeID() {
        return sizeID;
    }

    public void setSizeID(int sizeID) {
        this.sizeID = sizeID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }
}
