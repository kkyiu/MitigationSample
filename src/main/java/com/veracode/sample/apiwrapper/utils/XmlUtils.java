package com.veracode.sample.apiwrapper.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.veracode.sample.apiwrapper.models.Flaw;
import com.veracode.util.lang.StringUtility;

/**
 * An utility class that provides methods to work with XML
 */
public class XmlUtils {

    /**
     * Parse the AppID of the given application from the AppListResult XML
     * 
     * @param appName - Application name
     * @param xmlAppListResult - XML from applistresult.do web service API
     * @return App ID or empty if the application is not found
     * @throws Exception
     */
    public static final String parseAppId(String appName, String xmlAppListResult) throws Exception {
        Document xml = getXmlDocument(xmlAppListResult);
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xPathObj = xpf.newXPath();
        NodeList nodeList = (NodeList) xPathObj.evaluate(
                "/*/*[local-name()='app'][@app_id][@app_name]",
                xml.getDocumentElement(), XPathConstants.NODESET);
        String appId = "";

        for (int x = 0; x < nodeList.getLength(); x++) {
            Node node = nodeList.item(x);

            if (StringUtility.compare(node.getAttributes().getNamedItem("app_name").getNodeValue(), appName, true) == 0) {
                appId = node.getAttributes().getNamedItem("app_id").getNodeValue();
                break;
            }
        }
        return appId;
    }

    /**
     * Parse the BuildID from the BuidlInfoResult XML
     * 
     * @param xmlBuildInfoResult - XML from buildinforesult.do web service API
     * @return Build ID or empty if it is not found
     * @throws Exception
     */
    public static final String parseBuildId(String xmlBuildInfoResult) throws Exception {
        if (StringUtility.isNullOrEmpty(xmlBuildInfoResult)) {
            throw new IllegalArgumentException("Empty XML document.");
        }

        Document xml = getXmlDocument(xmlBuildInfoResult);
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xPathObj = xpf.newXPath();
        Node node = (Node)xPathObj.evaluate("/*/*[local-name()='build'][@build_id]", xml.getDocumentElement(), XPathConstants.NODE);
        String buildId = "";
        if (null != node) {
            buildId = node.getAttributes().getNamedItem("build_id").getNodeValue();
        }
        return (!StringUtility.isNullOrEmpty(buildId)) ? buildId : "";
    }

    /**
     * Return a set of flaws from the given Detailed Report.
     * 
     * @param detailedReportXml - XML from detailedreport.do web service API
     * @return A set of flaws
     * @throws Exception
     */
    public static Set<Flaw> parseFlaws(String detailedReportXml) throws Exception {
        Set<Flaw> flaws = null;
        if (StringUtility.isNullOrEmpty(detailedReportXml)) {
            return flaws;
        }

        Document detailedReportDoc = getXmlDocument(detailedReportXml);
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xPathObj = xpf.newXPath();

        final String FLAW_NODE_XPATH = "//*[local-name()='flaw']";

        try {
            // parsing flaw tags in detailedreport XML 
            NodeList flawNodeList = (NodeList)xPathObj.evaluate(FLAW_NODE_XPATH, detailedReportDoc.getDocumentElement(), XPathConstants.NODESET);

            Set<Node> flawNodes = new HashSet<>();
            for (int i = 0; i < flawNodeList.getLength(); i++) {
                flawNodes.add(flawNodeList.item(i));
            }
            flaws = flawNodes.stream().map((node) -> new Flaw(node)).collect(Collectors.toSet());
        } catch (XPathExpressionException xpee) {
            throw new RuntimeException(xpee);
        }

        return flaws;
    }

    /**
     * Get a XML Document from the given XML
     * 
     * @param xmlString - XML
     * @return XML Document
     * @throws Exception
     */
    public static final Document getXmlDocument(String xmlString) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); /* Solves security vulnerability */
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);

        InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document xml = db.parse(new InputSource(new InputStreamReader(inputStream, "UTF-8")));
        return xml;
    }

    /**
     * Get the error message, if any, from the given XML which could be
     * returned from any *.do web service API
     * 
     * @param xmlString - XML
     * @return an error message inside the XML or empty string if none found
     */
    public static final String getErrorString(String xmlString) {
        if (StringUtility.isNullOrEmpty(xmlString)) {
            return "";
        }

        Pattern pattern = Pattern.compile("<error>(.*?)</error>");
        Matcher matcher = pattern.matcher(xmlString);
        return matcher.find() ? matcher.group(1).trim() : "";
    }
}
