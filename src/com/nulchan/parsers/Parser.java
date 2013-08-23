package com.nulchan.parsers;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nulchan.objects.PostContainer;


abstract class Parser<T extends PostContainer> implements IParser<T>{

	protected final String getVideo(final Element element) {
		try {
			Elements currentElements = element.getElementsByTag("object");
			return currentElements.isEmpty() ? "" : "\n"
					+ currentElements.get(0).getElementsByTag("embed")
					.get(0).attr("src");
		} catch (Exception e) {
			return "";
		}
	}
}
