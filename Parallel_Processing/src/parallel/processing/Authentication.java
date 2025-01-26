/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parallel.processing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author roaam
 */
public class Authentication {

    public static String login(String card_num, String card_pin) {
        String query = "SELECT * FROM account WHERE cardNumber = '" + card_num + "' AND"
                + " cardPin = '" + card_pin + "'";
        ResultSet result = DBConnection.selectQuery(query);
        if (result == null) {
            return "";
        } else {
            String query2 = "SELECT * FROM account WHERE cardNumber = '" + card_num + "'";
            ResultSet rs = DBConnection.selectQuery(query2);
            try {
                String type = rs.getString("accountType");
                if (type.equals("Adminstration")) {
                    return "Admin";
                } else {
                    return "Customer";
                }
            } catch (SQLException ex) {
                Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static boolean checkCardNum(String card_num) {
        DBConnection.openConnection();
        String query = "SELECT * FROM account WHERE cardNumber = " + card_num;
        ResultSet result = DBConnection.selectQuery(query);
        if (result == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void logout(String accountID, String accountPass) {

    }
}
