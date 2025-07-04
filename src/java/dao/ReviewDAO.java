package dao;

import dto.ReviewDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class ReviewDAO {

    public List<ReviewDTO> getReviewsByProductID(int productID) throws Exception {
        List<ReviewDTO> list = new ArrayList<>();
        String sql = "SELECT ReviewID, Rating, Comment, ReviewDate, UserID, ProductID " +
                     "FROM Review WHERE ProductID = ?";
        try (
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, productID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReviewDTO review = new ReviewDTO(
                    rs.getInt("ReviewID"),
                    rs.getInt("Rating"),
                    rs.getString("Comment"),
                    rs.getDate("ReviewDate"),
                    rs.getInt("UserID"),
                    rs.getInt("ProductID")
                );
                list.add(review);
            }
            rs.close();
        }
        return list;
    }

    public int getReviewCountByUser(int userID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Review WHERE UserID = ?";
        try (Connection con = utils.DBUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}
