package me.liye.tekken.wiki.grab;

import java.io.File;
import java.util.Properties;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import me.liye.tekken.wiki.spider.Spider;
import me.liye.tekken.wiki.spider.Spider.A;
import me.liye.tekken.wiki.spider.Spider.Executor;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class GrabPage {

    public static void main(String[] args) {
        String indexUrl = args[0];
        String outputPath = args[1];
        new GrabPage(indexUrl, new File(outputPath)).grab();
    }

    private static final Logger log      = Logger.getLogger(GrabPage.class);

    // 爬取内容输出路径
    File                        outputPath;
    // 爬取url
    String                      indexUrl;
    String                      encoding = "UTF-8";

    // 一级页面xpath
    String                      XP_INDEX = "//*[@id=\"content_block_4\"]/TBODY/xhtml:TR/xhtml:TD/xhtml:A";
    String                      XP_LV2   = "//*[@id=\"page-body-inner\"]/xhtml:DIV/xhtml:A";
    String                      XP_LV3   = "//*[@id=\"page-body-inner\"]/xhtml:DIV[@class=\"user-area\"]";

    public GrabPage(String indexUrl, File outputPath) {
        super();
        this.indexUrl = indexUrl;
        this.outputPath = outputPath;
    }

    /**
     * 抓取首页
     */
    public void grab() {
        outputPath.mkdirs();
        new Spider(indexUrl, XP_INDEX).execute(new A() {

            @Override
            public void process(String txt, String href) throws Exception {
                File enNameDir = new File(outputPath, txt);
                log.debug(enNameDir);
                grabLv2(enNameDir, href);
            }
        });
    }

    /**
     * 抓取二级链接
     * 
     * @param enNameDir
     * @param href
     */
    private void grabLv2(final File lv2Dir, String href) {
        lv2Dir.mkdirs();
        // 抓取其它技能页面
        new Spider(href, XP_LV2).execute(new Executor() {

            @Override
            public void process(Node node, int index) throws Exception {
                String lv3Name = node.getTextContent();
                log.debug(lv3Name);
                String href = node.getAttributes().getNamedItem("href").getNodeValue();
                File lv3File = new File(lv2Dir, lv3Name + ".htm");
                grabLv3(lv3File, href);
            }
        });
    }

    private void grabLv3(final File lv3File, String href) {
        new Spider(href, XP_LV3).execute(new Executor() {

            @Override
            public void process(Node node, int index) throws Exception {
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                DOMSource source = new DOMSource(node);
                java.io.FileOutputStream fos = new java.io.FileOutputStream(lv3File);
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
        });

    }
}
