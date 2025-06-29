package dto;

public class CartItemDTO {
    private ProductDTO product;
    private int quantity;
    private String color; // ✅ Thêm thuộc tính color

    // ✅ Constructor đầy đủ với color
    public CartItemDTO(ProductDTO product, int quantity, String color) {
        this.product = product;
        this.quantity = quantity;
        this.color = color;
    }

    // ✅ Constructor cũ để tương thích (nếu cần)
    public CartItemDTO(ProductDTO product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // ✅ Getter tiện dụng để truy xuất size từ product
    public String getSize() {
        return product.getSize();
    }

    // ✅ Getter tiện dụng để truy xuất productID
    public int getProductID() {
        return product.getProductID();
    }

    // ✅ Getter và Setter cho color
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
