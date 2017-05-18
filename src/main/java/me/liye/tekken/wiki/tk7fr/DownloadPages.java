package me.liye.tekken.wiki.tk7fr;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;

import static me.liye.tekken.wiki.tk7fr.Config.*;


/**
 * Created by liye on 16/05/2017.
 *
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class DownloadPages {

    private static final Logger log = Logger.getLogger(DownloadPages.class);

   private static HttpClient hc = new DefaultHttpClient();
    private static final HashSet wgetUrls = new HashSet();

    public static void main(String[] args) throws Exception {
        log.info("cache dir:" + tmp);


        String content = wget(MAIN_URL,true);
        Utils.parseDom(content, XPATH_A, new ParseDomCallback4A() {
            @Override
            protected void callback(String href, String text) throws Exception {
                if(StringUtils.startsWith(href,host)){
                    System.out.println(href);


                    if(wgetUrls.contains(href)){
                        log.warn("found loop MAIN_URL:" + href);
                    }
                    else {
                        wget(href,true);
                    }
                }
            }
        });
    }

    public static  String wget(String url,boolean useCache) throws IOException {

        log.info("wget MAIN_URL:"+url);

        wgetUrls.add(url);

        String cacheFileName = URLEncoder.encode(url, "UTF-8");
        File cacheFile = new File(tmp, cacheFileName);

        String content = null;
        if (useCache && cacheFile.exists()) {
            log.debug("cache hit:" + cacheFileName);
            content = FileUtils.readFileToString(cacheFile);
        } else {
            log.debug("cache miss:" + cacheFileName);

            HttpGet get = new HttpGet(url);
            HttpResponse resp = hc.execute(get);

            if (resp.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("error " + resp.getStatusLine().getStatusCode());
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            resp.getEntity().writeTo(bos);

            content = new String(bos.toByteArray(), "EUC-JP");
            FileUtils.write(cacheFile, content);
        }
        return content;
    }



}
