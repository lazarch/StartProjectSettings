package utils;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Objects;

public class XMLProgramSettings {

    public static boolean isXMLSettingsFile() {
        if (!Files.exists(Paths.get(getFileNameXMLSettings()), LinkOption.NOFOLLOW_LINKS)) {
            createXMLSettingsFile();
        }
        return Files.exists(Paths.get(getFileNameXMLSettings()), LinkOption.NOFOLLOW_LINKS);
    }

    public static String getFileNameXMLSettings() {
        return ConstantForAll.FILE_XML_PARAMS;
    }

    private static Document createXMLSettingsParams() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();

        Comment comment = doc.createComment(
                "аргумент \"Log\" - ведение log-файла (1 - включено, 0 - отключено)" );
        Element rootElement = doc.createElement("ProgramSettings");
        rootElement.setAttribute("Log", "1");
        doc.appendChild(rootElement);
        rootElement.getParentNode().insertBefore(comment, rootElement);

        return doc;
    }

    public static String createXMLSettingsFile() {
        String fileName = "";
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(createXMLSettingsParams());

            fileName = ConstantForAll.FILE_XML_PARAMS;
            StreamResult result = new StreamResult(new File(fileName));

            if (transformer != null) {
                transformer.transform(source, result);
            }

        } catch (ParserConfigurationException |
                TransformerException
                pce) {
            pce.printStackTrace();
        }
        return fileName;
    }

    public static boolean isLogInSettings() {
        return Objects.equals(XMLProgramSettings.getAttr("ProgramSettings", null, "Log"), "1");
    }

    public static String getAttr(String strElement, String strId, String strAttr) {
        String iAttr = "";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return iAttr;
        }
        Document doc = null;
        String fileName = ConstantForAll.FILE_XML_PARAMS;
        try {
            doc = db.parse(new FileInputStream(new File(fileName)));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            return iAttr;
        }

        NodeList entries = null;
        if (doc != null) {
            entries = doc.getElementsByTagName(strElement);
        } else return iAttr;

        int num = 0;
        if (entries != null) {
            num = entries.getLength();
        } else return iAttr;

        if (num == 0) return iAttr;
        if (strId == null){
            Element node = (Element) entries.item(0);
            String result = node.getAttribute(strAttr);
            return node.getAttribute(strAttr);
        }else{
            //поиск по циклу id ?
            for (int i = 0; i < num; i++) {
                Element node = (Element) entries.item(i);
                if (Objects.equals(node.getAttribute("Id"), strId) && node.hasAttribute(strAttr)) {
                    return node.getAttribute(strAttr);
                }
            }
        }

        return iAttr;
    }
}
