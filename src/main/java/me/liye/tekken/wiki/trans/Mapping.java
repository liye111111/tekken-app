package me.liye.tekken.wiki.trans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Mapping {

    Map<String, String>       jp2en      = new HashMap();
    Map<String, List<String>> type2words = new HashMap();

    public void addFile(File file) {
        try {
            List<String> ln = FileUtils.readLines(file);
            String type = "";
            for (String row : ln) {
                if (row.trim().length() == 0) {
                    continue;
                }
                if (row.startsWith("#")) {
                    type = StringUtils.substringAfterLast(row, "#");
                } else {

                    String[] ss = StringUtils.substringsBetween(row, "\"", "\"");
                    if (ss != null) {
                        if (ss.length == 1) {
                            jp2en.put(ss[0], " ");
                        } else {
                            jp2en.put(ss[0], ss[1]);
                        }
                    } else {
                        ss = StringUtils.split(row);
                        if (ss.length < 1) {
                            throw new RuntimeException("wrong mapping! " + row);
                        }
                        if (ss.length == 1) {
                            jp2en.put(ss[0], " ");
                        } else {
                            jp2en.put(ss[0], ss[1]);
                        }
                    }
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

    public String getEn(String jp) {
        String en = jp2en.get(jp);
        return en == null ? jp : en;
    }

    //
    public List<String> getWords() {
        List<String> rt = new ArrayList();
        for (List ls : type2words.values()) {
            rt.addAll(ls);
        }
        return rt;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String[] left = StringUtils.substringsBetween("\"aaaa bbb\" \"aaaa bbb\"", "\"", "\"");
        for (int i = 0; i < left.length; i++) {
            System.out.println(left[i]);
        }

    }

}
