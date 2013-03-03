package me.liye.tekken.wiki.spider;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Spider {

    String          url;
    String          xpath;
    XPathExpression expr;
    Document        doc;

    public Spider(String url, String xpath) {
        super();
        this.url = url;
        this.xpath = xpath;

        XPath xp = XPathFactory.newInstance().newXPath();
        NamespaceContext nsContext = new XHTMLNamespaceContext();
        xp.setNamespaceContext(nsContext);
        try {
            expr = xp.compile(xpath);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public void execute(Executor exec) {
        try {
            DOMParser parser = new DOMParser();
            parser.parse(url);
            doc = parser.getDocument();

            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                exec.process(nodes.item(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Document getDoc() {
        return doc;
    }

    public interface Executor {

        void process(Node node) throws Exception;
    }

    public static abstract class A implements Executor {

        @Override
        public void process(Node node) throws Exception {

            String txt = node.getTextContent();
            String href = node.getAttributes().getNamedItem("href").getNodeValue();
            process(txt, href);
        }

        public abstract void process(String txt, String href) throws Exception;

    }

    public static void showNode(Node node, String perfix) {
        System.out.println(perfix + node.getNamespaceURI() + " " + node.getNodeName());
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            showNode(node.getChildNodes().item(i), perfix + perfix);
        }
    }
}
