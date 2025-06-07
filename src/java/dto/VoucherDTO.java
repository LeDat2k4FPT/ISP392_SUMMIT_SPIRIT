package dto;

public class VoucherDTO {
    private int voucherID;
    private String voucherCode;
    private double discountValue;

    public VoucherDTO(int voucherID, String voucherCode, double discountValue) {
        this.voucherID = voucherID;
        this.voucherCode = voucherCode;
        this.discountValue = discountValue;
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }
}
