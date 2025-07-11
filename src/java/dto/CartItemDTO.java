package dto;

public class CartItemDTO {

    private ProductDTO product;
    private int quantity;
    private String color; // ✅ Thêm thuộc tính color
    private boolean fromSaleOff = false; // ✅ Thêm thuộc tính fromSaleOff

    // ✅ Constructor đầy đủ với color và fromSaleOff
    public CartItemDTO(ProductDTO product, int quantity, String color, boolean fromSaleOff) {
        this.product = product;
        this.quantity = quantity;
        this.color = color;
        this.fromSaleOff = fromSaleOff;
    }

    // ✅ Constructor giữ lại để tương thích (nếu cần)
    public CartItemDTO(ProductDTO product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.color = product.getColor(); // nếu không truyền color riêng, lấy từ product
        this.fromSaleOff = product.isFromSaleOff(); // lấy trực tiếp từ product
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

    // ✅ Getter giá hiện tại
    public double getPrice() {
        return product.getPrice();
    }

    // ✅ Getter và Setter cho fromSaleOff
    public boolean isFromSaleOff() {
        return fromSaleOff;
    }

    public void setFromSaleOff(boolean fromSaleOff) {
        this.fromSaleOff = fromSaleOff;
    }
}
