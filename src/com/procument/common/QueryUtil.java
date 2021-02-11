package com.procument.common;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.TransformerException;
import java.io.File;
import org.xml.sax.SAXException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.NodeList;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import javax.xml.transform.TransformerConfigurationException;

public class QueryUtil {

public static String Q(String id) throws Exception {
		
		//declare variables
		NodeList nodeList;
		Element element = null;
		
		//assigning nodeList
		nodeList = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new File(CommonConstants.QUERY_XML_FILE))
				.getElementsByTagName(CommonConstants.TAG_NAME);
		
		for (int x = 0; x < nodeList.getLength(); x++) {
			element = (Element) nodeList.item(x);
			if (element.getAttribute(CommonConstants.ELEMENT_ID).equals(id))
				break;
		}
		
		//return elements
		return element.getTextContent().trim();
	}
	
}
