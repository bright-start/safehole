package com.safe.demo.hole.controller;

import com.safe.demo.hole.socket.WebSocketServer;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.io.*;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class FileSourceHole {

    public static final HashMap<String, String> mFileTypes = new HashMap<String, String>();

    static {
        // images
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E47", "png");
        mFileTypes.put("47494638", "gif");
        mFileTypes.put("49492A00", "tif");
        mFileTypes.put("424D", "bmp");
        //
        mFileTypes.put("41433130", "dwg"); // CAD
        mFileTypes.put("38425053", "psd");
        mFileTypes.put("7B5C727466", "rtf"); // 日记本
        mFileTypes.put("3C3F786D6C", "xml");
        mFileTypes.put("68746D6C3E", "html");
        mFileTypes.put("44656C69766572792D646174653A", "eml"); // 邮件
        mFileTypes.put("D0CF11E0", "doc");
        mFileTypes.put("5374616E64617264204A", "mdb");
        mFileTypes.put("252150532D41646F6265", "ps");
        mFileTypes.put("255044462D312E", "pdf");
        mFileTypes.put("504B0304", "docx");
        mFileTypes.put("52617221", "rar");
        mFileTypes.put("57415645", "wav");
        mFileTypes.put("41564920", "avi");
        mFileTypes.put("2E524D46", "rm");
        mFileTypes.put("000001BA", "mpg");
        mFileTypes.put("000001B3", "mpg");
        mFileTypes.put("6D6F6F76", "mov");
        mFileTypes.put("3026B2758E66CF11", "asf");
        mFileTypes.put("4D546864", "mid");
        mFileTypes.put("1F8B08", "gz");
        mFileTypes.put("4D5A9000", "exe/dll");
        mFileTypes.put("75736167", "txt");
    }

    /**
     * payload:
     *
     */
    @PostMapping("/upload")
    @CrossOrigin
    public Map<String, String> Upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + File.separator + "safehole_upload";

        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdir();
        }
        System.out.println(filePath);
        String fileHeader = mFileTypes.get(getFileHeader(file.getInputStream()));
        if (fileHeader == "") {
            throw new FileUploadException("上传错误");
        }
        if (!fileHeader.equals(fileName.substring(fileName.lastIndexOf(".") + 1))) {
            throw new FileUploadException("上传错误");
        }
        String newFilePath = filePath + File.separator + UUID.randomUUID().toString().replace("-", "") + "." + fileHeader;
        File newFile = new File(newFilePath);
        newFile.setExecutable(false);
//        File newFile = new File(filePath+ File.separator+fileName);
        try {
            InputStream inputStream = file.getInputStream();
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            file.transferTo(newFile);
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", newFile.getName());
            map.put("data", Base64.getEncoder().encodeToString(bytes));
            return map;
        } catch (Exception e) {
            throw new FileUploadException("上传错误");
        }
    }

    public String getFileHeader(InputStream is) throws IOException {
        String value = null;
        byte[] b = new byte[4];
        /*
         * int read() 从此输入流中读取一个数据字节。 int read(byte[] b) 从此输入流中将最多 b.length
         * 个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len)
         * 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
         */
        is.read(b, 0, b.length);
        value = bytesToHexString(b);
        return value;
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        System.out.println(builder.toString());
        return builder.toString();
    }


    /**
     *
     */
    @GetMapping("/down")
    public void download(HttpServletRequest request, HttpServletResponse response, String name) throws IOException, InterruptedException {
        String filePath = System.getProperty("user.dir") + File.separator + "safehole_upload";
        File downPath = new File(filePath);
        if (!downPath.exists()) {
            downPath.mkdirs();
        }
        System.out.println(filePath);

        name = name.replace(".","");
        name = name.replace("\\","");
        name = name.replace("/","");

        String downFilePath = filePath + File.separator + name;

        String filename = new String(downFilePath.getBytes("UTF-8"), "iso-8859-1");
        InputStream fis = new BufferedInputStream(new FileInputStream(filename));

        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename=" + name);
        response.addHeader("Content-Length", "" + new File(filename).length());
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");

        byte[] buffer = new byte[1024];
        long file_size = fis.available();
        long pos = 0;
        int len = 0;
        while ((len = fis.read(buffer)) != -1) {

            pos += len;
            WebSocketServer.sendInfo(String.valueOf(pos * 100 / file_size) + "%", name);
            toClient.write(buffer, 0, len);
            Thread.sleep(10);
        }
        fis.close();
        toClient.flush();
        toClient.close();
    }

    /**
     * poc：
     * /down_attack?name=../../a4702f7f41754421b6328196b8afd921.png
     *
     * /down_attack?name=../../safehole/src/main/resources/application.properties
     *
     * /down_attack?name=../../../../../../../../../../../../../etc/passwd
     */
    @GetMapping("/down_attack")
    public void down_attack(HttpServletRequest request, HttpServletResponse response, String name) throws Exception {
        String filePath = System.getProperty("user.dir") + File.separator + "safehole_upload";
        File file = new File(filePath + File.separator + "/down/file/");
        if (!file.exists()) {
            file.mkdirs();
        }

        File downfile = new File(filePath + File.separator + "/down/file/"+name);
        exportFile(response, downfile, name);
    }

    public void exportFile(HttpServletResponse response, File file, String outFileName) throws
            Exception {
        InputStream in = null;
        try {
            in = new FileInputStream(file.getPath());
            exportFile(response, in, outFileName);
        } catch (Exception e) {
            throw e;
        } finally {  /* 发生异常时，会导致文件并没有删掉 */

        }
    }

    public static void exportFile(HttpServletResponse response, InputStream in, String outFileName) throws
            IOException {
        String filename = URLEncoder.encode(outFileName, "UTF-8");
        OutputStream out = null;
        //对空格进行替换
        filename = filename.replaceAll("\\+", "%20");
        //对+号进行替换,Chrome无法转义带有%2B
        filename = filename.replaceAll("%2B", "+");
        //对(号进行替换,Chrome无法转义带有%28
        filename = filename.replaceAll("%28", "(");
        //对)号进行替换,Chrome无法转义带有%29
        filename = filename.replaceAll("%29", ")");
        // 定义输出类型(下载)
        response.setContentType("application/force-download");
        response.setHeader("Location", filename);
        // 定义输出文件头
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        out = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int i = -1;
        while ((i = in.read(buffer)) != -1) {
            out.write(buffer, 0, i);
        }
        in.close();
        out.close();
    }


    public static void main(String[] args) throws Exception {
        String path = "/Users/myhome/myresources/ideaproject/safe/saft/src/main/java/com/example/demo/hole/controller/../";
        if (path.indexOf("../") != -1) {
            throw new Exception("目录遍历错误");
        }
        File file = new File(path);
        for (File listFile : file.listFiles()) {
            System.out.println(listFile.getName());
        }
    }
}
