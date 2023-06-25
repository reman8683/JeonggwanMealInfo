package com.reman8683.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class ReadXML {
    /**
     * @param xml [String] Xml
     * @return [Map&lt;String, String&gt;] Map Xml data
     */
    public Map<String, String> convertXMLToMap(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true); // 콘텐츠를 무시하도록 설정

            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xml));
            Document document = builder.parse(inputSource);
            Element rootElement = document.getDocumentElement();
            return parseElement(rootElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, String> parseElement(Element element) {
        Map<String, String> resultMap = new HashMap<>();
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                String tagName = childElement.getTagName();
                String textContent = childElement.getTextContent();
                resultMap.put(tagName, textContent);
            }
        }
        return resultMap;
    }
}
