package com.safe.demo.hole.controller;

import org.owasp.esapi.ESAPI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.DriverManager;
import java.util.regex.Pattern;

@Controller
public class XSS {


    /**
     * 不安全的编码
     * 输出点在springboot页面
     *
     * /xss?val=val=<script>prompt(1)</script>
     *
     * /xss?val=val=<script>prompt(document.cookie)</script>
     */
    @GetMapping("/xss")
    @ResponseBody
    public String testXssHtml(HttpServletResponse response,String val){

        //设置cookie
        Cookie cookie = new Cookie("token","this_is_nsafe_cookie_when_you_watch_it.");
        cookie.setMaxAge(30);
        cookie.setPath("/xss");
        response.addCookie(cookie);

        return val;
    }


    /**
     *  不安全编码形式（thymeleaf引擎形式）
     *
     *  能预防的XSS形式（输出点在标签内）
     *  http://localhost:8081/xss2?url=%3Cscript%3Ealert(1)%3C/script%3E
     *
     *  不能预防的XSS形式（输出点在href）
     *  http://localhost:8081/xss2?url=javascript:alert(1)
     *
     */
    @GetMapping("/xss2")
    public String testXssTemplateHtml(Model model,String url){
        model.addAttribute("url", url);
        return "xss";
    }

    /**
     * 不安全编码形式（springboot页面输出形式）
     * http://localhost:8081/xss4?url=%3Cscript%3Ealert(1)%3C/script%3E
     */
    @GetMapping("/xss4")
    public void testXss(String url, HttpServletResponse response) throws IOException {
        String str = String.valueOf(url);
        PrintWriter writer = response.getWriter();
        writer.print(str);
    }

    /**
     * 反射性xss安全编码
     * http://localhost:8081/xss3?val=%3Cscript%3Ealert(1)%3C/script%3E
     */
    @GetMapping("/xss3")
    @ResponseBody
    public String xss(String val){
        val = URLEncoder.encode(val);
//        val = cleanXSS(val);
        ESAPI.encoder().encodeForJavaScript(val);
        return val;
    }

    /**
     * ESAPI预防XSS注入代码
     */
    private String cleanXSS(String value) {
        if (value != null) {
            // 推荐使用ESAPI库来避免脚本攻击，前端如果传递url编码过的中文就会导致乱码
            value = ESAPI.encoder().canonicalize(value);

            // 避免空字符串
            value = value.replaceAll("\\s", "");

            // 避免script 标签
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // 避免src形式的表达式
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // 删除单个的 </script> 标签
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // 删除单个的<script ...> 标签
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // 避免 eval(...) 形式表达式
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // 避免 e­xpression(...) 表达式
            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // 避免 javascript: 表达式
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // 避免 vbscript: 表达式
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // 避免 onXX= 表达式
            scriptPattern = Pattern.compile("on.*(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

        }
        return value;
    }

    public static void main(String[] args) {
        String key = "<script>alert(/1/)</script>";
        ESAPI.encoder().encodeForHTML(key);
        System.out.println(key);
    }
}
