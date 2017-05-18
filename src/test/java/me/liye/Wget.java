package me.liye;

import me.liye.tekken.wiki.spider.NodeUtils;
import me.liye.tekken.wiki.spider.XHTMLNamespaceContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;


/**
 * Created by liye on 16/05/2017.
 *
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Wget {

    private static final Logger log = Logger.getLogger(Wget.class);


    private static final String host="http://seesaawiki.jp/w/inatekken/d/";
    private static final String url = host+"%c1%ed%b9%e7TFR";
    private static final File tmp;
    private static final HashSet wgetUrls = new HashSet();
    static {
        tmp = new File(new File(System.getProperty("user.home"), "tmp"), "wiki");
        tmp.mkdirs();
    }

    public static void main(String[] args) throws IOException, SAXException, XPathExpressionException {
        log.info("cache dir:" + tmp);
        String content = wget(url);
        parseDom(content);
    }

    private static String wget(String url) throws IOException {

        log.info("wget MAIN_URL:"+url);

        wgetUrls.add(url);

        String cacheFileName = URLEncoder.encode(url, "UTF-8");
        File cacheFile = new File(tmp, cacheFileName);

        String content = null;
        if (cacheFile.exists()) {
            log.debug("cache hit:" + cacheFileName);
            content = FileUtils.readFileToString(cacheFile);
        } else {
            log.debug("cache miss:" + cacheFileName);
            HttpClient hc = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse resp = hc.execute(get);

            if (resp.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("error " + resp.getStatusLine().getStatusCode());
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            resp.getEntity().writeTo(bos);

            content = new String(bos.toByteArray(), "EUC-JP");
            FileUtils.write(cacheFile, content);
        }
        return content;
    }


    private static void parseDom(String content) throws SAXException, IOException, XPathExpressionException {
        //
        DOMParser parser = new DOMParser();

        ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes("EUC-JP"));
        InputSource is = new InputSource(bais);

        parser.parse(is);
        Document doc = parser.getDocument();
        //

        XPath xp = XPathFactory.newInstance().newXPath();
        NamespaceContext nsContext = new XHTMLNamespaceContext();
        xp.setNamespaceContext(nsContext);
//        XPathExpression expr = xp.compile("//*[@id=\"content_block_3\"]");
        XPathExpression expr = xp.compile("//xhtml:A");
        //

        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
           Node node = nodes.item(i);
            if (NodeUtils.isNotBlankNode(node)) {
//                exec.process(nodes.item(i), i);
//                System.out.println(nodes.item(i).getTextContent());
//                System.out.println(node.getClass());
                Node hrefNode = node.getAttributes().getNamedItem("href");
                if(hrefNode!=null){
                    String href = hrefNode.getNodeValue();
                    if(StringUtils.startsWith(href,host)){
                       System.out.println(href);


                        if(wgetUrls.contains(href)){
                            log.warn("found loop MAIN_URL:" + href);
                            continue;
                        }
                        else {
                            wget(href);
                        }
                   }
                }
            }
        }
    }
}
