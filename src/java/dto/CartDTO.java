package dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class CartDTO implements Serializable {
    private int userID; // Liên kết giỏ hàng với người dùng
    private Map<String, CartItemDTO> cartItems; // ✅ Key: productID_size_color

    public CartDTO() {
        this.cartItems = new HashMap<>();
    }

    public CartDTO(int userID) {
        this.userID = userID;
        this.cartItems = new HashMap<>();
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    // 🔑 Tạo key từ productID + size + color
    private String buildKey(int productID, String size, String color) {
        return productID + "_" + size + "_" + color;
    }

    // ✅ Thêm sản phẩm vào giỏ (theo productID + size + color)
    public void addToCart(ProductDTO product, int quantity) {
        if (product == null || quantity <= 0) {
            return;
        }

        String size = product.getSize();
        String color = product.getColor();
        String key = buildKey(product.getProductID(), size, color);

        if (cartItems.containsKey(key)) {
            CartItemDTO existingItem = cartItems.get(key);
            int newQuantity = existingItem.getQuantity() + quantity;

            if (newQuantity <= 0) {
                cartItems.remove(key);
            } else {
                existingItem.setQuantity(newQuantity);
            }
        } else {
            cartItems.put(key, new CartItemDTO(product, quantity));
        }
    }

    // ✅ Cập nhật số lượng theo productID + size + color
    public void updateQuantity(int productID, String size, String color, int newQuantity) {
        String key = buildKey(productID, size, color);

        if (newQuantity <= 0) {
            cartItems.remove(key);
        } else if (cartItems.containsKey(key)) {
            cartItems.get(key).setQuantity(newQuantity);
        }
    }

    // ✅ Xóa sản phẩm theo productID + size + color
    public void removeFromCart(int productID, String size, String color) {
        String key = buildKey(productID, size, color);
        cartItems.remove(key);
    }

    // ✅ Xóa tất cả biến thể theo productID (giữ tương thích hệ thống cũ)
    public void removeFromCart(int productID) {
        String prefix = productID + "_";
        cartItems.keySet().removeIf(key -> key.startsWith(prefix));
    }

    // ✅ Lấy toàn bộ sản phẩm trong giỏ
    public Collection<CartItemDTO> getCartItems() {
        return cartItems.values();
    }

    // ✅ Tính tổng số lượng
    public int getTotalQuantity() {
        int total = 0;
        for (CartItemDTO item : cartItems.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    // ✅ Tính tổng tiền
    public double getTotalPrice() {
        double total = 0;
        for (CartItemDTO item : cartItems.values()) {
            total += item.getQuantity() * item.getProduct().getPrice();
        }
        return total;
    }

    // ✅ Xóa toàn bộ giỏ
    public void clearCart() {
        cartItems.clear();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    // ✅ Truy xuất sản phẩm theo productID + size + color
    public CartItemDTO getCartItem(int productID, String size, String color) {
        return cartItems.get(buildKey(productID, size, color));
    }

    // ✅ Truy xuất sản phẩm theo productID (giữ lại phiên bản cũ)
    public CartItemDTO getCartItem(int productID) {
        for (String key : cartItems.keySet()) {
            if (key.startsWith(productID + "_")) {
                return cartItems.get(key);
            }
        }
        return null;
    }

    // ✅ Cập nhật số lượng theo key "productID_size_color"
    public void updateQuantity(String key, int quantity) {
        if (cartItems.containsKey(key)) {
            if (quantity <= 0) {
                cartItems.remove(key);
            } else {
                cartItems.get(key).setQuantity(quantity);
            }
        }
    }
}
