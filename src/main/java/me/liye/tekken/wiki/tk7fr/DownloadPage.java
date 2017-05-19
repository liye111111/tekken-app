package me.liye.tekken.wiki.tk7fr;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import java.io.File;

import static me.liye.tekken.wiki.tk7fr.Config.MAIN_URL;
import static me.liye.tekken.wiki.tk7fr.DownloadAllLink.wget;

/**
 * 下载角色技能
 * Created by liye on 17/05/2017.
 *
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class DownloadPage {

    private static final Logger log = Logger.getLogger(DownloadPage.class);

    public static void main(String[] args) throws Exception {
        FileUtils.deleteDirectory(Config.out);

// 1.首页,抓取技能TAB
        String page = wget(MAIN_URL, true);
//        log.debug(page);

        Utils.parseDom(page, Config.XPATH_SKILL, new ParseDomCallback4A() {

            @Override
            protected void callback(String href, String text) throws Exception {
                log.debug("skill:"+text+":"+href);
                File tab = new File(Config.out, text);
                tab.mkdirs();
                //2.技能页,抓取角色列表
                String playerPage = wget(href,true);
                Utils.parseDom(playerPage, Config.XPATH_PLAYER, new ParseDomCallback4A() {
                    @Override
                    protected void callback(String href, String text) throws Exception {
                        log.debug("player"+text+":"+href);
                        //
                        text = StringUtils.replace(text,"/","&");
                        File player = new File(tab,text+".html");
                        // 3.角色技能页
                        String contentPage = wget(href,true);
                        //
                        Utils.parseDom(contentPage, Config.XPATH_CONTENT, new ParseDomCallback() {
                            @Override
                            public void callback(Node node) throws Exception {
                                FileUtils.write(player,Utils.extractInnerHtml(node));
                            }
                        });
                    }
                });

            }
        });


    }
}
