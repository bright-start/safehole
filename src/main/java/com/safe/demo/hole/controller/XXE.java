package com.safe.demo.hole.controller;

import com.safe.demo.hole.utils.WebUtil;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;

import org.dom4j.io.SAXReader;

@RestController
public class XXE {
    private Logger logger = LoggerFactory.getLogger(XXE.class);

    /**
     * jdk8
     * tomcat9传递标准xxe payload会引起错误
     * tomcat8.5触发xxe
     *
     * payload:
     *  <?xml version="1.0" encoding="utf-8"?>
     *  <!DOCTYPE creds [
     *         <!ELEMENT creds ANY >
     *         <!ENTITY xxe SYSTEM "file:///etc/passwd">
     *         ]>
     *  <creds>
     *     &xxe;
     *  </creds>
     */
    @RequestMapping("/xxe")
    public String xxe(HttpServletRequest request) {
        try {

            String body = WebUtil.getRequestBody(request);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(body);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);  // parse xml

            // 遍历xml节点name和value
            StringBuffer buf = new StringBuffer();
            NodeList rootNodeList = document.getChildNodes();
            for (int i = 0; i < rootNodeList.getLength(); i++) {
                Node rootNode = rootNodeList.item(i);
                NodeList child = rootNode.getChildNodes();
                for (int j = 0; j < child.getLength(); j++) {
                    Node node = child.item(j);
                    buf.append(node.getNodeName() + ": " + node.getTextContent() + "\n");
                }
            }
            sr.close();
            System.out.println(buf.toString());
            return buf.toString();
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }

    /**
     * <user>
     * <username>admin</username>
     * <password>admin</password>
     * <role>admin</role>
     * </user>
     * <user>
     * <username>tt</username>
     * <password>tt</password>
     * <role>test</role>
     * </user>
     *
     * url:
     * /xml?uname=tt&upass=tt
     *
     * poc
     * /xml?uname=tat</username><password>tat</password><role>admin</role><user><username>tt</username><password>&upass=tt
     *
     */
    @GetMapping("/xml")
    public String xmlInject(String uname, String upass) {

        try {
            URL xmlPath = Thread.currentThread().getContextClassLoader().getResource("xml/users.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlPath.getPath());

            Element user = doc.createElement("userM");

            Element username = doc.createElement("username");
            Text name = doc.createTextNode(uname.toString());
            username.appendChild(name);

            Element password = doc.createElement("password");
            Text passwd = doc.createTextNode(upass);
            password.appendChild(passwd);

            Element role = doc.createElement("role");
            Text roleText = doc.createTextNode("comm");
            role.appendChild(roleText);

            user.appendChild(username);
            user.appendChild(password);
            user.appendChild(role);

            doc.getDocumentElement().appendChild(user);

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer;

            transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            File file = new java.io.File(xmlPath.getPath());
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

        } catch (Exception e) {
            logger.warn(e.getMessage());
            return e.getMessage();
        } finally {
            return "succ";
        }
    }


    /**
     * DocumentBuilder下禁用外部实体的方法
     */
    private static void XXE1() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        禁用外部实体
//        factory.setExpandEntityReferences(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        URL xmlPath = Thread.currentThread().getContextClassLoader().getResource("xml/users.xml");
        Document doc = builder.parse(xmlPath.getPath());
        Element root = doc.getDocumentElement();
        System.out.println(root.getFirstChild().getNodeValue());
    }

    /**
     * SAXReader下禁用外部实体的方法
     */
    private static void XXE2() throws DocumentException, SAXException {
        URL xmlPath = Thread.currentThread().getContextClassLoader().getResource("xml/users.xml");
        SAXReader sax=new SAXReader();//创建一个SAXReader对象

        //xxe预防
        sax.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        sax.setFeature("http://xml.org/sax/features/external-general-entities", false);
        sax.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        org.dom4j.Document document = sax.read(xmlPath.getPath());//获取document对象,如果文档无节点，则会抛出Exception提前结束
        org.dom4j.Element root = document.getRootElement();
//        System.out.println(root.getTextTrim());
//        List rowList = root2.selectNodes("//creds");
//        Iterator<?> iter1 = rowList.iterator();
//        if (iter1.hasNext()) {
//            org.dom4j.Element beanNode = (org.dom4j.Element) iter1.next();
//            System.out.println(beanNode.getTextTrim());
//        }
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, DocumentException {
        XXE1();
//        XXE2();
//        xmlInject();
    }
}
