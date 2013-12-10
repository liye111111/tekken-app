package me.liye.tekken.wiki.trans;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.liye.tekken.wiki.db.TKDB2;
import me.liye.tekken.wiki.doamin.SkillEntry;
import me.liye.tekken.wiki.doamin.SkillEntryEn;
import me.liye.tekken.wiki.trans.Translator.IgnoreHandler;
import me.liye.tekken.wiki.trans.Translator.SepartorProvider;
import me.liye.tekken.wiki.trans.domain.Word;

import org.apache.commons.beanutils.BeanUtils;

/*
 * jp翻译成en
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class TransIang {

    public static void main(String[] args) {
        // String[] src = { "LP,RP,RP", "LP,RP,4WP", "【LK,RK】", "3LP,RPd", "1or2LPしゃがみ中LP", "1or2LPnRPしゃがみ中LP,RP",
        // "2WP,4〜" };
        String[] src = { "RK,nRK" };
        TransIang ti = new TransIang("skill_tt2", "skill_tt2_en");
        ti.process();
        // for (String s : src) {
        // System.out.println(s + " = " + ti.process(s));
        // }
    }

    Map<String, File> dictFiles = new HashMap();

    String            srcTable;
    String            destTable;
    Translator        t;
    Set<String>       ignores   = new HashSet();

    public TransIang(String srcTable, String destTable) {
        this.srcTable = srcTable;
        this.destTable = destTable;
        dictFiles.put(Word.TYPE_BUTTON, new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_button.txt"));
        dictFiles.put(Word.TYPE_DIRECTION,
                      new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_direction.txt"));
        dictFiles.put(Word.TYPE_POSE, new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_pose.txt"));
        dictFiles.put(Word.TYPE_OTHER, new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_other.txt"));

        t = new Translator(dictFiles, new IgnoreHandler() {

            @Override
            public void handle(String ignore) {
                ignores.add(ignore);
                // System.out.println(ignore);
            }
        }, new SepartorProvider() {

            @Override
            public String provide(Word preWord, Word currentWord) {
                if (preWord != null) {
                    String pt = preWord.getType();
                    String ct = currentWord.getType();
                    //
                    String pv = preWord.getInput();
                    String cv = currentWord.getInput();
                    if (pv.equals("N") || cv.equals("N")) {
                        return ",";
                    } else if (pt.equals(ct) && ct.equals(Word.TYPE_BUTTON)) {
                        return ",";
                    } else if (pt.equals(Word.TYPE_BUTTON) && ct.equals(Word.TYPE_DIRECTION)) {
                        return ",";
                    } else if (pt.equals(Word.TYPE_DIRECTION) && ct.equals(Word.TYPE_BUTTON)) {
                        return "+";
                    } else if (pv.equals("or") || cv.equals("or")) {
                        return " ";
                    } else if (pt.equals(Word.TYPE_POSE)) {
                        return "+";
                    } else if (pt.equals(ct) && ct.equals(Word.TYPE_DIRECTION)) {
                        return ",";
                    }
                }
                return "";
            }
        });

    }

    public String process(String src) {
        return t.process(src);
    }

    public void process() {
        // ////// 原始数据
        List<Map<String, String>> rows = TKDB2.INSTANCE.select("select * from " + destTable, null);
        HashMap<String, String> idEnMap = new HashMap();
        for (Map<String, String> row : rows) {
            idEnMap.put(row.get("id"), row.get("command"));
        }

        // ///
        TKDB2.INSTANCE.deleteAllSkill(destTable);
        rows = TKDB2.INSTANCE.select("select * from " + srcTable, null);
        for (Map<String, String> row : rows) {
            SkillEntry skill = new SkillEntry();

            for (String key : row.keySet()) {
                try {
                    BeanUtils.setProperty(skill, key, row.get(key));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // System.out.println(skill.getCommand());
            String cmdEn = t.process(skill.getCommand());

            // System.out.println(skill.getCommand());
            // ////////////////////
            try {
                SkillEntryEn enSkill = new SkillEntryEn();
                enSkill.setCommand_jp(skill.getCommand());
                BeanUtils.copyProperties(enSkill, skill);
                enSkill.setCommand(cmdEn);

                TKDB2.INSTANCE.insertObject(destTable, enSkill);
                String oldSkillName = idEnMap.get(enSkill.getId());
                if (oldSkillName == null || !oldSkillName.equals(enSkill.getCommand())) {
                    System.out.println(enSkill.getCommand_jp() + " >>> " + oldSkillName + " >>> "
                                       + enSkill.getCommand());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
