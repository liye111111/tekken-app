package me.liye.tekken.wiki.trans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.liye.tekken.wiki.Config;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Mapping {

    static Map<String, String>       jp2en;
    static Map<String, List<String>> type2words;

    static {
        jp2en = new HashMap();
        type2words = new HashMap();
        String file = Config.mappingFile;
        try {
            List<String> ln = FileUtils.readLines(new File(file));
            String type = "";
            for (String row : ln) {
                if (row.startsWith("#")) {
                    type = StringUtils.substringAfterLast(row, "#");
                } else {

                    String[] ss = StringUtils.split(row);
                    if (ss.length != 2) {
                        throw new RuntimeException("wrong format in mapping: " + row);
                    }
                    jp2en.put(ss[0], ss[1]);
                    // 该类型下的日式符号表
                    List<String> words = type2words.get(type);
                    if (words == null) {
                        words = new ArrayList();
                        type2words.put(type, words);
                    }
                    words.add(ss[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEn(String jp) {
        String en = jp2en.get(jp);
        return en == null ? jp : en;
    }

    public static List<String> getWords(String type) {
        return type2words.get(type);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(getEn("立ち途中"));
        System.out.println(getWords("button"));
    }

}
