/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class DBUtils {
<<<<<<< HEAD
<<<<<<< HEAD
    private static final String DB_NAME = "ISM302";
=======
<<<<<<< HEAD
    private static final String DB_NAME = "ISP302";
=======
    private static final String DB_NAME = "ISM302";
>>>>>>> 3bc1e18dc4be164fe4acac256b9f36a99958f6a7
>>>>>>> dd4dc50dad166f64a8d749f7f7c88852de362a43
=======
    private static final String DB_NAME = "ISP302";
=======
    private static final String DB_NAME = "ISM302";
>>>>>>> 608a15763e5e09c5cb5d6b029a0d33fd43000f9f
>>>>>>> 6a22f44c9917dddd110f1771c211d30f0c0fea21
    private static final String DB_USER_NAME = "sa";
    private static final String DB_PASSWORD = "12345";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost:1433;databaseName=" + DB_NAME;
        conn = DriverManager.getConnection(url, DB_USER_NAME, DB_PASSWORD);
        return conn;
    }
}
