package me.liye.tekken.wiki.trans;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.liye.tekken.wiki.db.TKDB2;
import me.liye.tekken.wiki.doamin.SkillEntry;
import me.liye.tekken.wiki.trans.Translator.IgnoreHandler;
import me.liye.tekken.wiki.trans.Translator.SepartorProvider;
import me.liye.tekken.wiki.trans.domain.Word;

import org.apache.commons.beanutils.BeanUtils;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class TransImg {

    public static void main(String[] args) {
        String src = "hbf2";
        TransImg ti = new TransImg("skill_tt2_en", "skill_tt2_xb");
        ti.process();

        // System.out.println(ti.process(src));
    }

    Map<String, File> dictFiles = new HashMap();

    String            srcTable;
    String            destTable;
    Translator        t;

    public TransImg(String srcTable, String destTable) {
        this.srcTable = srcTable;
        this.destTable = destTable;
        dictFiles.put(Word.TYPE_BUTTON,
                      new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_img_button.txt"));
        dictFiles.put(Word.TYPE_DIRECTION,
                      new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_img_direction.txt"));

        t = new Translator(dictFiles, new IgnoreHandler() {

            @Override
            public void handle(String ignore) {
                System.out.println(ignore);
            }
        }, new SepartorProvider() {

            @Override
            public String provide(Word preWord, Word currentWord) {
                return "";
            }
        });

    }

    public String process(String src) {
        return t.process(src);
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

            System.out.println(skill.getCommand());
            skill.setCommand(t.process(skill.getCommand()));

            System.out.println(skill.getCommand());
            // ////////////////////
            try {
                TKDB2.INSTANCE.insertObject(destTable, skill);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
