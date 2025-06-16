package dao;

import dto.ProductDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class ProductDAO {

     private static final String GET_PRODUCTS_BY_CATEGORY_SORTED =
        "SELECT p.ProductID, p.ProductName, p.Description, " +
        "p.Price, MIN(pa.Size) AS Size, MIN(pa.Stock) AS Stock, " +
        "MIN(i.ImageURL) AS ImageURL, p.CateID " +
        "FROM Product p " +
        "LEFT JOIN ProductAttribute pa ON p.ProductID = pa.ProductID " +
        "LEFT JOIN ProductImages i ON p.ProductID = i.ProductID " +
        "WHERE p.CateID = ? " +
        "GROUP BY p.ProductID, p.ProductName, p.Description, p.Price, p.CateID " +
        "ORDER BY p.Price ";

    public List<ProductDTO> getProductsByCategorySorted(int cateID, String sortOrder)
            throws SQLException, ClassNotFoundException {

        List<ProductDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;

        String query = GET_PRODUCTS_BY_CATEGORY_SORTED +
                       ("asc".equalsIgnoreCase(sortOrder) ? "ASC" : "DESC");

        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(query);
                ptm.setInt(1, cateID);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    ProductDTO dto = new ProductDTO();
                    dto.setProductID(rs.getInt("ProductID"));
                    dto.setProductName(rs.getString("ProductName"));
                    dto.setDescription(rs.getString("Description"));
                    dto.setPrice(rs.getDouble("Price"));
                    dto.setSize(rs.getString("Size"));
                    dto.setStock(rs.getInt("Stock"));
                    String image = rs.getString("ImageURL");
                    dto.setProductImage(image != null ? image : "default.jpg");
                    dto.setCateID(rs.getInt("CateID"));
                    list.add(dto);
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }

        return list;
    }

    public ProductDTO getFullProductByID(int productID)
            throws SQLException, ClassNotFoundException {

        ProductDTO dto = null;
        String sql = "SELECT TOP 1 p.ProductID, p.ProductName, p.Description, " +
                     "p.Price, pa.Size, pa.Stock, i.ImageURL, p.CateID " +
                     "FROM Product p " +
                     "LEFT JOIN ProductAttribute pa ON p.ProductID = pa.ProductID " +
                     "LEFT JOIN ProductImages i ON p.ProductID = i.ProductID " +
                     "WHERE p.ProductID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ptm = conn.prepareStatement(sql)) {

            ptm.setInt(1, productID);
            try (ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {
                    dto = new ProductDTO();
                    dto.setProductID(rs.getInt("ProductID"));
                    dto.setProductName(rs.getString("ProductName"));
                    dto.setDescription(rs.getString("Description"));
                    dto.setPrice(rs.getDouble("Price"));
                    dto.setSize(rs.getString("Size"));
                    dto.setStock(rs.getInt("Stock"));
                    String image = rs.getString("ImageURL");
                    dto.setProductImage(image != null ? image : "default.jpg");
                    dto.setCateID(rs.getInt("CateID"));
                }
            }
        }

        return dto;
    }

    public List<ProductDTO> getTopNProductsByIDs(int[] ids)
            throws SQLException, ClassNotFoundException {

        List<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT TOP 1 p.ProductID, p.ProductName, p.Description, " +
                     "p.Price, pa.Size, pa.Stock, i.ImageURL, p.CateID " +
                     "FROM Product p " +
                     "LEFT JOIN ProductAttribute pa ON p.ProductID = pa.ProductID " +
                     "LEFT JOIN ProductImages i ON p.ProductID = i.ProductID " +
                     "WHERE p.ProductID = ?";

        try (Connection conn = DBUtils.getConnection()) {
            for (int id : ids) {
                try (PreparedStatement ptm = conn.prepareStatement(sql)) {
                    ptm.setInt(1, id);
                    try (ResultSet rs = ptm.executeQuery()) {
                        if (rs.next()) {
                            ProductDTO dto = new ProductDTO();
                            dto.setProductID(rs.getInt("ProductID"));
                            dto.setProductName(rs.getString("ProductName"));
                            dto.setDescription(rs.getString("Description"));
                            dto.setPrice(rs.getDouble("Price"));
                            dto.setSize(rs.getString("Size"));
                            dto.setStock(rs.getInt("Stock"));
                            String image = rs.getString("ImageURL");
                            dto.setProductImage(image != null ? image : "default.jpg");
                            dto.setCateID(rs.getInt("CateID"));
                            list.add(dto);
                        }
                    }
                }
            }
        }

        return list;
    }

    // Giữ nguyên: getProductsByCategoryDistinct nếu bạn dùng nơi khác
    public List<ProductDTO> getProductsByCategoryDistinct(int cateID)
            throws SQLException, ClassNotFoundException {

        List<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT DISTINCT p.ProductID, p.ProductName, p.Description, " +
                     "p.Price, pa.Size, pa.Stock, i.ImageURL, p.CateID " +
                     "FROM Product p " +
                     "LEFT JOIN ProductAttribute pa ON p.ProductID = pa.ProductID " +
                     "LEFT JOIN ProductImages i ON p.ProductID = i.ProductID " +
                     "WHERE p.CateID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ptm = conn.prepareStatement(sql)) {

            ptm.setInt(1, cateID);
            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    ProductDTO dto = new ProductDTO();
                    dto.setProductID(rs.getInt("ProductID"));
                    dto.setProductName(rs.getString("ProductName"));
                    dto.setDescription(rs.getString("Description"));
                    dto.setPrice(rs.getDouble("Price"));
                    dto.setSize(rs.getString("Size"));
                    dto.setStock(rs.getInt("Stock"));
                    String image = rs.getString("ImageURL");
                    dto.setProductImage(image != null ? image : "default.jpg");
                    dto.setCateID(rs.getInt("CateID"));
                    list.add(dto);
                }
            }
        }

        return list;
    }

    // Lấy 3 sản phẩm mặc định khi không chọn danh mục
    public List<ProductDTO> getTop3Products()
            throws SQLException, ClassNotFoundException {

        List<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT TOP 3 p.ProductID, p.ProductName, p.Description, " +
                     "p.Price, pa.Size, pa.Stock, i.ImageURL, p.CateID " +
                     "FROM Product p " +
                     "LEFT JOIN ProductAttribute pa ON p.ProductID = pa.ProductID " +
                     "LEFT JOIN ProductImages i ON p.ProductID = i.ProductID";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ptm = conn.prepareStatement(sql);
             ResultSet rs = ptm.executeQuery()) {

            while (rs.next()) {
                ProductDTO dto = new ProductDTO();
                dto.setProductID(rs.getInt("ProductID"));
                dto.setProductName(rs.getString("ProductName"));
                dto.setDescription(rs.getString("Description"));
                dto.setPrice(rs.getDouble("Price"));
                dto.setSize(rs.getString("Size"));
                dto.setStock(rs.getInt("Stock"));
                String image = rs.getString("ImageURL");
                dto.setProductImage(image != null ? image : "default.jpg");
                dto.setCateID(rs.getInt("CateID"));
                list.add(dto);
            }
        }

        return list;
    
    }
   public int insertAndReturnID(ProductDTO product) throws SQLException, ClassNotFoundException {
    int newProductID = getNextProductID(); // lấy ID mới

    String sql = "INSERT INTO Product (ProductID, ProductName, Description, CateID, Price, Status) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, newProductID);
        ps.setString(2, product.getProductName());
        ps.setString(3, product.getDescription());
        ps.setInt(4, product.getCateID());
        ps.setDouble(5, product.getPrice());
        ps.setString(6, product.getStatus());

        ps.executeUpdate();
        return newProductID;
    }
}

public int getNextProductID() throws SQLException, ClassNotFoundException {
    String sql = "SELECT ISNULL(MAX(ProductID), 0) + 1 FROM Product";
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            return rs.getInt(1);
        }
    }
    return 1; // fallback nếu bảng rỗng
}


}
