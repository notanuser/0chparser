package com.nulchan.parsers;

import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.ThreadEntity;

/**
 * Парсер для треда с доски.
 */
public final class ThreadParser extends Parser<ThreadEntity> {
	@Override
	public ThreadEntity parse(final Element element) throws ParseException {
		try {
			Element currentElement = element.getElementsByClass("postername")
					.get(0);
			Elements currentElements;
			String name = currentElement.text();
			boolean isSaged = currentElement.html().contains("mailto:sage");
			currentElements = element.getElementsByClass("filetitle");
			String title = currentElements.isEmpty() ? "" : currentElements
					.get(0).text();
			currentElements = element.getElementsByClass("postertrip");
			String trip = currentElements.isEmpty() ? "" : currentElements.get(
					0).text();
			String date = element.getAllElements().get(2)
					.getElementsByTag("label").get(0).ownText();
			String id = element.getElementsByClass("reflink").get(0)
					.getElementsByTag("a").get(1).text();
			currentElement = element.getElementsByClass("postnode").get(0);
			String text = currentElement.getElementsByTag("blockquote").get(0)
					.text()
					+ getVideo(element);
			currentElements = currentElement.getElementsByTag("a");
			String image = currentElements.size() > 1 ? makeUrl(currentElements
					.get(1).attr("href")) : "";
			currentElements = currentElement.getElementsByTag("img");
			String thumb = currentElements.isEmpty() ? ""
					: makeUrl(currentElements.get(0).attr("src"));
			currentElements = element.getElementsByClass("omittedposts");
			int posts = 1;
			int images = image.isEmpty() ? 0 : 1;
			if (!currentElements.isEmpty()) {
				Pattern pattern = Pattern.compile("\\D+");
				String matches[] = pattern.split(currentElements.get(0).text());
				posts += Integer.parseInt(matches[0]);
				if (matches.length == 2)
					images += Integer.parseInt(matches[1]);
			}
			currentElements = element.getElementsByClass("postnode");
			for (Element elem : currentElements) {
				// TODO something is terribly wrong with this. I don't even
				// know.
				posts++;
				if (!elem.getElementsByTag("img").isEmpty())
					images++;
			}

			return new ThreadEntity(id, image, thumb, name, isSaged, date,
					title, trip, text, posts, images);
		} catch (Exception ex) {
			throw new ParseException(this.getClass().getName());
		}
	}
}
