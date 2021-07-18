package com.safe.demo.hole.controller;

import com.safe.demo.hole.pojo.UserM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 越权代码模拟
 */
@RestController
public class UltraViresLoopholes {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 水平越权
     *
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/findInfo")
    public Map<String, Object> findInfo(String userId, HttpServletRequest request) {
        String roleId = (String) request.getSession().getAttribute("roleId");
        String sql = "select * from user where sid = ? and role_id = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, userId, roleId);
        if (maps != null && maps.size() > 0) {
            return maps.get(0);
        }
        return null;
    }

    /**
     * 垂直越权
     *
     * @param userM
     * @param request
     * @return
     */
    @PostMapping("/create_admin")
    public boolean createAdmin(UserM userM, HttpServletRequest request) {
        String roleName = (String) request.getSession().getAttribute("roleName");
        if (roleName != "admin") {
            return false;
        }
        String sql = "insert into admin(username,password,roleId) value(?,?,1)";
        int succ = jdbcTemplate.update(sql, userM.getUsername(), userM.getPassword());
        if (succ == 1) {
            return true;
        }
        return false;
    }

    /**
     * csrf漏洞
     *
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/csrf_change_money_attack")
    public String changeMoney(String id, String money, String token, HttpServletRequest request) {
        UserM userM = (UserM) request.getSession().getAttribute("user");
        String sql = "update from wallet set money-= ? where id = ?";
        String sql2 = "update from wallet set money+= ? where id = ?";
        jdbcTemplate.update(sql, money, userM.getId());
        jdbcTemplate.update(sql2, money, id);

       request.getSession().invalidate();
       return "change_money_success";

    }

    @PostMapping("/csrf_change_money_protect")
    public String csrf_change_money_protect(String id, String money, String token, HttpServletRequest request) {
        UserM userM = (UserM) request.getSession().getAttribute("user");
        String csrfToken = (String) request.getSession().getAttribute("csrfToken");
        if (token == null || !token.equals(csrfToken)) {
            return "csrf token is invaild";
        }
        String sql = "update from wallet set money-= ? where id = ?";
        String sql2 = "update from wallet set money+= ? where id = ?";
        jdbcTemplate.update(sql, money, userM.getId());
        jdbcTemplate.update(sql2, money, id);

        request.getSession().invalidate();
        return "change_money_success";

    }


}
