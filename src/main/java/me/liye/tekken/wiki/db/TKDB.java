package me.liye.tekken.wiki.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.liye.tekken.wiki.Config;
import me.liye.tekken.wiki.doamin.Language;
import me.liye.tekken.wiki.doamin.SkillEntry;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */

public class TKDB extends DB {

    public static void main(String[] args) {
        Map<String, Language> obj = TKDB.INSTANCE.getJpLanguageMap();
        System.out.println(obj);
    }

    public static TKDB INSTANCE = new TKDB();

    private TKDB() {
        super.open(Config.dbFile);
    }

    @Override
    protected String[] getDDL() {
        return new String[0];
    }

    public Map<String, Language> getJpLanguageMap() {
        List<Map<String, String>> records = select("select * from language");
        Map<String, Language> result = new HashMap<String, Language>();
        for (Map<String, String> line : records) {
            Language lan = new Language();
            lan.setCn(line.get("cn"));
            lan.setJp(line.get("jp"));
            lan.setEn(line.get("en"));

            result.put(lan.getJp(), lan);
        }
        return result;

    }

    public void insertSkillEntry(SkillEntry sk) {
        try {
            insertObject("skill", sk);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteAllSkill() {
        try {
            return update("delete from skill", (String) null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
