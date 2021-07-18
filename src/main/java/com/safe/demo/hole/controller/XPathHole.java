package com.safe.demo.hole.controller;

import org.owasp.esapi.ESAPI;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/xpath")
public class XPathHole {

    /**
     * 正常请求：
     * /xpath/attack?username=test&password=test-pass
     *
     * poc:
     * /xpath/attack?username=' or 1=1 or ''='&password=admin
     */
    @GetMapping("/attack")
    private static List<String> attack(String username, String password) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

        URL xmlPath = Thread.currentThread().getContextClassLoader().getResource("xml/users.xml");

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(xmlPath.getPath());

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr =
                xpath.compile("//users/user[username/text()='" + username + "' and password/text()='" + password + "' ]");
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;

        // Print first names to the console
        List<String> list= new LinkedList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node =
                    nodes.item(i).getChildNodes().item(1).
                            getChildNodes().item(0);
            list.add(node.getNodeValue());
            System.out.println("Authenticated: " + node.getNodeValue());
        }

        return list;
    }

    @GetMapping("/protect")
    private static List<String> protect(String username, String password) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        username = ESAPI.encoder().encodeForXPath(username);
        password = ESAPI.encoder().encodeForXPath(password);
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        URL xmlPath = Thread.currentThread().getContextClassLoader().getResource("xml/users.xml");;
        Document doc = builder.parse(xmlPath.getPath());

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr =
                xpath.compile("//users/user[username/text()='" + username + "' and password/text()='" + password + "' ]");
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;

        // Print first names to the console
        List<String> list= new LinkedList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node =
                    nodes.item(i).getChildNodes().item(1).
                            getChildNodes().item(0);
            list.add(node.getNodeValue());
            System.out.println("Authenticated: " + node.getNodeValue());
        }

        return list;
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        attack("admin","admin");
//        attack("' or 1=1 or ''='","admin");
    }
}
