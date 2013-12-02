package me.liye.tekken.wiki.imp;

import java.util.ArrayList;
import java.util.List;

import me.liye.tekken.wiki.doamin.SkillBlock;
import me.liye.tekken.wiki.doamin.SkillEntry;
import me.liye.tekken.wiki.spider.NodeIterator;
import me.liye.tekken.wiki.spider.NodeUtils;
import me.liye.tekken.wiki.spider.Spider.Executor;
import me.liye.tekken.wiki.trans.Trans;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class SkillPageExecutor implements Executor {

    String           blockName = "normal";
    SkillBlock       block;

    List<SkillBlock> result    = new ArrayList();
    String           charactor;
    String           category;

    public SkillPageExecutor(String charactor, String category) {
        this.charactor = charactor;
        this.category = category;
    }

    public List<SkillBlock> getResult() {
        return result;
    }

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

}
