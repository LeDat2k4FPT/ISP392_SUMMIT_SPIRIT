package dao;

import dto.ProductDTO;
import utils.DBUtils;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#.000");

    private static final String BASE_SELECT
            = "SELECT p.ProductID, p.ProductName, p.Description, "
            + "MIN(pv.Price) AS Price, MIN(pv.Quantity) AS Stock, "
            + "MIN(s.SizeName) AS Size, MIN(i.ImageURL) AS ImageURL, p.CateID ";

    private static final String BASE_FROM
            = "FROM Product p "
            + "LEFT JOIN ProductVariant pv ON p.ProductID = pv.ProductID "
            + "LEFT JOIN Size s ON pv.SizeID = s.SizeID "
            + "LEFT JOIN ProductImage i ON p.ProductID = i.ProductID ";

    // 1. Sắp xếp theo danh mục
    public List<ProductDTO> getProductsByCategorySorted(int cateID, String sortOrder)
            throws SQLException, ClassNotFoundException {
        List<ProductDTO> list = new ArrayList<>();
        String query = BASE_SELECT + BASE_FROM
                + "WHERE p.CateID = ? "
                + "GROUP BY p.ProductID, p.ProductName, p.Description, p.CateID "
                + "ORDER BY Price " + ("asc".equalsIgnoreCase(sortOrder) ? "ASC" : "DESC");

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ptm = conn.prepareStatement(query)) {

            ptm.setInt(1, cateID);
            try ( ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    list.add(extractProduct(rs));
                }
            }
        }
        return list;
    }

    // 2. Chi tiết sản phẩm
    public ProductDTO getFullProductByID(int productID)
            throws SQLException, ClassNotFoundException {
        ProductDTO dto = null;
        String sql = BASE_SELECT + BASE_FROM
                + "WHERE p.ProductID = ? "
                + "GROUP BY p.ProductID, p.ProductName, p.Description, p.CateID";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ptm = conn.prepareStatement(sql)) {
            ptm.setInt(1, productID);
            try ( ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {
                    dto = extractProduct(rs);
                }
            }
        }
        return dto;
    }

    // 3. Lấy theo mảng ID
    public List<ProductDTO> getTopNProductsByIDs(int[] ids)
            throws SQLException, ClassNotFoundException {
        List<ProductDTO> list = new ArrayList<>();
        String sql = BASE_SELECT + BASE_FROM
                + "WHERE p.ProductID = ? "
                + "GROUP BY p.ProductID, p.ProductName, p.Description, p.CateID";

        try ( Connection conn = DBUtils.getConnection()) {
            for (int id : ids) {
                try ( PreparedStatement ptm = conn.prepareStatement(sql)) {
                    ptm.setInt(1, id);
                    try ( ResultSet rs = ptm.executeQuery()) {
                        if (rs.next()) {
                            list.add(extractProduct(rs));
                        }
                    }
                }
            }
        }
        return list;
    }

    // 4. Lấy 3 sản phẩm mặc định
    public List<ProductDTO> getTop3Products() throws SQLException, ClassNotFoundException {
        List<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT TOP 3 " + BASE_SELECT.substring(7) + BASE_FROM
                + "GROUP BY p.ProductID, p.ProductName, p.Description, p.CateID";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ptm = conn.prepareStatement(sql);  ResultSet rs = ptm.executeQuery()) {
            while (rs.next()) {
                list.add(extractProduct(rs));
            }
        }
        return list;
    }

    // 5. Lấy theo danh mục - DISTINCT
    public List<ProductDTO> getProductsByCategoryDistinct(int cateID)
            throws SQLException, ClassNotFoundException {
        List<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT DISTINCT " + BASE_SELECT.substring(7) + BASE_FROM
                + "WHERE p.CateID = ?";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ptm = conn.prepareStatement(sql)) {
            ptm.setInt(1, cateID);
            try ( ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    list.add(extractProduct(rs));
                }
            }
        }
        return list;
    }

    // 6. Insert sản phẩm AddProductController
    public int insertAndReturnID(ProductDTO product) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Product (ProductName, Description, CateID, Status) VALUES (?, ?, ?, ?)";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getDescription());
            ps.setInt(3, product.getCateID());
            ps.setString(4, product.getStatus());
            ps.executeUpdate();

            try ( ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // trả về ProductID mới tạo
                }
            }
        }
        return -1;
    }

    // 7. Lấy ID tiếp theo
    private int getNextProductID() throws SQLException, ClassNotFoundException {
        String sql = "SELECT ISNULL(MAX(ProductID), 0) + 1 FROM Product";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 1;
    }

//    // 8. Lấy tất cả sản phẩm (chỉ từ bảng Product)
//    public List<ProductDTO> getAllProducts() throws SQLException, ClassNotFoundException {
//        List<ProductDTO> list = new ArrayList<>();
//        String sql = "SELECT * FROM Product";
//        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                ProductDTO p = new ProductDTO(
//                        rs.getInt("ProductID"),
//                        rs.getString("ProductName"),
//                        null,
//                        rs.getString("Description"),
//                        null,
//                        0.0,
//                        rs.getString("Status"),
//                        0,
//                        rs.getInt("CateID")
//                );
//                list.add(p);
//            }
//        }
//        return list;
//    }
    public List<ProductDTO> getAllProducts() throws SQLException, ClassNotFoundException {
        List<ProductDTO> list = new ArrayList<>();
        String sql
                = "SELECT p.ProductID, p.ProductName, p.Description, p.Status, p.CateID, "
                + "       (SELECT TOP 1 Price FROM ProductVariant WHERE ProductID = p.ProductID) AS Price, "
                + "       (SELECT TOP 1 ImageURL FROM ProductImage WHERE ProductID = p.ProductID) AS ImageURL "
                + "FROM Product p";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductDTO p = new ProductDTO();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                p.setDescription(rs.getString("Description"));
                p.setStatus(rs.getString("Status"));
                p.setCateID(rs.getInt("CateID"));

                double price = rs.getDouble("Price");
                p.setPrice(price);

                String img = rs.getString("ImageURL");
                p.setProductImage(img != null ? img : "default.jpg");

                list.add(p);
            }
        }
        return list;
    }

    // 9. Top bán chạy theo danh mục
    public List<ProductDTO> getTopSalesFromCategory(int cateID, int limit)
            throws SQLException, ClassNotFoundException {
        List<ProductDTO> result = new ArrayList<>();
        String sql = "SELECT TOP (?) " + BASE_SELECT.substring(7) + BASE_FROM
                + "WHERE p.CateID = ? AND p.Sold > 0 "
                + "GROUP BY p.ProductID, p.ProductName, p.Description, p.CateID "
                + "ORDER BY p.Sold DESC";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, cateID);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(extractProduct(rs));
                }
            }
        }
        return result;
    }

    // ============================
    // HÀM TIỆN ÍCH DÙNG CHUNG
    // ============================
    private ProductDTO extractProduct(ResultSet rs) throws SQLException {
        ProductDTO dto = new ProductDTO();
        dto.setProductID(rs.getInt("ProductID"));
        dto.setProductName(rs.getString("ProductName"));
        dto.setDescription(rs.getString("Description"));
        dto.setPrice(Double.parseDouble(PRICE_FORMAT.format(rs.getDouble("Price"))));
        dto.setSize(rs.getString("Size"));
        dto.setStock(rs.getInt("Stock"));
        dto.setCateID(rs.getInt("CateID"));
        String image = rs.getString("ImageURL");
        dto.setProductImage(image != null ? image : "default.jpg");
        return dto;
    }

    public boolean deleteProductByID(int productID) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        boolean result = false;

        try {
            con = DBUtils.getConnection();
            con.setAutoCommit(false); // Bắt đầu transaction

            // 1. Xóa variant
            ProductVariantDAO variantDAO = new ProductVariantDAO();
            variantDAO.deleteByProductID(productID);

            // 2. Xóa ảnh
            ProductImageDAO imageDAO = new ProductImageDAO();
            imageDAO.deleteByProductID(productID);

            // 3. Xóa sản phẩm
            String sql = "DELETE FROM Product WHERE ProductID = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, productID);
            int rows = ps.executeUpdate();

            con.commit(); // commit nếu mọi thứ thành công
            result = rows > 0;

        } catch (Exception e) {
            if (con != null) {
                con.rollback(); // rollback nếu lỗi
            }
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.setAutoCommit(true);
            }
            con.close();
        }

        return result;
    }

    public ProductDTO getProductByID(int productID) throws SQLException, ClassNotFoundException {
    String sql = "SELECT p.ProductID, p.ProductName, p.Description, p.Status, p.CateID, " +
                 "(SELECT TOP 1 Price FROM ProductVariant WHERE ProductID = p.ProductID) AS Price, " +
                 "(SELECT TOP 1 ImageURL FROM ProductImage WHERE ProductID = p.ProductID) AS ImageURL " +
                 "FROM Product p WHERE p.ProductID = ?";
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, productID);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                ProductDTO dto = new ProductDTO();
                dto.setProductID(rs.getInt("ProductID"));
                dto.setProductName(rs.getString("ProductName"));
                dto.setDescription(rs.getString("Description"));
                dto.setStatus(rs.getString("Status"));
                dto.setCateID(rs.getInt("CateID"));
                dto.setPrice(rs.getDouble("Price"));
                String img = rs.getString("ImageURL");
                dto.setProductImage(img != null ? img : "default.jpg");
                return dto;
            }
        }
    }
    return null;
}

public boolean updateProductByID(ProductDTO product) throws SQLException, ClassNotFoundException {
    String sql = "UPDATE Product SET ProductName = ?, Description = ?, Status = ?, CateID = ? WHERE ProductID = ?";
    try (
        Connection conn = DBUtils.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setString(1, product.getProductName());
        ps.setString(2, product.getDescription());
        ps.setString(3, product.getStatus());
        ps.setInt(4, product.getCateID());
        ps.setInt(5, product.getProductID());
        return ps.executeUpdate() > 0;
    }
}


}
