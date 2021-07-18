package com.safe.demo.hole.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

@RestController
public class SSRF {

    /**
     *
     * poc：
     * /ssrf_attack?url=file:///etc/passwd
     *
     * /ssrf_attack?url=gopher://192.168.0.119:6666/_abc
     */
    @GetMapping("/ssrf_attack")
    public String ssrf_attack(String url) {
        try {

            URL u = new URL(url);

            URLConnection urlConnection = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); //send request
            String inputLine;
            StringBuilder html = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }
            in.close();
            return html.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     *
     */
    @GetMapping("/ssrf_protect")
    public String ssrf_protect(String url) {
        try {

            URL u = new URL(url);

            if(u.getHost() != null && u.getHost() != ""){
                return "host is incorrect";
            }
            if(!internalIp(u.getHost().getBytes())){
                return "this IP is inner ip";
            }

            String protocol = u.getProtocol();
            if("http".equals(protocol) || "https".equals(protocol)){
                return "protocol only Support http and https";
            }


            Integer port = u.getPort();
            List list = Arrays.asList(80,443,8080,8090);
            if(port == null || !list.contains(port)){
                return "port not visit";
            }

            URLConnection urlConnection = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); //send request
            String inputLine;
            StringBuilder html = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }
            in.close();
            return html.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }



    /**
     * 判断 127.0.0.0/8 和0.0.0.0/8
     */
    public static boolean internalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        //10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        //172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                switch (b1) {
                    case SECTION_6:
                        return true;
                }
            default:
                return false;
        }
    }

}

