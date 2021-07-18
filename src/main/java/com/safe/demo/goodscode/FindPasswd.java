package com.safe.demo.goodscode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 找回密码无漏洞逻辑
 */
@RestController
public class FindPasswd {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 校验账号是否有效
     * @param username
     * @param request
     * @return
     */
    @PostMapping("fistVerify")
    public boolean fistVerify(String username, HttpServletRequest request){
        String sql = "select * from user where sname = ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, username);
        if(list != null && list.size() > 0){
            request.getSession().setAttribute("authUser",list.get(0).get("username"));
            return true;
        }else{
            return false;
        }

    }

    /**
     * 短信验证码验证账号身份
     * @param code
     * @param request
     * @return
     */
    @PostMapping("secVerify")
    public boolean secVerify(@RequestParam(required = true) String code, HttpServletRequest request){
        //短信验证码
        String authCode = (String) request.getSession().getAttribute("authCode");
        if(authCode.equals(code)){
            request.getSession().setAttribute("authOK",request.getSession().getAttribute("authUser"));
            return true;
        }
        return false;
    }

    /**
     * 身份验证通过后再认证身份关联行，认证通过后才可以修改密码
     * @param passwd
     * @param request
     * @return
     */
    @PostMapping("thirdVerify")
    public boolean thirdVerify(@RequestParam(required = true)String passwd, HttpServletRequest request){
        String authOK = (String) request.getSession().getAttribute("authOK");
        if(authOK != null) {
            if (request.getSession().getAttribute("authOK").equals(request.getSession().getAttribute("authUser"))) {
                String authUser = (String) request.getSession().getAttribute("authUser");
                String sql = "update user passwd set ? where sname = ?";
                int succ = jdbcTemplate.update(sql, passwd, authUser);
                if (succ == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
