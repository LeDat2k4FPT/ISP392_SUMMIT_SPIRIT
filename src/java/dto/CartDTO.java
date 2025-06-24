package dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class CartDTO implements Serializable {
    private int userID; // Liên kết giỏ hàng với người dùng
    private Map<Integer, CartItemDTO> cartItems;

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

    public void addToCart(ProductDTO product, int quantity) {
        if (product == null || quantity <= 0) {
            return;
        }

        int productID = product.getProductID();

        if (cartItems.containsKey(productID)) {
            CartItemDTO existingItem = cartItems.get(productID);
            int newQuantity = existingItem.getQuantity() + quantity;

            if (newQuantity <= 0) {
                cartItems.remove(productID);
            } else {
                existingItem.setQuantity(newQuantity);
            }
        } else {
            cartItems.put(productID, new CartItemDTO(product, quantity));
        }
    }

    public void updateQuantity(int productID, int newQuantity) {
        if (newQuantity <= 0) {
            cartItems.remove(productID);
        } else if (cartItems.containsKey(productID)) {
            cartItems.get(productID).setQuantity(newQuantity);
        }
    }

    public void removeFromCart(int productID) {
        cartItems.remove(productID);
    }

    public Collection<CartItemDTO> getCartItems() {
        return cartItems.values();
    }

    public int getTotalQuantity() {
        int total = 0;
        for (CartItemDTO item : cartItems.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItemDTO item : cartItems.values()) {
            total += item.getQuantity() * item.getProduct().getPrice();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public CartItemDTO getCartItem(int productID) {
        return cartItems.get(productID);
    }
}
