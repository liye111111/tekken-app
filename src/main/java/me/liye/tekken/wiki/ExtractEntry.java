package me.liye.tekken.wiki;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.liye.tekken.wiki.doamin.SkillEntry;
import me.liye.tekken.wiki.spider.NodeUtils;
import me.liye.tekken.wiki.spider.Spider;
import me.liye.tekken.wiki.spider.Spider.Executor;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class ExtractEntry {

    public static void main(String[] args) throws SAXException, IOException {

        final List<SkillEntry> sks = new ArrayList();

        String file = "/Users/liye/Documents/tkwiki/tt2u/alex/special.htm";
        String[] ss = StringUtils.split(file, "/");
        final String category = StringUtils.substringBefore(ss[6], ".");
        final String charactor = ss[5];

        String xp = "//xhtml:TR";
        final String xp2 = "./xhtml:TD";
        Spider sp = new Spider(new File(file), xp);
        sp.execute(new Executor() {

            @Override
            public void process(Node tr, int index) throws Exception {
                // 是否有列匹配
                if (NodeUtils.isHave(tr, xp2)) {

                    Spider sp2 = new Spider(tr, xp2);

                    // 行
                    final SkillEntry sk = new SkillEntry();
                    sk.setCharactor(charactor);
                    sk.setCategory(category);
                    sk.setDomContent(tr.getTextContent());
                    sks.add(sk);

                    sp2.execute(new Executor() {

                        @Override
                        public void process(Node td, int index) throws Exception {
                            // System.out.println(td.getNamespaceURI() + " " + td.getNodeName());
                            if (NodeUtils.isHave(td, "xhtml:B")) {
                                sk.setIsNew("Y");
                            }
                            // 列
                            String txt = td.getTextContent();
                            switch (index) {

                                case 0:
                                    sk.setName(txt);
                                    break;
                                case 1:
                                    sk.setCommand(txt);
                                    break;
                                case 2:
                                    sk.setJudge(txt);
                                    break;
                                case 3:
                                    sk.setDamage(txt);
                                    break;
                                case 4:
                                    sk.setF_init(txt);
                                    break;
                                case 5:
                                    sk.setF_block(txt);
                                    break;
                                case 6:
                                    sk.setF_hit(txt);
                                    break;
                                case 7:
                                    sk.setF_ch(txt);
                                    break;
                                case 8:
                                    sk.setMemo(txt);
                                    break;
                                default:
                                    throw new RuntimeException("".format("unknown cloumn:%s %s ", index, txt));
                            }

                        }

                    });

                } else {
                    System.out.println("ignore: " + tr.getTextContent());
                }
            }
        });

        for (SkillEntry sk : sks) {
            // if (StringUtils.isEmpty(sk.getCommand())) {
            System.out.println("sk: " + sk);
            // System.out.println("tr: " + sk.getDomContent());
            // TKDB.INSTANCE.insertSkillEntry(sk);
            // }
        }
    }
}
