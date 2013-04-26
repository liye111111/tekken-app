package me.liye.tekken.wiki.extracter;

import java.io.File;

import me.liye.tekken.wiki.doamin.SkillEntry;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class CounterSkillExtracter extends BlockSkillExtracter {

    public static void main(String[] args) {
        new CounterSkillExtracter().process(new File("/Users/liye/Documents/tkwiki/tt2u"));
    }

    @Override
    public String getInitSql() {
        return "delete from skill where category='counter'";
    }

    @Override
    public String getCategory() {
        return "counter";
    }

    @Override
    protected void processTD(String blockName, int index, String txt, SkillEntry sk) {
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
                sk.setF_init(txt);
                break;
            case 4:
                sk.setDamage(txt);
                break;
            case 5:
                sk.setDistance(txt);
                break;
            case 6:
                sk.setF_hit(txt);
                break;
            case 7:
                sk.setMemo(txt);
                break;
            default:
                throw new RuntimeException("".format("unknown cloumn:%s %s ", index, txt));
        }
    }
}
