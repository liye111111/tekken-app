package me.liye.tekken.wiki.trans;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.liye.tekken.wiki.db.TKDB;
import me.liye.tekken.wiki.doamin.Language;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class TransFileName {

    private static final Logger log = Logger.getLogger(TransFileName.class);

    public static void main(String[] args) {
        new TransFileName(new File("/Users/liye/www/wiki"), new File("/Users/liye/www/wiki-trans")).exec();
    }

    File        root;
    File        output;
    String      fileExt   = "htm";
    Set<String> transFail = new HashSet();

    public TransFileName(File root, File output) {
        super();
        this.root = root;
        this.output = output;
    }

    public void exec() {
        try {
            FileUtils.deleteDirectory(output);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
        Map<String, Language> jpMap = TKDB.INSTANCE.getJpLanguageMap();
        exec(root, jpMap);
        System.err.println(transFail);
    }

    private void exec(File dir, Map<String, Language> jpMap) {
        for (File file : dir.listFiles()) {

            if (file.isFile()) {
                String fn = file.getName();
                String ext = FilenameUtils.getExtension(fn);
                if (fileExt.equals(ext)) {
                    // 相对路径
                    String absName = StringUtils.substringAfter(file.getAbsolutePath(), root.getAbsolutePath());
                    String[] ss = absName.split(File.separator);
                    ss[ss.length - 1] = FilenameUtils.getBaseName(fn);
                    String[] transSS = trans(jpMap, ss);

                    String target = output.getAbsolutePath() + StringUtils.join(transSS, File.separator) + "." + ext;
                    rename(file, new File(target));
                }

            } else if (file.isDirectory()) {
                exec(file, jpMap);
            }
        }
    }

    private void rename(File file, File target) {
        log.debug(target);
        try {
            FileUtils.copyFile(file, target);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    private String[] trans(Map<String, Language> jpMap, String[] jps) {
        String[] ens = new String[jps.length];
        for (int i = 0; i < ens.length; i++) {
            Language lan = jpMap.get(jps[i]);
            if (lan != null) {
                String en = lan.getEn();
                ens[i] = en;
                // System.out.println(en);
            } else {
                // System.err.println(jps[i]);
                transFail.add(jps[i]);
                ens[i] = jps[i];
            }
        }
        return ens;
    }
}
