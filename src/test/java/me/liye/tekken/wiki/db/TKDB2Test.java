package me.liye.tekken.wiki.db;

import me.liye.tekken.wiki.doamin.Language;
import org.junit.Test;

import java.util.Map;

/**
 * Created by liye on 18/05/2017.
 *
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class TKDB2Test {
    @Test
    public void getJpLanguageMap() throws Exception {
        Map<String, Language> map = TKDB2.INSTANCE.getJpLanguageMap();

        System.out.println(map);

        System.out.println(map.get("コンボ").getCn());
    }

}
