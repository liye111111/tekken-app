package me.liye.tekken.wiki.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.liye.tekken.wiki.doamin.Language;

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
        String dbFile = System.getProperty("tekken-app.db",
                                           "/Users/liye/mywork/tekken-app/src/main/resources/tekken.sqlite");
        super.open(dbFile);
    }

    @Override
    protected String[] getDDL() {
        return new String[0];
    }

    public Map<String, Language> getJpLanguageMap() {
        List<Map<String, String>> records = select("select * from language");
        Map<String, Language> result = new HashMap();
        for (Map<String, String> line : records) {
            Language lan = new Language();
            lan.setCn(line.get("cn"));
            lan.setJp(line.get("jp"));
            lan.setEn(line.get("en"));

            result.put(lan.getJp(), lan);
        }
        return result;

    }

}
