package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class KeyManager {
    private static Scanner scanner = new Scanner(System.in);

    private static String generateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String mainKeyInHash() {
        return "801ab523c8b00b4e96e05c67096d65191b1e7935b9aa1316954f218037e6adbd";
    }

    public static String getInputKey() {
        System.out.print("Enter key: ");
        return scanner.nextLine();
    }

    public static boolean checkKey() throws NoSuchAlgorithmException {
        String hashedMainKey = mainKeyInHash();
        String userInputKey = getInputKey();

        String hashedUserInputKey = generateHash(userInputKey);
//        String hashedMainKey = generateHash(mainKey);
//        System.out.println(hashedMainKey);
//        System.out.println(hashedUserInputKey);

        if (hashedUserInputKey.equals(hashedMainKey)) {
            System.out.println("Key is correct!");
            return  true;
        } else {
            System.out.println("Wrong key!");
            return false;
        }
    }
}