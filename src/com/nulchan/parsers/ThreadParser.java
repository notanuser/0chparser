package com.nulchan.parsers;

import java.util.regex.Pattern;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.ThreadContainer;


/**
 * Парсер для треда с доски.
 */
public final class ThreadParser extends Parser<ThreadContainer> {
	@Override
	public ThreadContainer parse(final Element element) throws ParseException {
		try {
			Element currentElement = element.getElementsByClass("postername").get(0);
			String name = currentElement.text();
			boolean isSaged = currentElement.html().contains("mailto:sage");
			Elements currentElements = currentElement.getElementsByTag("label");
			String title = currentElements.isEmpty() ? "" : currentElements.get(0).text();
			currentElements = element.getElementsByClass("postertrip");
			String trip = currentElements.isEmpty() ? "" : currentElements.get(0).text();
			String date = element.getAllElements().get(2).getElementsByTag("label").get(0).text();
			date = date.substring(name.length()+1);
			String id = element.getElementsByClass("reflink").get(0).getElementsByTag("a").get(1).text();
			currentElement = element.getElementsByClass("postnode").get(0);
			String text = currentElement.getElementsByTag("blockquote").get(0).text() + getVideo(element);
			currentElements = currentElement.getElementsByTag("a");			
			String image = currentElements.size() > 1 ? currentElements.get(1).attr("href") : "";
			currentElements = currentElement.getElementsByTag("img");
			String thumb = currentElements.isEmpty() ? "" : currentElements.get(0).attr("src");
			currentElements = element.getElementsByClass("omittedposts");
			int posts = 1;
			int images = image.isEmpty() ? 0 : 1;
			if(!currentElements.isEmpty()) {
				Pattern pattern = Pattern.compile("\\D+");
				String matches[] = pattern.split(currentElements.get(0).text());
				posts+=Integer.parseInt(matches[0]);
				if(matches.length==2)
					images+=Integer.parseInt(matches[1]);
			}
			currentElements = element.getElementsByClass("postnode");
			for(Element elem : currentElements) { 
				//TODO something is terribly wrong with this. I don't even know.
				posts++;
				if(!elem.getElementsByTag("img").isEmpty())
					images++;
			}
			
			return new ThreadContainer(id, image, thumb, name, isSaged, date, title, trip, text, posts, images);
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new ParseException(this.getClass().getName());
		}
	}
}
