package me.liye.tekken.wiki.spider;

import java.util.Iterator;

import org.w3c.dom.Node;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class NodeIterator implements Iterator<Node>, Iterable<Node> {

    Node node;
    int  i;

    public NodeIterator(Node node) {
        this.node = node;
        i = 0;
    }

    @Override
    public boolean hasNext() {
        return i < node.getChildNodes().getLength();
    }

    /**
     * 后退
     */
    public void backword() {
        i = i == 0 ? 0 : i - 1;
    }

    @Override
    public Node next() {
        Node child = node.getChildNodes().item(i);
        i++;
        if (NodeUtils.isNotBlankNode(child)) {
            return child;
        } else if (hasNext()) {
            return next();
        } else {
            return null;
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Node> iterator() {
        return this;
    }

}
