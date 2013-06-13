import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class DelSvn {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        del(new File("/Users/liye/work/uic-modules-datasource.bk"));
    }

    static void del(File dir) throws IOException {
        if (dir.getName().equals(".svn")) {
            System.out.println(dir);
            FileUtils.deleteDirectory(dir);
        } else {
            for (File f : dir.listFiles()) {
                if (f.isDirectory()) {
                    del(f);
                }
            }
        }
    }

}
