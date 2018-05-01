package org.hummer.core.util;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

//import com.sun.org.apache.xpath.internal.XPathAPI;

/**
 * Description:
 *
 * @author Jeff.Zhou Date: 2004-10-28 13:04:14
 * @version $Id: $
 */
public class XmlDOMUtil {
    /**
     * parser by file(string)
     *
     * @param strfile
     * @return
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public static org.dom4j.Document parse(String strfile) throws ParserConfigurationException, SAXException,
            IOException {

        org.dom4j.Document ret = null;

        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(strfile);
            ret = dom4jParse(strfile, is);
        } catch (Exception e1) {
            Log4jUtils.getLogger(XmlDOMUtil.class).warn(
                    strfile + " is not in the classpath! please check your runtime classpath");
        }

        return ret;

    }

    /**
     * parser by file(string)
     *
     * @param strfile
     * @return
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public static org.dom4j.Document dom4jParse(String strfile) throws ParserConfigurationException, SAXException,
            IOException {

        org.dom4j.Document ret = null;

        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(strfile);
            int length = is.available();
            byte[] contentByte = new byte[length];
            is.read(contentByte);
            String text = new String(contentByte);
            ret = org.dom4j.DocumentHelper.parseText(text);
        } catch (Exception e1) {
            Log4jUtils.getLogger(XmlDOMUtil.class).warn(
                    strfile + " is not in the classpath! please check your runtime classpath");
        }

        return ret;

    }

    /**
     * creatDOM by InputStream Object
     *
     * @param file
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static org.dom4j.Document dom4jParse(String strfile, InputStream is) throws ParserConfigurationException,
            SAXException, IOException {
        org.dom4j.Document ret = null;

        try {
            int length = is.available();
            byte[] contentByte = new byte[length];
            is.read(contentByte);
            String text = new String(contentByte);
            ret = org.dom4j.DocumentHelper.parseText(text);
        } catch (Exception e1) {
            Log4jUtils.getLogger(XmlDOMUtil.class).warn(
                    strfile + " is not in the classpath! please check your runtime classpath");
        }

        return ret;
    }

    /**
     * creatDOM by File Object
     *
     * @param file
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document parse(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);

        return doc;
    }

    /**
     * creat a Node by XML string
     *
     * @param strXML
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws javax.xml.transform.TransformerException
     *
     */
//	public static Node getNode(String strXML) throws ParserConfigurationException, SAXException, IOException,
//			TransformerException {
//		NodeList nodelist = getNodeList(strXML);
//		Node node = null;
//
//		if ((nodelist != null) && (nodelist.getLength() > 0)) {
//			node = nodelist.item(0);
//		}
//
//		return node;
//	}

    /**
     * creat a NodeList by xml string
     *
     * @param strXML
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
//	public static NodeList getNodeList(String strXML) throws ParserConfigurationException, SAXException, IOException,
//			TransformerException {
//		strXML = "<xml>\n" + strXML + "\n</xml>";
//
//		Document doc = getDocument(strXML);
//		NodeList nodelist = null;
//		nodelist = XPathAPI.selectNodeList(doc, "/xml/*");
//
//		return nodelist;
//	}

    /**
     * creat DOM by string content
     *
     * @param strContent
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document getDocument(String strContent) throws ParserConfigurationException, SAXException,
            IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);

        DocumentBuilder db = dbf.newDocumentBuilder();

        return db.parse(new InputSource(new StringReader(strContent)));
    }

    /**
     * query NodeList in the specify node under a XPath.
     *
     * @param node
     * @param strXPath
     * @return
     * @throws TransformerException
     */
//	public static NodeList queryNodeList(Node node, String strXPath) throws TransformerException {
//		if (node == null) {
//			return null;
//		}
//
//		NodeList nl = null;
//		nl = XPathAPI.selectNodeList(node, strXPath);
//
//		return nl;
//	}

    /**
     * query Node in the specify node under a XPath
     *
     * @param node
     * @param strXPath
     * @return
     * @throws TransformerException
     */
//	public static Node queryNode(Node node, String strXPath) throws TransformerException {
//		if (node == null) {
//			return null;
//		}
//
//		NodeList nl = queryNodeList(node, strXPath);
//		Node n = null;
//
//		if ((nl != null) && (nl.getLength() > 0)) {
//			n = nl.item(0);
//		}
//
//		return n;
//	}

    /**
     * query string value in the specify node under a XPath
     *
     * @param node
     * @param strXPath
     * @return
     * @throws TransformerException
     */
//	public static String queryNodeValue(Node node, String strXPath) throws TransformerException {
//		if (node == null) {
//			return null;
//		}
//
//		Node n = queryNode(node, strXPath);
//		String strR = null;
//
//		if (n != null) {
//			strR = queryNodeValue(n);
//		}
//
//		return strR;
//	}

    /**
     * query string value in the specify node
     *
     * @param node
     * @return
     */
    public static String queryNodeValue(Node node) {
        String strR = null;

        if (node != null) {
            if ((node.getFirstChild() != null) && (node.getNodeType() == Node.ELEMENT_NODE)) {
                strR = node.getFirstChild().getNodeValue();
            } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                strR = node.getNodeValue();
            }
        }

        return strR;
    }

    /**
     * query string Array in the specify node under a XPath
     *
     * @param node
     * @param strXPath
     * @return
     * @throws TransformerException
     */
//	public static String[] queryNodeValues(Node node, String strXPath) throws TransformerException {
//		if (node == null) {
//			return null;
//		}
//
//		NodeList nl = queryNodeList(node, strXPath);
//		int iLength = nl.getLength();
//		String[] strArr = newweb String[iLength];
//
//		for (int i = 0; i < nl.getLength(); i++) {
//			strArr[i] = queryNodeValue(nl.item(i));
//		}
//
//		return strArr;
//	}

    /**
     * node to Stirng
     *
     * @param node
     * @return
     * @throws javax.xml.transform.TransformerConfigurationException
     * @throws TransformerException
     */
    public static String node2String(Node node) throws TransformerConfigurationException, TransformerException {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }

        String strReturn = null;

        // if(node instanceof Document) node =
        // ((Document)node).getDocumentElement();
        StringWriter strWr = new StringWriter();
        Transformer serializer = TransformerFactory.newInstance().newTransformer();
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.transform(new DOMSource(node), new StreamResult(strWr));
        strReturn = strWr.toString();

        return strReturn.replaceFirst("\\<\\?[ ]*xml.*\\?\\>", "");
    }

    /**
     * Get the value of the attribute in the node
     *
     * @param node
     * @param attrName
     * @return
     */
    public static String queryAttribute(Node node, String attrName) {
        NamedNodeMap nodeMap = node.getAttributes();
        Attr attr = (Attr) nodeMap.getNamedItem(attrName);
        String attrValue = null;

        if (attr != null) {
            attrValue = attr.getNodeValue();
        }

        return attrValue;
    }

    // queryAttribute

    /**
     * Get the value of the attribute of the node
     *
     * @param node
     * @param attrName
     * @return
     * @throws TransformerException
     */
//	public static String queryAttribute(Node node, String strXPath, String attrName) throws TransformerException {
//		// get the requested node using xpath
//		Node n = queryNode(node, strXPath);
//
//		// get the attributes of node
//		NamedNodeMap nodeMap = n.getAttributes();
//		Attr attr = (Attr) nodeMap.getNamedItem(attrName);
//		String attrValue = null;
//
//		if (attr != null) {
//			attrValue = attr.getNodeValue();
//		}
//
//		return attrValue;
//	}

    // queryAttribute
}
