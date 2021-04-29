package com.framework.utils;

import io.cucumber.java.Before;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class TestUtils {

    public HashMap<String, String> parseStringXML(InputStream is) throws Exception {
        HashMap<String, String> stringMap = new HashMap<String, String>();

        //Get document builder
        DocumentBuilderFactory docFac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuild = docFac.newDocumentBuilder();

        //Build document
        Document document = docBuild.parse(is);

        //Normalize XML structure.. Very important step
        document.getDocumentElement().normalize();

        //Find root node
        Element root = document.getDocumentElement();

        //Get all elements
        NodeList nodeList = document.getElementsByTagName("string");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                stringMap.put(eElement.getAttribute("name"), eElement.getTextContent());
            }
        }
        return stringMap;
    }


}
