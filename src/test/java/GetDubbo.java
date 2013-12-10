import me.liye.tekken.wiki.spider.Spider;
import me.liye.tekken.wiki.spider.Spider.Executor;

import org.w3c.dom.Node;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class GetDubbo {

    public static void main(String[] args) {
        String xpath = "//*[@id=\"table_o\"]";
        String url = "http://us-dubbo.vip.scl.en.alidc.net:8080/governance/addresses/172.20.226.108:59920/consumers";
        new Spider(url, xpath).execute(new Executor() {

            @Override
            public void process(Node node, int index) throws Exception {
                System.out.println(node.getTextContent());
            }
        });
    }
}
