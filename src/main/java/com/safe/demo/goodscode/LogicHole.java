package com.safe.demo.goodscode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class LogicHole {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据购物者的商品id和数量生成订单价格
     *
     * @param map
     */
    @PostMapping("/buildOrder")
    public String buildOrder(Map<Integer, Integer> map) {
        Double totalPrice = 0.00;
        for (Map.Entry<Integer, Integer> integerIntegerEntry : map.entrySet()) {
            Integer key = integerIntegerEntry.getKey();
            Integer value = integerIntegerEntry.getValue();
            Double price = getPrice(key);
            totalPrice += value * price;
        }
        return totalPrice.toString();
    }

    private Double getPrice(Integer productId) {
        return Double.valueOf(productId * 10);
    }

    @PostMapping("/login")
    public boolean login(@RequestParam(required = true) String username, @RequestParam(required = true) String password, @RequestParam(required = true) String code, HttpServletRequest request) {
        String authCode = (String) request.getSession().getAttribute("authCode");
        boolean validateOk = false;
        if (authCode != null) {
            if (code.equals(authCode)) {
                validateOk = true;
            }
            request.getSession().setAttribute("authCode", null);
        }
        if(validateOk) {
            return loginByUsernameAndPassword(username, password);
        }
        return false;

    }

    private boolean loginByUsernameAndPassword(String username, String password) {
        return true;
    }


    public boolean sendSMS(String phone, String code, HttpServletRequest request) {
        Long expire = redisTemplate.opsForValue().getOperations().getExpire(phone);
        if (expire != -2) {
            return false;
        }

        if (sendSmsCode(phone, code)) {
            redisTemplate.opsForValue().set(phone, 1, 30, TimeUnit.SECONDS);
        }
        return true;
    }

    private boolean interval(Long time, long time1) {
        return true;
    }

    private boolean sendSmsCode(String phone, String code) {
        try {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    public void countprice(){
//        int price = 1200;
//        int[] numbers = getInput();
//        int totalprice = 0;
//        boolean paramOK = true;
//        for (int i = 0; i < numbers.length; i++) {
//            int number = numbers[i];
//            if(!(number > 0 && number < 100)) {
//                paramOK = false;
//            }
//        }
//        if(paramOK){
//            for (int i = 0; i < numbers.length; i++) {
//                totalprice += numbers[i];
//            }
//        }
//
//    }
}
