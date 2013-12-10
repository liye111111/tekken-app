package me.liye.tekken.wiki.trans;

import java.util.List;
import java.util.Map;

import me.liye.tekken.wiki.db.TKDB2;
import me.liye.tekken.wiki.doamin.SkillEntry;
import me.liye.tekken.wiki.doamin.SkillEntryEn;

import org.apache.commons.beanutils.BeanUtils;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class TransTable {

    public static void main(String[] args) {
        new TransTable("skill_tt2", "skill_tt2_en").process();
    }

    String srcTable;
    String destTable;

    public TransTable(String srcTable, String destTable) {
        this.srcTable = srcTable;
        this.destTable = destTable;
    }

    public void process() {
        TKDB2.INSTANCE.deleteAllSkill(destTable);
        List<Map<String, String>> rows = TKDB2.INSTANCE.select("select * from " + srcTable, null);
        for (Map<String, String> row : rows) {
            SkillEntry skill = new SkillEntry();

            for (String key : row.keySet()) {
                try {
                    BeanUtils.setProperty(skill, key, row.get(key));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            // if ("catch".equals(skill.getCategory())) {
            // System.out.println(skill.getCommand());
            // }

            // System.out.println(skill);
            String cmdEn = null;
            if (skill.getCate().equals("combo")) {
                cmdEn = Trans.transCombo(skill.getCommand());
            } else {
                cmdEn = Trans.transCommand(skill.getCommand());
            }

            // ////////////////////
            try {
                SkillEntryEn enSkill = new SkillEntryEn();
                enSkill.setCommand_jp(skill.getCommand());
                BeanUtils.copyProperties(enSkill, skill);
                enSkill.setCommand(cmdEn);
                String groupEn = Trans.transGroup(skill.getGroupName());
                enSkill.setGroupName(groupEn);

                TKDB2.INSTANCE.insertObject(destTable, enSkill);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
