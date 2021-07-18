package com.safe.demo.goodscode;

import com.alibaba.druid.util.Base64;
import com.safe.demo.hole.utils.AesUtil;
import com.safe.demo.hole.utils.Converty;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Passwd {
    public static void main(String[] args) throws Exception {
//        String password = "root";

//        MessageDigest messageDigest = MessageDigest.getInstance("sha-256");
//        messageDigest.update(password.getBytes("UTF-8"));
//        byte[] digest = messageDigest.digest();
//        System.out.println(Converty.byteConvertHexString(digest));

        /**
         * 安全随机数加密过程
         */
        String password = "root";
        byte[] encrypt1 = AesUtil.encryptRand(password.getBytes());
        System.out.println(Base64.byteArrayToBase64(encrypt1));

        byte[] bytes = AesUtil.decryptRand(encrypt1);
        System.out.println(new String(bytes, StandardCharsets.UTF_8));


        /**
         * 不安全随机数加密过程
         */
        byte[] encrypt = AesUtil.encrypt(password.getBytes());
        String s = Converty.byteConvertHexString(encrypt);
        System.out.println(s);


        byte[] bytes2 = AesUtil.decrypt(encrypt);
        System.out.println(new String(bytes2));

    }

}
