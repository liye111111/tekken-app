package me.liye.tekken.wiki;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Config {

    private static final String KEY_RESOURCES = "resources";
    public static String        dbFile;
    public static String        mappingFile;
    // public static boolean onlyIndex;

    static {
        File cfg = new File(System.getProperty("user.home"), "tekken-app.properties");
        Properties p = new Properties();
        if (!cfg.exists()) {
            throw new RuntimeException("".format("%s not exist!", cfg));
        } else {
            try {
                p.load(new FileInputStream(cfg));
                String res = p.getProperty(KEY_RESOURCES);
                dbFile = new File(res, "tekken.sqlite").getPath();
                mappingFile = new File(res, "mapping.txt").getPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Config.dbFile);
    }
}
