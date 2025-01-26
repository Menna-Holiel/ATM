/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parallel.processing;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.time.LocalDate;

import static parallel.processing.ATM.markAccountInactive;

/**
 *
 * @author roaam
 */
public class Account{

    private int cardNumber;
    private int cardPIN;

    public Account(int cardNumber, int cardPIN) {
        this.cardNumber = cardNumber;
        this.cardPIN = cardPIN;
    }

    public synchronized void transfer(String desCardNum, String tAmount) {
        if (Validation.isEmpty(desCardNum)) {
            JOptionPane.showMessageDialog(null, "You Cannot Leave This Field Empty!");
        } else if (!Validation.isOnlyNumbers(desCardNum)) {
            JOptionPane.showMessageDialog(null, "Only Numbers are Allowed !");
        } else if (Validation.isEmpty(tAmount)){
            JOptionPane.showMessageDialog(null, "You Cannot Leave This Field Empty!");
        } else if (!Validation.isOnlyNumbers(tAmount)) {
            JOptionPane.showMessageDialog(null, "Only Numbers are Allowed !");
        } else {
            int dCardNum = Integer.parseInt(desCardNum);
            int amount = Integer.parseInt(tAmount);
            
            if (amount > 10000) {
                JOptionPane.showMessageDialog(null, "That is More than the Limit !");
            } else{
                try {
                    DBConnection.openConnection();
                    String query1 = "SELECT * FROM account WHERE cardNumber = " + cardNumber;
                    int balance = Integer.parseInt(DBConnection.selectQuery(query1).getString("balance"));
                    if (amount > balance) {
                        JOptionPane.showMessageDialog(null, "You Don't have Enough Money for this Transaction !");
                    } else if (cardNumber == dCardNum) {
                        JOptionPane.showMessageDialog(null, "You Can't Transfer Money To Your Card Number!");
                    } else{
                        boolean result1 = Authentication.checkCardNum(desCardNum);
                        if (result1 == false){
                            JOptionPane.showMessageDialog(null, "Destination Card Number Not Found!");
                        } else{
                            
                            int newBalance = balance - amount;
                            String query3 = "UPDATE account SET balance = " + newBalance + " WHERE cardNumber = " + cardNumber;
                            DBConnection.updateQuery(query3);
                            String query4 = "SELECT * FROM account WHERE cardNumber = " + dCardNum;
                            int dBalance = Integer.parseInt(DBConnection.selectQuery(query4).getString("balance"));
                            int dNewBalance = dBalance + amount;
                            String query5 = "UPDATE account SET balance = " + dNewBalance + " WHERE cardNumber = " + dCardNum;
                            DBConnection.updateQuery(query5);
                            DBConnection.closeConnection();
                            JOptionPane.showMessageDialog(null, "Transfer Successfully :)");
                            this.transactionLogging("Transfer", balance, LocalDate.now());
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public synchronized void transactionLogging(String transactionType, int balanceBefore, LocalDate date) {
        try {
            
            String query1 = "SELECT * FROM account WHERE cardNumber = " + cardNumber;
            String accountId = DBConnection.selectQuery(query1).getString("accountId");
            int balanceAfter = Integer.parseInt(DBConnection.selectQuery(query1).getString("balance"));
            String query2 = "INSERT INTO transactionlogging (accountId, transactionType, balanceBefore, balanceAfter, date) "
                    + "VALUES ('" + accountId + "', '" + transactionType + "', '" + balanceBefore + "', '" + balanceAfter + "', '" + date + "')" ;
            DBConnection.updateQuery(query2);
        } catch (SQLException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    public synchronized ResultSet displayTransactioins(){
        try{
            String query1 = "SELECT accountId  from account where cardNumber =" + cardNumber;
            String accountIdFromAccount = DBConnection.selectQuery(query1).getString("accountId" );
            String queryTL =  "SELECT * FROM transactionLogging WHERE accountId = '" + accountIdFromAccount + "'";
            ResultSet rs = DBConnection.selectQueryReturnMultipleRows(queryTL);
            return rs;
        }catch (SQLException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public synchronized void deposit(String Amount) {

        if (Validation.isEmpty(Amount)) {
            JOptionPane.showMessageDialog(null, "You Cannot Leave This Field Empty!");
        } else if (!Validation.isOnlyNumbers(Amount)) {
            JOptionPane.showMessageDialog(null, "Only Numbers are Available !");
        } else {
            int amount = Integer.parseInt(Amount);
            if (amount > 10000) {
                JOptionPane.showMessageDialog(null, "That is More than the Limit !");
            } else {
                try {
                    String query1 = "select* from account where cardNumber=" + cardNumber;
                    int balance = Integer.parseInt(DBConnection.selectQuery(query1).getString("balance"));
                    int newBalance = balance + amount;
                    String query2 = "UPDATE account SET balance = " + newBalance + " WHERE cardNumber = " + cardNumber;
                    DBConnection.updateQuery(query2);
                    JOptionPane.showMessageDialog(null, "Deposit Occured Successfully");
                    this.transactionLogging("Deposit", balance, LocalDate.now());
                } catch (SQLException e) {
                    System.err.println("Database error occurred: " + e.getMessage());
                    e.printStackTrace();
                }

            }
        }
    }

    public synchronized int checkBalance() {
        try {
            String query = "SELECT balance from account WHERE cardNumber =" + cardNumber;
            int balance = Integer.parseInt(DBConnection.selectQuery(query).getString("balance"));
            return balance;

        } catch (SQLException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public synchronized void withdraw(String wAmount) {
        if (Validation.isEmpty(wAmount)) {
            JOptionPane.showMessageDialog(null, "You Cannot Leave This Field Empty!");
        } else if (!Validation.isOnlyNumbers(wAmount)) {
            JOptionPane.showMessageDialog(null, "Only Numbers are Available !");
        } else {
            int amount = Integer.parseInt(wAmount);
            if (amount > 10000) {
                JOptionPane.showMessageDialog(null, "That is More than the Limit !");
            } else {
                try {
                    String query = "SELECT * FROM account WHERE cardNumber = " + cardNumber;
                    int balance = Integer.parseInt(DBConnection.selectQuery(query).getString("balance"));
                    if (amount > balance) {
                        JOptionPane.showMessageDialog(null, "You Don't have Enough Money for this Transaction !");
                    } else {                        
                        int newBalance = balance - amount;
                        String query2 = "UPDATE account SET balance = " + newBalance + " WHERE cardNumber = " + cardNumber;
                        DBConnection.updateQuery(query2);
                        JOptionPane.showMessageDialog(null, "Withdrew Successfully :)");
                        this.transactionLogging("Withdraw", balance, LocalDate.now());
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public synchronized void changePIN(String newPIN, String confirmPIN) {
        String query1 = "SELECT cardPin FROM account WHERE cardNumber = " + cardNumber;
        try {
            String oldPIN = DBConnection.selectQuery(query1).getString("cardPin");
            if (Validation.isEmpty(newPIN + "") || Validation.isEmpty(confirmPIN + "")) {
                JOptionPane.showMessageDialog(null, "You cannot Leave any Field Empty!!");
            } else if (!Validation.isOnlyNumbers(newPIN + "") || !Validation.isOnlyNumbers(confirmPIN + "")) {
                JOptionPane.showMessageDialog(null, "Only Numbers are Allowed!");
            } else if (Integer.parseInt(newPIN) != Integer.parseInt(confirmPIN)) {
                JOptionPane.showMessageDialog(null, "New Pin is not the Same as the Confirm one !");
            } else if (Integer.parseInt(oldPIN) == Integer.parseInt(newPIN)) {
                JOptionPane.showMessageDialog(null, "New Password Cannot be the same as the Old Password !");
            } else {
                String query = "UPDATE account set cardPin = " + Integer.parseInt(newPIN) + " WHERE cardNumber = " + cardNumber;
                DBConnection.updateQuery(query);
                JOptionPane.showMessageDialog(null, "Pin Updated Successfully :)");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getCardNumber() {
        return cardNumber;
    }
}
