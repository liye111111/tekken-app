package me.liye.tekken.wiki.tk7fr;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by liye on 22/05/2017.
 *
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class PicRename {
    public static void main(String[] args) throws IOException {
        File src = new File("/Users/liye/github/tekken-app/src/main/resources/www/img/tk7fr");
        File dest = new File("/Users/liye/github/tekken-app/src/main/resources/www/img/fr");
        for(File f : src.listFiles()){
            if(f.getName().endsWith(".png")){
                System.out.println(f);
                if(f.getName().startsWith("150px-T7")){
                    String newName = "T7FR" + f.getName().substring("150px-T7".length());
                    System.out.println(newName);
                    FileUtils.copyFile(f,new File(dest,newName));
                }
            }
        }
    }
}
