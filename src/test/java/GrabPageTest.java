import me.liye.tekken.wiki.grab.GrabPage;

import java.io.File;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class GrabPageTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        String MAIN_URL = "http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7TBR";
        String url = "http://seesaawiki.jp/w/inatekken/d/%C1%ED%B9%E7TFR";
        String path = "/tmp/tk7fr";
        new GrabPage(url, new File(path)).grab();
    }

}
