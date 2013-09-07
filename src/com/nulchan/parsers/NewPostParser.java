package com.nulchan.parsers;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.PostEntity;

/**
 * Парсер для новых сообщений в треде.
 */
public final class NewPostParser extends Parser<PostEntity> {

	@Override
	public PostEntity parse(final Element element) throws ParseException {
		try {
			Elements currentElements;
			Element currentElement;
			String id = element.getElementsByTag("a").attr("name");
			currentElements = element.getElementsByTag("a");
			//String image = "", thumb = "";
			//currentElements = element.getElementsByAttributeValue("target", "_blank");
			String attachment[] = getAttachment(element);
			/*for(Element e : currentElements)
				if(e.children().hasClass("thumb")) {
					image = makeUrl(e.attr("href"));
					thumb = makeUrl(e.getElementsByClass("thumb").get(0).attr("src"));
				}*/
			/*currentElements = element.getElementsByTag("img");
			String thumb = currentElements.isEmpty() ? ""
					: makeUrl(currentElements.get(0).attr("src"));*/
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
			String text = element.getElementsByClass("postmessage").get(0)
					.text()
					+ getVideo(element);
			return new PostEntity(id, attachment[1], attachment[0], name, isSaged, date, title,
					trip, text);
		} catch (Exception e) {
			throw new ParseException(this.getClass().getName());
		}
	}

}
