package com.nulchan.parsers;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.PostContainer;

/**
 * Парсер сообщений треда.
 */
public final class PostParser extends Parser<PostContainer>{
	@Override
	public PostContainer parse(final Element element) throws ParseException {
		try{
			final Element content = element.getElementsByClass("reply").get(0);
			Element currentElement;
			Elements currentElements;
			String id = content.getElementsByTag("a").get(0).attr("name");
			currentElement = content.getElementsByClass("postername").get(0);
			String name = currentElement.text();
			boolean isSaged = currentElement.html().contains("mailto:sage");
			currentElements = currentElement.getElementsByTag("label");
			String title = currentElements.isEmpty() 
					? "" : currentElements.get(0).text();
			currentElements = element.getElementsByClass("postertrip");
			String trip = currentElements.isEmpty() 
					? "" : currentElements.get(0).text();
			String date = element.getAllElements().get(2)
					.getElementsByTag("label").get(0).ownText();
			currentElement = element.getElementsByClass("postnode").get(0);
			String text = content.getElementsByClass("postmessage")
					.get(0).text() + getVideo(content);
			currentElements = currentElement.getElementsByTag("a");			
			String image = currentElements.size() > 1 
					? currentElements.get(1).attr("href") : "";
			currentElements = currentElement.getElementsByTag("img");
			String thumb = currentElements.isEmpty() 
					? "" : currentElements.get(0).attr("src");
		return new PostContainer(id, image, thumb, name, isSaged, date
				, title, trip, text);
		} catch (Exception ex) {
			throw new ParseException(this.getClass().getName());
		}
	}
}
