package dao;

import dto.UserDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class UserDAO {

    private static final String LOGIN = "SELECT UserID, FullName, Address, Phone, Role FROM Account WHERE Email = ? AND Password = ?";
    private static final String CREATE = "INSERT INTO Account (FullName, Address, Email, Phone, Role, Password) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String EDIT_PROFILE = "UPDATE Account set FullName=?, Address=?, Email=?, Phone=? WHERE UserID=?";
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
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
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
                ptm.setString(2, user.getAddress());
                ptm.setString(3, user.getEmail());
                ptm.setString(4, user.getPhone());
                ptm.setString(5, user.getRole());
                ptm.setString(6, user.getPassword());
                check = ptm.executeUpdate() > 0 ? true : false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }

    public boolean edit(UserDTO user) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(EDIT_PROFILE);
                ptm.setString(1, user.getFullName());
                ptm.setString(2, user.getAddress());
                ptm.setString(3, user.getEmail());
                ptm.setString(4, user.getPhone());
                ptm.setInt(5, user.getUserID());
                check = ptm.executeUpdate() > 0 ? true : false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
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
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
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
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
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
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }

    public List<UserDTO> searchUsers(String keyword) {
    List<UserDTO> list = new ArrayList<>();
    String sql = "SELECT * FROM Account WHERE (FullName LIKE ? OR Email LIKE ? OR Address LIKE ? OR Role LIKE ?) AND Role <> 'admin'";


    try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        String k = "%" + keyword + "%";
        for (int i = 1; i <= 4; i++) {
            ps.setString(i, k);
        }
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            UserDTO user = new UserDTO(
                rs.getInt("UserID"),
                rs.getString("FullName"),
                rs.getString("Address"),
                rs.getString("Password"),    
                rs.getString("Email"),                
                rs.getString("Phone"),
                rs.getString("Role")
            );
            list.add(user);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
    public List<UserDTO> getAllUsers() {
    List<UserDTO> list = new ArrayList<>();
    String sql = "SELECT * FROM Account WHERE Role <> 'admin'";

    try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            UserDTO user = new UserDTO(
                rs.getInt("UserID"),
                rs.getString("FullName"),
                rs.getString("Address"),
                rs.getString("Password"),    
                rs.getString("Email"),                
                rs.getString("Phone"),
                rs.getString("Role")
            );
            list.add(user);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
    public void toggleRole(int userID) {
    String sql = "UPDATE Account SET Role = CASE WHEN Role = 'user' THEN 'staff' ELSE 'user' END WHERE UserID = ?";
    try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userID);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void setActive(int userID, boolean active) {
    String sql = "UPDATE Account SET Status = ? WHERE UserID = ?";
    try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, active ? 1 : 0); // 1 = active, 0 = inactive
        ps.setInt(2, userID);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
