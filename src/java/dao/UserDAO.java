package dao;

import dto.UserDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBUtils;

public class UserDAO {
    private static final String LOGIN = 
    "SELECT UserID, FullName, Address, Phone, Role FROM Account WHERE Email =? AND Password =?";
    private static final String CREATE = "INSERT INTO Account (FullName, Password, RoleID) VALUES (?, ?, ?)";
    private static final String CHECK_DUPLICATE = "SELECT userID FROM Account WHERE userID = ?";
    private static final String GET_USER = "SELECT * FROM Account WHERE userID = ?";
    private static final String UPDATE_USER = "UPDATE Account SET FullName=?, Address=?, Phone=?, Email=? WHERE userID=?";
    private static final String UPDATE_PASSWORD = "UPDATE Account SET Password=? WHERE userID=?";

    public UserDTO checkLogin(String email, String password) throws SQLException {
        UserDTO user = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(LOGIN);
                ptm.setString(1, email);
                ptm.setString(2, password);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    int userID = rs.getInt("userID");                   
                    String fullName = rs.getString("fullName");
                    String role = rs.getString("role");
                    String address = rs.getString("address");
                    String phone = rs.getString("phone");
                    user = new UserDTO(userID, fullName, address, "", phone, email, role);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return user;
    }

    public boolean create(UserDTO user) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(CREATE);
                ptm.setString(1, user.getFullName());
                ptm.setString(2, user.getPassword());
                ptm.setString(3, user.getRole());
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

    public boolean checkDuplicate(String userID) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(CHECK_DUPLICATE);
                ptm.setString(1, userID);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    check = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return check;
    }

    public UserDTO getUser(String userID) throws SQLException {
        UserDTO user = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_USER);
                ptm.setString(1, userID);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    user = new UserDTO(
                            rs.getInt("userID"),
                            rs.getString("FullName"),
                            rs.getString("Address"),
                            rs.getString("Password"),
                            rs.getString("Phone"),
                            rs.getString("Email"),
                            rs.getString("RoleID")
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
        return user;
    }

    public boolean updateUser(UserDTO user) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(UPDATE_USER);
                ptm.setString(1, user.getFullName());
                ptm.setString(2, user.getAddress());
                ptm.setString(3, user.getPhone());
                ptm.setString(4, user.getEmail());
                ptm.setInt(5, user.getUserID());
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

    public boolean updatePassword(UserDTO user) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(UPDATE_PASSWORD);
                ptm.setString(1, user.getPassword());
                ptm.setInt(2, user.getUserID());
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