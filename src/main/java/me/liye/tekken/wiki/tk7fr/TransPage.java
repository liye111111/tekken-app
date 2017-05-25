package me.liye.tekken.wiki.tk7fr;

import me.liye.tekken.wiki.db.TKDB2;
import me.liye.tekken.wiki.doamin.Language;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 翻译文件名为英文
 * Created by liye on 18/05/2017.
 *
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class TransPage {
    private static final Logger log = Logger.getLogger(TransPage.class);
    public static void main(String[] args) throws IOException {

        Set<String> transMiss = new HashSet<>();
        Map<String, Language> lang= TKDB2.INSTANCE.getJpLanguageMap();


        File source = Config.out_ori;
        File dest = Config.out_trans;

        FileUtils.deleteDirectory(dest);
        FileUtils.copyDirectory(source,dest);

        for(File skill: dest.listFiles()){
            log.debug("source:"+skill.getName());
            File skillDest = new File(dest,lang.get(skill.getName()).getEn());
            log.debug("dest:"+ skillDest);
            skillDest.mkdirs();

            for(File player:skill.listFiles()){
                log.debug("source:"+player.getName());
                String jp = StringUtils.substringBefore(player.getName(), ".");
                if(lang.get(jp)==null){
                   transMiss.add(jp);
                }
                else {
                    String en = lang.get(jp).getEn();
                    en = StringUtils.capitalize(en);
                    File enFile = new File(skillDest, en + ".html");
                    log.debug("dest:"+enFile.getName());
                    player.renameTo(enFile);
                }
            }

            FileUtils.deleteDirectory(skill);

        }
        System.out.println(transMiss);
    }
}
