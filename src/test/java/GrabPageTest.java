import java.io.File;

import me.liye.tekken.wiki.grab.GrabPage;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class GrabPageTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String url = "http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7TBR ~/www/wiki/br";
        String path = "/tmp/www";
        new GrabPage(url, new File(path)).grab();
    }

}
