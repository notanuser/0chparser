package com.nulchan.parsers;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nulchan.Board;
import com.nulchan.objects.PostEntity;

abstract class Parser<T extends PostEntity> implements IParser<T> {

	protected final String getVideo(final Element element) {
		try {
			Elements currentElements = element.getElementsByTag("object");
			return currentElements.isEmpty() ? "" : "\n"
					+ makeUrl(currentElements.get(0).getElementsByTag("embed")
							.get(0).attr("src"));
		} catch (Exception e) {
			return "";
		}
	}

	protected final String makeUrl(final String str) {
		return str.startsWith("//") ? str.replaceFirst("//",
				Board.Settings.protocol) : str;
	}
}
