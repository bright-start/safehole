package com.safe.demo.goodscode;

/**
 * 安全编码
 * 防止类的循环初始化
 */
public class Cycle {
    private final int balance;
    private static final Cycle c = new Cycle();
    // Random deposit
    private static final int deposit = (int) (Math.random() * 100);

    public Cycle() {
        balance = deposit - 10;
    }

    public static void main(String[] args) {
        System.out.println("The account balance is: " + c.balance);
    }
}

