package me.liye.tekken.wiki.spider;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

public class XHTMLNamespaceContext implements NamespaceContext {

	@Override
	public String getNamespaceURI(String prefix) {
		return "http://www.w3.org/1999/xhtml";
	}

	@Override
	public String getPrefix(String namespaceURI) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator getPrefixes(String namespaceURI) {
		throw new UnsupportedOperationException();
	}

}
