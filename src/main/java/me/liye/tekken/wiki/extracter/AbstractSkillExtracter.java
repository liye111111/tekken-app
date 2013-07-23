package me.liye.tekken.wiki.extracter;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.liye.tekken.wiki.db.TKDB;
import me.liye.tekken.wiki.doamin.SkillBlock;
import me.liye.tekken.wiki.doamin.SkillEntry;
import me.liye.tekken.wiki.spider.NodeIterator;
import me.liye.tekken.wiki.spider.NodeUtils;
import me.liye.tekken.wiki.spider.Spider;
import me.liye.tekken.wiki.spider.Spider.Executor;
import me.liye.tekken.wiki.trans.Trans;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public abstract class AbstractSkillExtracter implements Executor {

    List<SkillBlock> result = new ArrayList();
    String           charactor;
    String           category;

    public void init() {
        try {
            int count = TKDB.INSTANCE.update(getInitSql(), null);
            System.out.println("".format("delete %s %s ", getCategory(), count));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void save(List<SkillEntry> sks) {
        for (SkillEntry sk : sks) {
            TKDB.INSTANCE.insertSkillEntry(sk);
        }
    }

    protected abstract String getInitSql();

    public abstract String getCategory();

    public void process(File root) {
        Collection<File> files = FileUtils.listFiles(root, new NameFileFilter(getCategory() + ".htm"),
                                                     TrueFileFilter.INSTANCE);
        init();
        for (File f : files) {
            System.out.println(f);
            String[] ss = StringUtils.split(f.getPath(), "/");
            String charactor = ss[5];
            List<SkillBlock> blocks = fromHtmlFile(f, charactor, getCategory());
            for (SkillBlock block : blocks) {
                System.out.println(block.getBlockName() + ": " + block.getRows().size());
                save(block.getRows());
            }
        }
    }

    public List<SkillBlock> fromHtmlFile(File file, String charactor, String category) {
        result = new ArrayList();
        this.charactor = charactor;
        this.category = category;
        // 初始化当前文件的block流
        blockName = null;
        block = null;
        new Spider(file, "//xhtml:TBODY").execute(this);
        return result;
    }

    String     blockName;
    SkillBlock block;

    @Override
    public void process(Node node, int index) throws Exception {
        // 将一个大表格拆分为多个skillBlock
        // table
        NodeIterator ni = new NodeIterator(node);
        while (ni.hasNext()) {
            Node tr = ni.next();
            if (tr != null) {
                List<Node> ths = NodeUtils.children(tr, "xhtml:TH");
                // block name, 可能没有
                if (ths.size() == 1) {
                    blockName = ths.get(0).getTextContent().trim();
                }
                // columnNames
                else if (ths.size() > 1) {
                    // 创建当前block
                    block = new SkillBlock();
                    result.add(block);
                    block.setBlockName(blockName);
                    for (Node th : ths) {
                        block.getColumnNames().add(StringUtils.trim(th.getTextContent()));
                    }
                } else {
                    // 数据行
                    List<Node> tds = NodeUtils.children(tr, "xhtml:TD");
                    SkillEntry sk = new SkillEntry();
                    sk.setCategory(category);
                    sk.setCharactor(charactor);
                    sk.setDomContent(tr.getTextContent().trim());
                    for (int i = 0; i < tds.size(); i++) {
                        if (NodeUtils.isHave(tds.get(i), "xhtml:B")) {
                            sk.setIsNew("Y");
                        }
                        String txt = tds.get(i).getTextContent().trim();
                        sk.setGroup(blockName);
                        processTD(blockName, i, txt, sk);
                    }

                    block.getRows().add(sk);
                }
            }
        }

    }

    protected void processTD(String blockName, int index, String txt, SkillEntry sk) {
        switch (index) {
            case 0:
                sk.setName(txt);
                break;
            case 1:
                sk.setCommand(txt);
                String command_en = Trans.trans(txt);
                sk.setCommand_en(command_en);
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

    public List<SkillBlock> getResult() {
        return result;
    }

}
