package me.liye.tekken.wiki.trans;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import me.liye.tekken.wiki.db.TKDB;

import org.apache.commons.lang.StringUtils;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class M {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<Map<String, String>> rows = TKDB.INSTANCE.select("select distinct(command) from skill where command like '%中%'",
                                                              null);
        Set<String> poses = new TreeSet();
        for (Map<String, String> row : rows) {
            String cmd = row.values().iterator().next();
            poses.add(StringUtils.substringBeforeLast(cmd, "中") + "中");
        }
        for (String pose : poses) {
            System.out.println(pose + " " + pose);
        }
    }

}
