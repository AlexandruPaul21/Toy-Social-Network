package com.example.map_toysocialnetwork_javafx.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Secure {
    public static String fromByteToStringHex(byte[] bytes) {
        StringBuilder bobTheBuilder = new StringBuilder();

        for (byte byt : bytes) {
            bobTheBuilder.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }

        return bobTheBuilder.toString();
    }

    public static String getHashedPassword(String password, byte[] salt) {
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());

            hashedPassword = fromByteToStringHex(bytes);

        } catch (NoSuchAlgorithmException ns) {
            ns.printStackTrace();
        }
        return hashedPassword;
    }

    public static byte[] getSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    public static byte[] getSaltFromHex(String hexSalt) {
        byte[] ans = new byte[hexSalt.length() / 2];

        for (int i = 0; i < ans.length; i++) {
            int index = i * 2;

            int val = Integer.parseInt(hexSalt.substring(index, index + 2), 16);
            ans[i] = (byte)val;
        }

        return ans;
    }

    public static void main(String[] args) {
        byte[] salt = getSalt();

        String password = "1234";

        System.out.println(getHashedPassword(password, salt)); //64 chars for password

        System.out.println(fromByteToStringHex(salt)); //32 chars

        String stringSalt = fromByteToStringHex(salt);

        System.out.println(Arrays.toString(salt));
        System.out.println(Arrays.toString(getSaltFromHex(stringSalt)));
    }
}
