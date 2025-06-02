package dao;

import dto.ProductDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class ProductDAO {
    private static final String GET_ALL_PRODUCTS = "SELECT * FROM Product";
    private static final String GET_PRODUCT_BY_ID = "SELECT * FROM Product WHERE ProductID = ?";
    private static final String GET_PRODUCTS_BY_CATEGORY = "SELECT * FROM Product WHERE CateID = ?";
    private static final String SEARCH_PRODUCTS = "SELECT * FROM Product WHERE ProductName LIKE ?";
    private static final String UPDATE_PRODUCT = "UPDATE Product SET ProductName=?, ProductImage=?, Description=?, Size=?, Price=?, Status=?, Stock=?, CateID=? WHERE ProductID=?";
    private static final String DELETE_PRODUCT = "DELETE FROM Product WHERE ProductID=?";
    private static final String CREATE_PRODUCT = "INSERT INTO Product (ProductName, ProductImage, Description, Size, Price, Status, Stock, CateID) VALUES (?,?,?,?,?,?,?,?)";

    public List<ProductDTO> getAllProducts() throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_ALL_PRODUCTS);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    list.add(new ProductDTO(
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getString("ProductImage"),
                            rs.getString("Description"),
                            rs.getString("Size"),
                            rs.getDouble("Price"),
                            rs.getString("Status"),
                            rs.getInt("Stock"),
                            rs.getInt("CateID")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return list;
    }

    public ProductDTO getProductByID(int productID) throws SQLException {
        ProductDTO product = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_PRODUCT_BY_ID);
                ptm.setInt(1, productID);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    product = new ProductDTO(
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getString("ProductImage"),
                            rs.getString("Description"),
                            rs.getString("Size"),
                            rs.getDouble("Price"),
                            rs.getString("Status"),
                            rs.getInt("Stock"),
                            rs.getInt("CateID")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return product;
    }

    public List<ProductDTO> getProductsByCategory(int cateID) throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_PRODUCTS_BY_CATEGORY);
                ptm.setInt(1, cateID);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    list.add(new ProductDTO(
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getString("ProductImage"),
                            rs.getString("Description"),
                            rs.getString("Size"),
                            rs.getDouble("Price"),
                            rs.getString("Status"),
                            rs.getInt("Stock"),
                            rs.getInt("CateID")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return list;
    }

    public List<ProductDTO> searchProducts(String search) throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(SEARCH_PRODUCTS);
                ptm.setString(1, "%" + search + "%");
                rs = ptm.executeQuery();
                while (rs.next()) {
                    list.add(new ProductDTO(
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getString("ProductImage"),
                            rs.getString("Description"),
                            rs.getString("Size"),
                            rs.getDouble("Price"),
                            rs.getString("Status"),
                            rs.getInt("Stock"),
                            rs.getInt("CateID")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return list;
    }

    public boolean updateProduct(ProductDTO product) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(UPDATE_PRODUCT);
                ptm.setString(1, product.getProductName());
                ptm.setString(2, product.getProductImage());
                ptm.setString(3, product.getDescription());
                ptm.setString(4, product.getSize());
                ptm.setDouble(5, product.getPrice());
                ptm.setString(6, product.getStatus());
                ptm.setInt(7, product.getStock());
                ptm.setInt(8, product.getCateID());
                ptm.setInt(9, product.getProductID());
                check = ptm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return check;
    }

    public boolean deleteProduct(int productID) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(DELETE_PRODUCT);
                ptm.setInt(1, productID);
                check = ptm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return check;
    }

    public boolean createProduct(ProductDTO product) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(CREATE_PRODUCT);
                ptm.setString(1, product.getProductName());
                ptm.setString(2, product.getProductImage());
                ptm.setString(3, product.getDescription());
                ptm.setString(4, product.getSize());
                ptm.setDouble(5, product.getPrice());
                ptm.setString(6, product.getStatus());
                ptm.setInt(7, product.getStock());
                ptm.setInt(8, product.getCateID());
                check = ptm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return check;
    }
} 