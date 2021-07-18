package com.safe.demo.hole.utils;

public class Converty {
    public static String byteConvertHexString(byte[] data) {
        StringBuffer result = new StringBuffer();
        String hexString = "";
        for (byte b : data) {
            hexString = Integer.toHexString(b & 255);
            result.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return result.toString().toUpperCase();
    }

    public static byte[] hexStringConvertBytes(String data) {
        int length = data.length() / 2;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            int first = Integer.parseInt(data.substring(i * 2, i * 2 + 1), 16);
            int second = Integer.parseInt(data.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (first * 16 + second);
        }
        return result;
    }
}
