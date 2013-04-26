package me.liye.tekken.wiki.spider;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import me.liye.tekken.wiki.spider.Spider.Executor;

import org.w3c.dom.Node;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class NodeUtils {

    // 空节点
    public static boolean isNotBlankNode(Node node) {
        return !(node.getNamespaceURI() == null && node.getNodeName().equals("#text") && node.getTextContent().trim().length() == 0);
    }

    public static void showNode(Node node, String perfix, PrintStream ps) {
        if (node == null) {
            return;
        }
        ps.println(perfix + node.getNamespaceURI() + " " + node.getNodeName());
        NodeIterator ni = new NodeIterator(node);
        for (Node child : ni) {
            showNode(child, perfix + perfix, ps);
        }

    }

    public static boolean isHave(Node node, String xpath) {
        return new Spider(node, xpath).isHave();
    }

    public static int childCount(Node node, String xpath) {
        final AtomicInteger count = new AtomicInteger(0);
        new Spider(node, xpath).execute(new Executor() {

            @Override
            public void process(Node node, int index) throws Exception {
                count.incrementAndGet();
            }
        });
        return count.get();
    }

    public static List<Node> children(Node node, String xpath) {
        final List<Node> children = new ArrayList();
        new Spider(node, xpath).execute(new Executor() {

            @Override
            public void process(Node node, int index) throws Exception {
                children.add(node);
            }
        });
        return children;

    }
}
