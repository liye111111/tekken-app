package me.liye.tekken.www;

import java.io.File;

import me.liye.tekken.wiki.SavePage;

import org.apache.commons.io.FileUtils;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class BuildSite {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        BuildSite bs = new BuildSite();
        String url = "http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7TBR";
        bs.build("/Users/liye/www", "/Users/liye/mywork/tekken-app/src/main/resources/www", url, "br");
    }

    public void build(String wwwDir, String templateDir, String url, String type) throws Exception {
        File www = new File(wwwDir);
        if (!www.exists()) {
            www.mkdirs();
        }
        // index
        FileUtils.copyDirectory(new File(templateDir), www);
        File target = new File(new File(www, "tkwiki"), type);
        File tempalte = new File(templateDir, "page_template.htm");
        SavePage sp = new SavePage(url, target.getPath(), tempalte.getPath());
        sp.execute();

    }
}
