package me.liye.tekken.wiki.trans.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Dictionary {

    // 翻译对照表
    Map<String, String>          fromTo = new HashMap();

    //
    String[]                     keywords;
    String                       type;
    Map<Character, List<String>> kwMap;

    public Dictionary(String type, File file) {

        List<String> ln = null;
        try {
            ln = FileUtils.readLines(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String row : ln) {
            if (row.trim().length() == 0) {
                continue;
            }
            if (row.startsWith("#")) {
                // comment
            } else {
                String[] ss = StringUtils.split(row, "##");
                if (ss.length < 1) {
                    throw new RuntimeException("wrong mapping! " + row);
                }
                if (ss.length == 1) {
                    fromTo.put(ss[0].trim(), " ");
                } else {
                    fromTo.put(ss[0].trim(), ss[1].trim());
                }
            }
        }
        // ////
        String[] fromKws = fromTo.keySet().toArray(new String[0]);
        this.keywords = fromKws;
        this.type = type;
        kwMap = list2map(fromKws);

    }

    public Dictionary(String type, String[] keywords) {
        this.keywords = keywords;
        this.type = type;
        kwMap = list2map(keywords);
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        type = type;
    }

    public Map<Character, List<String>> getKwMap() {
        return kwMap;
    }

    public void setKwMap(Map<Character, List<String>> kwMap) {
        this.kwMap = kwMap;
    }

    private Map<Character, List<String>> list2map(String[] ss) {
        HashMap<Character, List<String>> result = new HashMap();
        for (String btn : ss) {
            List<String> ls = result.get(btn.charAt(0));
            if (ls == null) {
                ls = new ArrayList();
                result.put(btn.charAt(0), ls);
            }
            ls.add(btn);
        }
        for (List ls : result.values()) {
            Collections.sort(ls, new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    return o2.length() - o1.length();
                }
            });
        }
        return result;
    }

    public String getTo(String from) {
        return fromTo.get(from);
    }

}
