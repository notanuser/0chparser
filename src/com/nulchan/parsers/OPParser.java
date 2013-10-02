package com.nulchan.parsers;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.PostEntity;

/**
 * Парсер первого сообщения в треде.
 */
public final class OPParser extends Parser<PostEntity> {

	@Override
	public PostEntity parse(final Element element) throws ParseException {
		try {
			Elements currentElements;
			Element currentElement;
			String id = element.getElementsByClass("reflink").get(0)
					.getElementsByTag("a").get(1).text();
			currentElements = element.getElementsByTag("a");
			/*String image = currentElements.size() > 1 ? makeUrl(currentElements
					.get(1).attr("href")) : "";
			currentElements = element.getElementsByTag("img");
			String thumb = currentElements.isEmpty() ? ""
					: makeUrl(currentElements.get(0).attr("src"));*/
			String attach[]=getAttachment(element);
			currentElement = element.getElementsByClass("postername").get(0);
			String name = currentElement.text();
			boolean isSaged = currentElement.html().contains("mailto:sage");
			currentElements = element.getElementsByClass("filetitle");
			String title = currentElements.isEmpty() ? "" : currentElements
					.get(0).text();
			currentElements = element.getElementsByClass("postertrip");
			String trip = currentElements.isEmpty() ? "" : currentElements.get(
					0).text();
			currentElements = element.getElementsByTag("label");
			String date = currentElements.get(0).ownText();
			String text = element.getElementsByTag("blockquote").get(0).text()
					+ getVideo(element);
			return new PostEntity(id, attach[1], attach[0], name, isSaged, date, title,
					trip, text);
		} catch (Exception ex) {
			throw new ParseException(this.getClass().getName(), ex);
		}
	}

}
