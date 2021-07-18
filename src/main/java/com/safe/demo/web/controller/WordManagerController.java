package com.safe.demo.web.controller;

import com.alibaba.fastjson.JSON;
import com.safe.demo.web.pojo.WordTemplate;
import com.safe.demo.web.service.WordManagerService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: white_xiaosheng
 * @Description: TODO
 * @CreateTime: 2021/3/4 8:51 下午
 * @Version 1.0
 */
@Api("word报告管理")
@RestController
@RequestMapping("/word")
public class WordManagerController {

    @Autowired
    private WordManagerService wordManagerService;


    /**
     * word模板
     *      doc:HWPFDocument
     *      docx:POIXMLDocument.openPackage
     */
    @ApiOperation("通过word模板生成word")
    @GetMapping("/create")
    public void wordCreateByTemplate(HttpServletResponse response, int word_template_id) throws IOException, TemplateException {
        WordTemplate wordTemplate = wordManagerService.wordDateMake(word_template_id);
        AssertException(wordTemplate);

//        download(response, wordTemplate);
        download2(response, wordTemplate);
    }


    private void download(HttpServletResponse response, WordTemplate wordTemplate) throws IOException, TemplateException {
        String templateDate = wordTemplate.getTemplateDate();
        Map<String, String> map = (Map<String, String>) JSON.parse(templateDate);


        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        String templateDir = this.getClass().getClassLoader().getResource("customtemplate").getPath();
        configuration.setDirectoryForTemplateLoading(new File(templateDir));

        Template template = configuration.getTemplate("paper_template.ftl", "utf-8");
        File tempDoc = new File(templateDir + File.separator + wordTemplate.getWordName());
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempDoc), StandardCharsets.UTF_8), 10240);
        template.process(map, out);
        out.close();


        //导出到文件
        try {

            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("customtemplate/" + wordTemplate.getWordName());

            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + wordTemplate.getWordName());
            response.addHeader("Content-Length", "" + inputStream.available());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

            response.setContentType("application/octet-stream");
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            toClient.write(bytes);
            toClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tempDoc.exists()) {
                tempDoc.delete();
            }
        }
    }

    private void download2(HttpServletResponse response, WordTemplate wordTemplate) {
        String templateDate = wordTemplate.getTemplateDate();
        Map<String, String> map = (Map<String, String>) JSON.parse(templateDate);

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("customtemplate/paper_template.doc");
        HWPFDocument document = null;
        try {
            document = new HWPFDocument(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 读取文本内容
        Range bodyRange = document.getRange();
        // 替换内容
        for (Map.Entry<String, String> entry : map.entrySet()) {

            bodyRange.replaceText("${" + entry.getKey() + "}", entry.getValue());
        }

        //导出到文件
        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.write(byteArrayOutputStream);

            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + wordTemplate.getWordName());
            response.addHeader("Content-Length", "" + byteArrayOutputStream.size());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

            response.setContentType("application/octet-stream");
            toClient.write(byteArrayOutputStream.toByteArray());
            toClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void AssertException(WordTemplate wordTemplate) {
        if (wordTemplate.getWordName() == null) {
            throw new RuntimeException("查询数据WordName为空");
        }
        if (wordTemplate.getTemplateDate() == null) {
            throw new RuntimeException("查询数据TemplateDate为空");
        }
    }

    @GetMapping("/makeData")
    public String MakeData() throws FileNotFoundException {
        Map<String, String> map = new HashMap<>();
        map.put("compangy", "梦想合资");
        map.put("project", "白皮书");
        map.put("date", (new Date()).toString());
        map.put("organization", "梦想合资监制");
        map.put("summary", "这是一家有梦想的公司");
        map.put("version", "v1.0.0");
        map.put("author", "召唤师");
        map.put("group", "运营部");
//        Map<String,Object> pic = new HashMap<String, Object>();
//        pic.put("width", 100);
//        pic.put("height", 100);
//        pic.put("type", "png");
//        String picPath = this.getClass().getClassLoader().getResource("customtemplate/pic.png").getPath();
//        pic.put("content", inputStream2ByteArray(new FileInputStream(picPath),true));
//
//        map.put("pic",pic);
        String s = JSON.toJSONString(map);
        WordTemplate wordTemplate = new WordTemplate();
        wordTemplate.setTemplateDate(s);
        wordTemplate.setWordName("mshz.docx");

        int i = wordManagerService.makeData(wordTemplate);

        return i == 1 ? "SUCCESS" : "FAIL";

    }

    public static byte[] inputStream2ByteArray(InputStream in,boolean isClose){
        byte[] byteArray = null;
        try {
            int total = in.available();
            byteArray = new byte[total];
            in.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(isClose){
                try {
                    in.close();
                } catch (Exception e2) {
                    System.out.println("关闭流失败");
                }
            }
        }
        return byteArray;
    }
}
