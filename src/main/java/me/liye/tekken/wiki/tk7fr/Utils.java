package me.liye.tekken.wiki.tk7fr;

import me.liye.tekken.wiki.spider.NodeUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by liye on 17/05/2017.
 *
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Utils {

    public static void parseDom(String content, XPathExpression xpath, ParseDomCallback parseDomCallback) throws Exception {
        //
        DOMParser parser = new DOMParser();

        ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes("EUC-JP"));
        InputSource is = new InputSource(bais);

        parser.parse(is);
        Document doc = parser.getDocument();
        //

        Object result = xpath.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (NodeUtils.isNotBlankNode(node)) {
                parseDomCallback.callback(node);
            }
        }
    }

    public static String extractInnerHtml(Node node) throws TransformerException, IOException {

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(node);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(baos);
        Properties props = new Properties();
        props.setProperty("encoding", "UTF-8");
        props.setProperty("method", "html");
        props.setProperty("omit-xml-declaration", "no");
        transformer.setOutputProperties(props);
        transformer.transform(source, result);
        baos.close();

        return new String(baos.toByteArray());
    }
}
