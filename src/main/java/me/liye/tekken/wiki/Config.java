package me.liye.tekken.wiki;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Config {

    private static final String KEY_DBFILE = "dbFile";

    private static final String DEF_DBFILE = "/Users/liye/mywork/tekken-app/src/main/resources/tekken.sqlite";

    public static String        dbFile;
    // public static boolean onlyIndex;

    static {
        File cfg = new File(System.getProperty("user.home"), "tekken-app.properties");
        Properties p = new Properties();
        if (!cfg.exists()) {
            dbFile = DEF_DBFILE;
            p.setProperty(KEY_DBFILE, dbFile);
            try {
                p.store(new FileOutputStream(cfg), "");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                p.load(new FileInputStream(cfg));
                dbFile = p.getProperty(KEY_DBFILE);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Config.dbFile);
    }
}
