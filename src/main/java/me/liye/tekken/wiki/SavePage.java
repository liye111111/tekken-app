package me.liye.tekken.wiki;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import me.liye.tekken.wiki.db.TKDB;
import me.liye.tekken.wiki.doamin.Language;
import me.liye.tekken.wiki.spider.Spider;
import me.liye.tekken.wiki.spider.Spider.A;
import me.liye.tekken.wiki.spider.Spider.Executor;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class SavePage {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // String output = "/Users/liye/Documents/tkwiki/tt2u2";
        String output = "/Users/liye/Documents/tkwiki/br2";
        String template = "/Users/liye/Documents/tkwiki/index_template.htm";
        // String url = "http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7TUD";
        String url = "http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7TBR";

        SavePage sp = new SavePage(url, output, template);
        sp.execute();
        // System.out.println(sp.unknown);
        for (String jp : sp.unknown) {
            System.out.println("".format("insert into language (jp,cn,en) values ('%s','','');", jp));
        }
    }

    private static final String XP_CHARACTER      = "//*[@id=\"content_block_4\"]/TBODY/xhtml:TR/xhtml:TD/xhtml:A";
    private static final String XP_SKILLNAME      = "//*[@id=\"page-body-inner\"]/xhtml:DIV/xhtml:A";
    private static final String XP_SKILLCONTENT   = "//*[@id=\"page-body-inner\"]/xhtml:DIV[@class=\"user-area\"]";

    // File output = new File("/Users/liye/Documents/tkwiki/br");
    // String url = "http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7TBR";

    // 爬取内容输出路径
    File                        output;
    // 爬取url
    String                      url;
    Map<String, String>         dictCN            = new HashMap();
    Map<String, String>         dictEN            = new HashMap();
    String                      encoding          = "UTF-8";
    int                         INDEX_COLUMN_SIZE = 8;
    // 没有对应language的jp标题
    Set<String>                 unknown           = new HashSet();
    // 模版
    String                      template;
    // 版本
    String                      version;

    boolean                     onlyIndex         = false;
    // 翻译表
    final Map<String, Language> jpLanMap;

    public SavePage(String url, String output, String template) {
        this.output = new File(output);
        this.output.mkdirs();
        this.url = url;
        try {
            this.template = FileUtils.readFileToString(new File(template), encoding);
            this.version = new SimpleDateFormat("yyyyMMdd").format(new Date());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        jpLanMap = TKDB.INSTANCE.getJpLanguageMap();
    }

    public void execute() {
        output.mkdirs();
        final StringBuilder sb = new StringBuilder("<table  border=\"1\"  cellpadding=\"3\"><tr>");
        final List<String> clist = new ArrayList<String>();

        new Spider(url, XP_CHARACTER).execute(new A() {

            @Override
            public void process(String txt, String href) throws Exception {
                String enName = getLanByJp(txt).getEn();
                if (enName == null) {
                    System.out.println("need name mapping:" + txt);
                    return;
                }

                File enNameDir = new File(output, enName);
                if (!onlyIndex) {
                    saveCharactor(enNameDir, href);
                }
                clist.add(enName);

            }
        });
        Collections.sort(clist);

        int i = 0;
        for (String c : clist) {
            if (i % INDEX_COLUMN_SIZE == 0) {
                sb.append("</tr><tr>");
            }
            sb.append("<td><a href=\"" + c + "/index.htm\"><img src=\"/img/" + c + ".png\"><br/>" + c + "</td>");
            sb.append("\r\n");
            i++;
        }

        sb.append("</tr></table>");

        write(new File(output, "index.htm"), sb.toString());

        System.err.println("unknown:" + this.unknown);
    }

    private void write(File file, String content) {
        String cnt = template.replace("$$content$$", content);
        cnt = cnt.replace("$$version$$", version);
        try {
            System.out.println("wirte " + file);
            FileUtils.write(file, cnt, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void saveCharactor(final File enNameDir, String href) {
        enNameDir.mkdirs();
        saveSkill(new File(enNameDir, "index.htm"), href);
        // 抓取其它技能页面
        new Spider(href, XP_SKILLNAME).execute(new Executor() {

            @Override
            public void process(Node node, int index) throws Exception {
                // Spider.showNode(node, " ");
                String skillName = node.getTextContent();
                Language lan = getLanByJp(skillName);
                String skillNameEN = lan.getEn();
                String href = node.getAttributes().getNamedItem("href").getNodeValue();
                File skillFile = new File(enNameDir, skillNameEN + ".htm");
                saveSkill(skillFile, href);
            }
        });
    }

    private Language getLanByJp(String jp) {
        Language lan = jpLanMap.get(jp);
        if (lan == null) {
            unknown.add(jp);
            lan = new Language();
            lan.setCn(jp);
            lan.setEn(jp);
            lan.setJp(jp);
            return lan;
        }
        return lan;
    }

    protected void saveSkill(final File skillFile, String href) {
        new Spider(href, XP_SKILLCONTENT).execute(new Executor() {

            @Override
            public void process(Node node, int index2) throws Exception {
                Node first = null;

                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    Node child = node.getChildNodes().item(i);

                    // hr以上为有效内容
                    if (child.getNodeName().equals("HR")) {
                        break;
                    } else if (child.getNodeName().equals("A")) {
                        // 第一个链接
                        if (first == null) {
                            first = child;
                            child.setTextContent("介绍");
                            Attr href = node.getOwnerDocument().createAttribute("href");
                            href.setValue("index.htm");
                            child.getAttributes().setNamedItem(href);
                        }
                        // 其它链接
                        else {

                            String cn = getLanByJp(child.getTextContent()).getCn();
                            String en = getLanByJp(child.getTextContent()).getEn();
                            child.setTextContent(cn);
                            Attr href = node.getOwnerDocument().createAttribute("href");
                            href.setValue(en + ".htm");
                            child.getAttributes().setNamedItem(href);
                        }
                    }
                    // 当前页
                    else if (child.getNodeName().equals("B")) {
                        if (first == null) {
                            first = child;
                            child.setTextContent("介绍");
                        } else {
                            String cn = getLanByJp(child.getTextContent()).getCn();
                            child.setTextContent(cn);
                        }
                    }
                }
                // 增加一个返回首页链接
                Element index = node.getOwnerDocument().createElement("A");
                index.setTextContent("返回列表");
                Attr href = node.getOwnerDocument().createAttribute("href");
                href.setValue("../index.htm");
                index.getAttributes().setNamedItem(href);
                node.insertBefore(index, first);

                Node blank = node.getOwnerDocument().createTextNode(" ");
                node.insertBefore(blank, first);

                // node.appendChild(index);

                // Spider.showNode(node, " ");
                // writeDoc(node);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                DOMSource source = new DOMSource(node);
                java.io.FileOutputStream fos = new java.io.FileOutputStream(skillFile);
                StreamResult result = new StreamResult(fos);
                Properties props = new Properties();
                props.setProperty("encoding", encoding);
                props.setProperty("method", "html");
                props.setProperty("omit-xml-declaration", "no");
                transformer.setOutputProperties(props);
                transformer.transform(source, result);
                fos.close();
                String content = FileUtils.readFileToString(skillFile, encoding);
                write(skillFile, content);
            }
        });

    }

    public Set<String> getUnknown() {
        return unknown;
    }

}
