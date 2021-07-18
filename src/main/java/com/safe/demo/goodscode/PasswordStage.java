package com.safe.demo.goodscode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 密码强度策略
 * 正则存在些许问题，不够严谨
 *
 *
 * 正向先行断言 (?= )
 *
 * 在符号“(?=” 和 “)” 之间加入一个表达式，它就是一个先行断言，用以说明圆括号内的表达式必须正确匹配。
 */
public class PasswordStage {
    public static void main(String[] args) {
        String[] stage = {
                "^[0-9]{6,8}$|^[a-zA-Z]{6,8}$",
                "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,12}$",
                "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&*])[\\da-zA-Z~!@#$%^&*]{12,16}$",
        };
        String password = "a12345678905!aA";
//        for (int i = 0; i < 3; i++) {
//            if(findStage(stage[i],password)){
//                System.out.println(i);
//            }
//        }
        String rule = "^.*(?=.{6,16})(?=.*\\d)(?=.*[A-Z]{1,})(?=.*[a-z]{1,})(?=.*[!@#$%^&*?\\(\\)]).*$";
        boolean stage1 = findStage(rule, password);
        System.out.println(stage1);

    }

    private static boolean findStage(String step,String password) {
        Pattern compile = Pattern.compile(step);
        Matcher matcher = compile.matcher(password);
        if(matcher.matches()){
            return true;
        }
        return false;
    }

}
