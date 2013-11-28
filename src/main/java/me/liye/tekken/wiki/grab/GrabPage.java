package me.liye.tekken.wiki.grab;

import java.io.File;

import me.liye.tekken.wiki.spider.Spider;
import me.liye.tekken.wiki.spider.Spider.A;
import me.liye.tekken.wiki.spider.Spider.Executor;
import me.liye.tekken.wiki.spider.Spider.SaveHtmlExcutor;

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

    private static final Logger log          = Logger.getLogger(GrabPage.class);

    // 爬取内容输出路径
    File                        outputPath;
    // 爬取url
    String                      indexUrl;
    String                      encoding     = "UTF-8";

    // 一级页面xpath,角色
    String                      XP_INDEX     = "//*[@id=\"content_block_4\"]/TBODY/xhtml:TR/xhtml:TD/xhtml:A";
    // 二级页面，技能链接
    String                      XP_LV2       = "//*[@id=\"page-body-inner\"]/xhtml:DIV/xhtml:A";
    // 二级页面，技能首页
    String                      XP_LV2_INDEX = "//*[@id=\"content_block_5-body\"]";
    // 三级页面，技能内容
    String                      XP_LV3       = "//*[@id=\"page-body-inner\"]/xhtml:DIV[@class=\"user-area\"]";

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

        // 抓取技能首页
        File indexFile = new File(lv2Dir, "index.htm");
        new Spider(href, XP_LV2_INDEX).execute(new SaveHtmlExcutor(indexFile, encoding));

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
        new Spider(href, XP_LV3).execute(new SaveHtmlExcutor(lv3File, encoding));
    }
}
