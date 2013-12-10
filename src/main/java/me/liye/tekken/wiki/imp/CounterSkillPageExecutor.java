package me.liye.tekken.wiki.imp;

import me.liye.tekken.wiki.doamin.SkillEntry;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class CounterSkillPageExecutor extends AbstractSkillPageExecutor {

    public CounterSkillPageExecutor(String charactor, String category) {
        super(charactor, category);
    }

    protected void processTD(String blockName, int index, String txt, SkillEntry sk) {
        switch (index) {
            case 0:
                sk.setCmdName(txt);
                break;
            case 1:
                sk.setCommand(txt);
                // String command_en = Trans.trans(txt);
                // sk.setCommand_en(command_en);
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
