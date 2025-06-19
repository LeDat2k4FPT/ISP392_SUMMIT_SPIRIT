package dto;

import java.util.List;

public class ProductDTO {

    private int productID;
    private String productName;
    private String description;
    private String status;
    private int cateID;

    private String productImage; // ảnh đại diện
    private double price;        // giá của 1 biến thể mặc định hoặc thấp nhất
    private String productVariant; // mô tả hiển thị ("Size M - Red") nếu có

    // Danh sách tất cả biến thể của sản phẩm
    private List<ProductVariantDTO> variants;

    public ProductDTO() {
    }

    public ProductDTO(int productID, String productName, String description, String status, int cateID,
                      String productImage, double price, String productVariant, List<ProductVariantDTO> variants) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.status = status;
        this.cateID = cateID;
        this.productImage = productImage;
        this.price = price;
        this.productVariant = productVariant;
        this.variants = variants;
    }

    // Getters and Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCateID() {
        return cateID;
    }

    public void setCateID(int cateID) {
        this.cateID = cateID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(String productVariant) {
        this.productVariant = productVariant;
    }

    public List<ProductVariantDTO> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariantDTO> variants) {
        this.variants = variants;
    }
}
