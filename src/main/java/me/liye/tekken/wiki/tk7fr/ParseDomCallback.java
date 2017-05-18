package me.liye.tekken.wiki.tk7fr;

import org.w3c.dom.Node;

/**
 * Created by liye on 17/05/2017.
 *
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public interface ParseDomCallback {
    void callback(Node node) throws Exception;
}
