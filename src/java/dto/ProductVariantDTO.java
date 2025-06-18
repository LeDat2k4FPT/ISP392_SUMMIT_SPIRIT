/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author Admin
 */

public class ProductVariantDTO {

    private int attributeID; // không bắt buộc nếu auto-increment
    private int productID;
    private int colorID;
    private int sizeID;
    private double price;
    private int quantity;

    public ProductVariantDTO() {
    }

    public ProductVariantDTO(int productID, int colorID, int sizeID, double price, int quantity) {
        this.productID = productID;
        this.colorID = colorID;
        this.sizeID = sizeID;
        this.price = price;
        this.quantity = quantity;
    }
    

    // Getters
    public int getProductID() {
        return productID;
    }

    public int getColorID() {
        return colorID;
    }

    public int getSizeID() {
        return sizeID;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
