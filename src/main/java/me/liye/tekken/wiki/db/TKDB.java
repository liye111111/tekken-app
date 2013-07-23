package me.liye.tekken.wiki.db;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.liye.tekken.wiki.Config;
import me.liye.tekken.wiki.doamin.Language;
import me.liye.tekken.wiki.doamin.SkillEntry;

import org.apache.commons.beanutils.PropertyUtils;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */

public class TKDB extends DB {

    public static void main(String[] args) {
        Map<String, Language> obj = TKDB.INSTANCE.getJpLanguageMap();
        System.out.println(obj);
        List<SkillEntry> sks = TKDB.INSTANCE.listSkill(null, null);
        for (SkillEntry sk : sks) {
            System.out.println(sk);
        }
    }

    public static TKDB INSTANCE = new TKDB();

    private TKDB() {
        super.open(Config.dbFile);
    }

    @Override
    protected String[] getDDL() {
        return new String[0];
    }

    public List<SkillEntry> listSkill(String category, String charactor) {
        List<SkillEntry> result = new ArrayList();
        String sql = "select * from skill where 1=1";
        sql = category == null ? sql : sql + " and category=? ";
        sql = charactor == null ? sql : sql + " and charactor=? ";
        String[] param = null;
        if (category != null && charactor != null) {
            param = new String[] { category, charactor };
        } else if (category != null) {
            param = new String[] { category };
        } else if (charactor != null) {
            param = new String[] { charactor };
        }
        List<Map<String, String>> rows = select(sql, param);
        for (Map<String, String> row : rows) {
            SkillEntry sk = new SkillEntry();
            PropertyDescriptor[] ps = PropertyUtils.getPropertyDescriptors(sk);
            for (PropertyDescriptor p : ps) {
                String displayName = p.getDisplayName();
                if ("class".equals(displayName)) {
                    continue;
                }
                Object val = row.get(displayName);
                try {
                    PropertyUtils.setProperty(sk, displayName, val);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            result.add(sk);
        }
        return result;
    }

    public Map<String, Language> getJpLanguageMap() {
        List<Map<String, String>> records = select("select * from language", null);
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
            insertObject("skill2", sk);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insertSkillEntry(SkillEntry sk, String tbName) {
        try {
            insertObject(tbName, sk);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteAllSkill() {
        try {
            return update("delete from skill", null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
