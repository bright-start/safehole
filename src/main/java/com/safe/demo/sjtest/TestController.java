package com.safe.demo.sjtest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/testc")
public class TestController {
    @RequestMapping("/t1")
    public void t1(HttpServletRequest request,HttpServletResponse response){
        String val = request.getParameter("val");
        response.addHeader("Set-Cookie",val);

    }

    @RequestMapping("/t3")
    public String t3(HttpServletRequest request){
        String uniqueId = request.getParameter("uniqueId");
        String uploadDirectory = getUploadDirectory();
        uploadDirectory += toSaveFilePath(uniqueId);
        uploadDirectory = formatPath(uploadDirectory);
        File baseDir = new File(uploadDirectory);
        List list = new ArrayList<>();
        list.add(baseDir.getAbsolutePath());
        File[] files = baseDir.listFiles();
        if(files !=null && files.length > 0) {
            for (File file : files) {
                list.add(file.getName());
            }
        }
        return baseDir.getAbsolutePath();
    }

    private static String getUploadDirectory() {
        String path = System.getProperty("java.io.tmpdir"); //上传的先放到临时目录
        String pathname = path + "/crm_uploads/" + DateUtils.format(new Date(), "yyyyMMdd") + "/user/";
        return pathname;
    }
    private String toSaveFilePath(String el) {
        return el.replaceAll("[/|\\||\\*|\\?|\\|\"|<|>|\\|]", "");
    }

    public static String formatPath(String path) {
        if (!path.endsWith(File.separator)) {
            path = new StringBuilder(path).append(File.separator).toString();
        }
        return path;
    }

    @RequestMapping("/t2")
    public String t2(HttpServletRequest request) throws MalformedURLException {
        String temp = request.getParameter("paramName");
        if (temp != null){
            // 新增安全注入抵抗的代码
            temp = getSafeSql(temp);
            // 新增过滤入参，阻止XSS攻击
            temp = getSafeEncode(temp);
        }
        return temp == null ? "--" : temp;
    }
    public static String getSafeSql(String text) {
        // 替换单引号，即把所有单独出现的单引号改成两个单引号，防止攻击者修改SQL命令的含义
        String sql = StringEscapeUtils.escapeSql(text);
        // 禁止Oracle SQL中的注释传递
        sql = sql.replaceAll("--", "");
        sql = sql.replaceAll("/\\*", "");
        sql = sql.replaceAll("\\*/", "");
        if (false) {
            // 进行javascript的语法替换
            return StringEscapeUtils.escapeJavaScript(sql);
        } else {
            return sql;
        }
    }
    public static String getSafeEncode(String input) {
        // 20170612 - 改为使用Spring 框架的 HtmlUtils转义处理
        return input == null ? null : HtmlUtils.htmlEscape(input);
    }


    @GetMapping("/readfile")
    public void ReadFile(HttpServletRequest request) throws IOException {
        String sysTime = request.getParameter("sysTime");
        checkFilePathName(null, sysTime, "/,\\,".split(","));
        File file=new File("/Users/myhome/Desktop/");
        java.awt.Desktop.getDesktop().open(file);
    }
    private static final String[] CHECK_PATH = new String[]{"*","?",">","<","/windows/","/etc/",".."};

    public static void checkFilePathName(String s1,String pathName,String[] arr){
        if(!pathName.startsWith(s1)) {
            throw new RuntimeException("no ok");
        }
        if(s1 != null){
            if(!pathName.startsWith(s1)) {
                throw new RuntimeException("no ok");
            }
        }
        if(!(pathName.length() < 2000)) {
            throw new RuntimeException("no ok");
        }
        for (String s : CHECK_PATH) {
            if(pathName.contains(s)) {
                throw new RuntimeException("no ok");
            }
        }
        if(arr != null){
            for (String s : arr) {
                if(pathName.contains(s)) {
                    throw new RuntimeException("no ok");
                }
            }
        }
    }

    @GetMapping("/sess")
    public Object auth(HttpServletRequest request){

        HttpSession session = request.getSession(true);
        session.setAttribute("0x00","pppppp");
        session.getId();
        request.getCookies();


        ResponseEntity<Object> res = null;
        return res.getBody();
    }

    public static void main(String[] args) throws IOException {
        String[] strings = StringUtils.commaDelimitedListToStringArray("12=12,13114=12,1414");
        ArrayList pa = new ArrayList();
        for (int i =0;i<3;i++) {
//            System.out.println(string);
            if(!strings[i].contains("=")) {
                pa.set(pa.size() - 1, pa.get(pa.size() - 1) + "," + strings[i]);
            }else{
                pa.add(strings[i]);
            }
        }
        System.out.println(pa.size());
        System.out.println(pa);

        ResourceLoader resourceLoader = null;
        Resource resource = resourceLoader.getResource("http://www.baidu.com");


    }
}
