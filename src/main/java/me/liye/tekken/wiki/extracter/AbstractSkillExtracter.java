package me.liye.tekken.wiki.extracter;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.liye.tekken.wiki.db.TKDB;
import me.liye.tekken.wiki.doamin.SkillEntry;
import me.liye.tekken.wiki.spider.Spider;
import me.liye.tekken.wiki.spider.Spider.Executor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public abstract class AbstractSkillExtracter {

    public abstract String getInitSql();

    public abstract String getCategory();

    public abstract String getRowPattern();

    public abstract String getColPattern();

    final Set<String> unknown = new HashSet();

    public void init() {
        try {
            int count = TKDB.INSTANCE.update(getInitSql(), null);
            System.out.println("".format("delete %s %s ", getCategory(), count));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void process(File root) {
        Collection<File> files = FileUtils.listFiles(root, new NameFileFilter(getCategory() + ".htm"),
                                                     TrueFileFilter.INSTANCE);
        init();
        for (File f : files) {
            List<SkillEntry> sks = extracter(f);
            System.out.println(sks.size() + " " + f);
            save(sks);
        }

        System.err.println("unknown: " + unknown);
    }

    private void save(List<SkillEntry> sks) {
        for (SkillEntry sk : sks) {
            TKDB.INSTANCE.insertSkillEntry(sk);
        }
    }

    private List<SkillEntry> extracter(File file) {

        final List<SkillEntry> sks = new ArrayList();

        String[] ss = StringUtils.split(file.getPath(), "/");
        final String charactor = ss[5];

        String rowPattern = getRowPattern();
        final String colPattern = getColPattern();
        new Spider(file, rowPattern).execute(new Executor() {

            @Override
            public void process(Node tr, int index) throws Exception {
                // 是否有列匹配
                if (Spider.isHave(tr, colPattern)) {
                    // 行
                    final SkillEntry sk = new SkillEntry();
                    sk.setCharactor(charactor);
                    sk.setCategory(getCategory());
                    sk.setDomContent(tr.getTextContent());
                    sks.add(sk);

                    // 列匹配
                    new Spider(tr, colPattern).execute(new Executor() {

                        @Override
                        public void process(Node td, int index) throws Exception {
                            if (Spider.isHave(td, "xhtml:B")) {
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
                    unknown.add(tr.getTextContent());
                }
            }
        });
        return sks;
    }

}
