package dto;

import java.util.Date;

public class ReviewDTO {
    private int reviewID;
    private int rating;
    private String comment;
    private Date reviewDate;
    private int userID;
    private int productID;

    public ReviewDTO(int reviewID, int rating, String comment, Date reviewDate, int userID, int productID) {
        this.reviewID = reviewID;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.userID = userID;
        this.productID = productID;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
}
