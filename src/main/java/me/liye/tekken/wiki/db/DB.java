package me.liye.tekken.wiki.db;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public abstract class DB {

    private final static Logger log = Logger.getLogger(DB.class);

    protected String            name;
    Connection                  conn;

    public void open(String dbFile) {
        try {
            Class.forName("org.sqlite.JDBC");
            new File(dbFile).getParentFile().mkdirs();
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void init() throws Exception {
        log.info("db init! " + name);

        for (String ddl : getDDL()) {
            Statement st = conn.createStatement();
            try {
                st.execute(ddl);
            } catch (Exception e) {
                log.warn("exception when init ddl : " + ddl + " ," + e.getMessage());
            } finally {
                st.close();
            }
        }

    }

    public int[] insertOrUpdate(String insertSql, String[] insertParam, String updateSql, String[] updateParam)
                                                                                                               throws SQLException {
        int[] rs = new int[2];
        int ct = update(updateSql, updateParam);
        rs[0] = ct;
        if (ct == 0) {
            rs[1] = insert(insertSql, insertParam);
        }
        return rs;
    }

    public int insertObject(String tableName, Object obj) throws Exception {
        // 反射获取字段名
        Class<? extends Object> clz = obj.getClass();
        Field[] allFs = clz.getDeclaredFields();
        List<Field> fsList = new ArrayList<Field>();
        // 去掉 transent
        for (Field f : allFs) {
            if (!Modifier.isTransient(f.getModifiers())) {
                fsList.add(f);
            }
        }
        Field[] fs = fsList.toArray(new Field[0]);

        String[] columns = new String[fs.length];
        String[] q = new String[fs.length];
        String[] vals = new String[fs.length];

        for (int i = 0; i < fs.length; i++) {
            columns[i] = fs[i].getName();
            q[i] = "?";
        }

        String sql = String.format("insert into \"%s\" (\"%s\") values (%s)", tableName,
                                   StringUtils.join(columns, "\",\""), StringUtils.join(q, ","));

        for (int i = 0; i < columns.length; i++) {
            String col = columns[i];
            String upperCol = StringUtils.capitalize(col);
            Method m = clz.getMethod("get" + upperCol, (Class<String>) null);
            Object val = m.invoke(obj, (Class<String>) null);
            vals[i] = val == null ? null : val.toString();
        }

        return insert(sql, vals);

    }

    public int insert(String insertSql, String... insertParam) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(insertSql);
        for (int i = 0; i < insertParam.length; i++) {
            ps.setString(i + 1, insertParam[i]);
        }
        int ct = ps.executeUpdate();
        ps.close();
        return ct;
    }

    public int update(String updateSql, String... updateParam) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(updateSql);
        for (int i = 0; updateParam != null && i < updateParam.length; i++) {
            ps.setString(i + 1, updateParam[i]);
        }
        int ct = ps.executeUpdate();
        ps.close();
        return ct;
    }

    public List<Map<String, String>> select(String selectSql, String... queryParam) {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        try {
            PreparedStatement ps = conn.prepareStatement(selectSql);
            for (int i = 0; queryParam != null && i < queryParam.length; i++) {
                ps.setString(i + 1, queryParam[i]);
            }
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                Map<String, String> row = new HashMap<String, String>();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    row.put(md.getColumnName(i), rs.getString(i));
                }
                data.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    protected abstract String[] getDDL();

}
