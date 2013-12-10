package me.liye.tekken.wiki.imp;

import java.io.File;
import java.util.Collections;
import java.util.List;

import me.liye.tekken.wiki.db.TKDB2;
import me.liye.tekken.wiki.doamin.SkillBlock;
import me.liye.tekken.wiki.doamin.SkillEntry;
import me.liye.tekken.wiki.spider.Spider;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Importor {

    private static final Logger log = Logger.getLogger(Importor.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        File root = new File("/Users/liye/www/wiki-trans/br");
        for (File f : root.listFiles()) {
            new Importor().process(f);
        }
    }

    public void process(File characterDir) {
        String game = characterDir.getParentFile().getName();
        String charactor = characterDir.getName();
        File[] skillFiles = characterDir.listFiles();
        for (File skillFile : skillFiles) {
            log.debug("imp " + skillFile);
            String skillCategory = FilenameUtils.getBaseName(skillFile.getName());
            List<SkillBlock> blocks = fromHtmlFile(skillFile, charactor, skillCategory);
            if (!blocks.isEmpty()) {
                save(game, charactor, skillCategory, blocks);
            }
        }

    }

    private void save(String game, String charactor, String skillCategory, List<SkillBlock> blocks) {
        log.debug("".format("save %s %s %s", game, charactor, skillCategory));
        String table = "skill_" + game;
        TKDB2.INSTANCE.deleteSkill(table, charactor, skillCategory);
        for (SkillBlock block : blocks) {
            for (SkillEntry sk : block.getRows()) {
                TKDB2.INSTANCE.insertSkillEntry(table, sk);
            }
        }
    }

    public List<SkillBlock> fromHtmlFile(File file, String charactor, String category) {
        if ("special".equals(category)) {
            SkillPageExecutor exec = new SkillPageExecutor(charactor, category);
            new Spider(file, "//xhtml:TBODY").execute(exec);
            return exec.getResult();
        }
        return Collections.EMPTY_LIST;

    }
}