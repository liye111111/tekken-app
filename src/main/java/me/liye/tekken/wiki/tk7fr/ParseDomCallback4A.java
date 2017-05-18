package me.liye.tekken.wiki.tk7fr;

import org.w3c.dom.Node;

/**
 * Created by liye on 17/05/2017.
 *
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public abstract class ParseDomCallback4A implements ParseDomCallback {
    @Override
    public void callback(Node node) throws Exception {
        String text = node.getTextContent();
        String href = null;
        Node hrefNode = node.getAttributes().getNamedItem("href");
        if (hrefNode != null) {
            href = hrefNode.getNodeValue();
        }
        callback(href,text);

    }

    protected abstract void callback(String href, String text) throws Exception;
}
