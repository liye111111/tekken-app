package me.liye.tekken.wiki.spider;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Spider {

    String          url;
    InputSource     is;
    String          xpath;
    XPathExpression expr;
    Node            doc;

    public Spider(String url, String xpath) {
        this.url = url;
        this.xpath = xpath;

        try {
            initDom();
            intiXpath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Spider(File file, String xpath) {
        this.xpath = xpath;
        try {
            is = new InputSource(new FileReader(file));
            initDom();
            intiXpath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Spider(Node node, String xpath) {
        this.xpath = xpath;
        doc = node;
        try {
            intiXpath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void intiXpath() throws XPathExpressionException {
        XPath xp = XPathFactory.newInstance().newXPath();
        NamespaceContext nsContext = new XHTMLNamespaceContext();
        xp.setNamespaceContext(nsContext);
        expr = xp.compile(xpath);
    }

    private void initDom() throws SAXException, IOException {
        DOMParser parser = new DOMParser();
        if (url != null) {
            parser.parse(url);
        } else if (is != null) {
            parser.parse(is);
        } else {
            throw new RuntimeException("no input source found!");
        }
        doc = parser.getDocument();
    }

    public boolean isHave() {
        NodeList result;
        try {
            result = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        return result.getLength() != 0;
    }

    public void execute(Executor exec) {
        execute(exec, 0);
    }

    public void execute(Executor exec, int startLine) {
        try {
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            for (int i = startLine; i < nodes.getLength(); i++) {
                if (NodeUtils.isNotBlankNode(nodes.item(i))) {
                    exec.process(nodes.item(i), i);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface Executor {

        void process(Node node, int index) throws Exception;
    }

    public static abstract class A implements Executor {

        @Override
        public void process(Node node, int index) throws Exception {

            String txt = node.getTextContent();
            String href = node.getAttributes().getNamedItem("href").getNodeValue();
            process(txt, href);
        }

        public abstract void process(String txt, String href) throws Exception;

    }

    public static class SaveHtmlExcutor implements Executor {

        File   outputFile;
        String encoding;

        public SaveHtmlExcutor(File outputFile, String encoding) {
            super();
            this.outputFile = outputFile;
            this.encoding = encoding;
        }

        @Override
        public void process(Node node, int index) throws Exception {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(node);
            java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFile);
            StreamResult result = new StreamResult(fos);
            Properties props = new Properties();
            props.setProperty("encoding", encoding);
            props.setProperty("method", "html");
            props.setProperty("omit-xml-declaration", "no");
            transformer.setOutputProperties(props);
            transformer.transform(source, result);
            fos.flush();
            fos.close();
        }
    }

}
