package parallel.processing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simplified and enhanced DBConnection class with better efficiency and thread safety.
 */
public class DBConnection {
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String HOST = "jdbc:mysql://localhost/bank";

    private static Connection con;

    public static synchronized Connection openConnection() {
        if (con == null) {
            try {
                con = DriverManager.getConnection(HOST, USER, PASSWORD);
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return con;
    }


    public static ResultSet selectQuery(String query) {
        Connection con = openConnection();
        try {
            ResultSet rs = con.prepareStatement(query).executeQuery();
            if (rs.next()) {
                return rs;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    public static ResultSet selectQueryReturnMultipleRows(String query) {
        Connection con = openConnection();
        try {
            return con.prepareStatement(query).executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    public static void updateQuery(String query) {
        Connection con = openConnection();
        try {
            con.prepareStatement(query).executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static synchronized void closeConnection() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
