package me.liye.tekken.wiki.imp;

import me.liye.tekken.wiki.doamin.SkillEntry;
import me.liye.tekken.wiki.spider.NodeUtils;

import org.w3c.dom.Node;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class ComboSkillPageExecutor extends AbstractSkillPageExecutor {

    public ComboSkillPageExecutor(String charactor, String category) {
        super(charactor, category);
    }

    protected String adjustBlockName(String blockName, Node node) {
        // String name = node.getParentNode().getPreviousSibling().getTextContent().trim();
        // name = node.getParentNode().getPreviousSibling().getPreviousSibling().getTextContent().trim() + name;

        String name = NodeUtils.getPreviousSiblingTextContent(node.getParentNode().getPreviousSibling(), "TABLE", 5);
        return name;
    };

    @Override
    protected void processTD(String blockName, int index, String txt, SkillEntry sk) {
        switch (index) {
            case 0:
                sk.setCommand(txt);
                break;
            case 1:
                sk.setDamage(txt);
                break;
            case 2:
                sk.setMemo(txt);
                break;
            case 3:
                break;
            default:
                throw new RuntimeException("".format("unknown cloumn:%s %s ", index, txt));
        }
    }

}
