package com.safe.demo.goodscode;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 统计学PRING和密码学PRING
 */
public class RandomPK {

    /**
     * 不安全的随机数编码（统计学PRING）
     */
    public static void GenerateReceiptURL(){
        Set<Integer> set = new HashSet<>();
        Random ranGen1 = new Random();
        ranGen1.setSeed((new Date()).getTime());
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 1000; j++) {
//            System.out.println(ranGen1.nextInt(400000000) + ".html");
                set.add(ranGen1.nextInt(400000000));
            }
            System.out.println(set.size());
            set.clear();
        }



    }

    /**
     * 安全的随机数编码（密码学PRING）
     */
    public static void rand() throws NoSuchAlgorithmException {
        SecureRandom random1 = SecureRandom.getInstance("SHA1PRNG");
        SecureRandom random2 = SecureRandom.getInstance("SHA1PRNG");
        for (int i = 0; i < 100; i++) {
            System.out.println(random1.nextInt() + " != " + random2.nextInt());
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        GenerateReceiptURL();
        rand();
    }
}
