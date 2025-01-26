/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parallel.processing;

/**
 *
 * @author roaam
 */
public class Validation {
    public static boolean isValidString(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLetterOrDigit(s.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(String s) {
        return s.trim().equals("");
    }
    public static boolean isOnlyNumbers(String number) {
        for (int i = 0; i < number.length(); i++) {
            if (!Character.isDigit(number.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
