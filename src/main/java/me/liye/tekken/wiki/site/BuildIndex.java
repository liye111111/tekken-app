package me.liye.tekken.wiki.site;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class BuildIndex {

    private static final Logger log = Logger.getLogger(BuildIndex.class);

    public static void main(String[] args) {
        new BuildIndex(new File("/Users/liye/www/wiki/tt2")).build();
    }

    File root;

    public BuildIndex(File root) {
        this.root = root;
    }

    private void build() {
        List<String> subNames = new ArrayList();
        File[] subs = root.listFiles();
        for (File sub : subs) {
            if (sub.isDirectory()) {
                subNames.add(sub.getName());
            }
        }
        Collections.sort(subNames);

    }

}
