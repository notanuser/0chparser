package com.nulchan.parsers;



import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.PostContainer;

/**
 * Парсер для новых сообщений в треде.
 */
public final class NewPostParser extends Parser<PostContainer> {

    @Override
    public PostContainer parse(final Element element) throws ParseException {
    	try {
        String id = element.getElementsByTag("a").attr("name");
        Elements currentElements = element.getElementsByTag("a"); 
        String image = currentElements.size() > 3 ? currentElements.get(3).attr("href") : "";
        currentElements = element.getElementsByTag("img");
        String thumb = currentElements.isEmpty() ? "" : currentElements.get(0).attr("src"); 			
        Element currentElement = element.getElementsByClass("postername").get(0);
        String name = currentElement.text();
        boolean isSaged = currentElement.html().contains("mailto:sage");
        currentElements = element.getElementsByClass("filetitle");
        String title = currentElements.isEmpty() ? "" : currentElements.get(0).text();
        currentElements = element.getElementsByClass("postertrip");
        String trip = currentElements.isEmpty() ? "" : currentElements.get(0).text();
        currentElements = element.getElementsByTag("label");
        String date = currentElements.get(0).text().substring(name.length()+1);
        String text = element.getElementsByClass("postmessage").get(0).text() + getVideo(element);
        return new PostContainer(id, image, thumb, name, isSaged, date, title, trip, text);
    	} catch(Exception e) {
    		throw new ParseException(this.getClass().getName());
    	}
    }

}
