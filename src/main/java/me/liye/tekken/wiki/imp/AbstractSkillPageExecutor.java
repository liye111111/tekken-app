package me.liye.tekken.wiki.imp;

import java.util.ArrayList;
import java.util.List;

import me.liye.tekken.wiki.doamin.SkillBlock;
import me.liye.tekken.wiki.doamin.SkillEntry;
import me.liye.tekken.wiki.spider.NodeIterator;
import me.liye.tekken.wiki.spider.NodeUtils;
import me.liye.tekken.wiki.spider.Spider.Executor;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public abstract class AbstractSkillPageExecutor implements Executor {

    String           blockName = "normal";
    SkillBlock       block;

    List<SkillBlock> result    = new ArrayList();
    String           charactor;
    String           category;

    public AbstractSkillPageExecutor(String charactor, String category) {
        this.charactor = charactor;
        this.category = category;
    }

    public List<SkillBlock> getResult() {
        return result;
    }

    @Override
    public void process(Node node, int index) throws Exception {
        // 修正blockName
        blockName = adjustBlockName(blockName, node);
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
                    sk.setCate(category);
                    sk.setRole(charactor);
                    sk.setDomContent(tr.getTextContent().trim());
                    for (int i = 0; i < tds.size(); i++) {
                        if (NodeUtils.isHave(tds.get(i), "xhtml:B")) {
                            sk.setIsNew("Y");
                        }
                        String txt = tds.get(i).getTextContent().trim();
                        sk.setGroupName(blockName);
                        try {
                            processTD(blockName, i, txt, sk);
                        } catch (Exception e) {
                            throw new RuntimeException(charactor + " : " + tr.getTextContent());
                        }
                    }

                    block.getRows().add(sk);
                }
            }
        }

    }

    protected String adjustBlockName(String blockName, Node node) {
        return blockName;
    }

    protected abstract void processTD(String blockName, int index, String txt, SkillEntry sk);

}
