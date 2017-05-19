package me.liye.tekken.wiki.tk7fr;

import me.liye.tekken.wiki.spider.XHTMLNamespaceContext;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;

/**
 * Created by liye on 17/05/2017.
 *
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Config {
    public static final String host = "http://seesaawiki.jp/w/inatekken/d/";
    public static final String MAIN_URL = host + "%c1%ed%b9%e7TFR";
    public static final File tmp = new File(new File(System.getProperty("user.home"), "tmp"), "pagecache");
    public static final File out = new File(new File(System.getProperty("user.home"), "tmp"), "tk7fr");
    public static final File out_trans = new File(new File(System.getProperty("user.home"), "tmp"), "tk7fr-trans");
    //

    //全部链接
    public static XPathExpression XPATH_A;
    // 技能
    public static XPathExpression XPATH_SKILL;
    // 角色
    public static XPathExpression XPATH_PLAYER;
    // 内容
    public static XPathExpression XPATH_CONTENT;

//    user-area


    static {
        tmp.mkdirs();

        XPath xp = XPathFactory.newInstance().newXPath();
        NamespaceContext nsContext = new XHTMLNamespaceContext();
        xp.setNamespaceContext(nsContext);
        try {
            XPATH_A = xp.compile("//xhtml:A");
            XPATH_SKILL = xp.compile("//*[@id=\"content_block_1\"]/xhtml:TBODY/xhtml:TR/xhtml:TD/xhtml:A");
            XPATH_PLAYER = xp.compile("//*[@id=\"content_block_3\"]/xhtml:TBODY/xhtml:TR/xhtml:TD/xhtml:A");
            XPATH_CONTENT = xp.compile("//xhtml:DIV[@id=\"page-body-inner\"]/xhtml:DIV[@class=\"user-area\"]");
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
