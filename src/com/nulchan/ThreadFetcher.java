package com.nulchan;

import java.util.ArrayList;
import java.util.List;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.ThreadContainer;
import com.nulchan.parsers.IParser;
import com.nulchan.parsers.ThreadParser;


final class ThreadFetcher extends Fetcher<ThreadContainer> {

	/**
	 * 
	 * @param path путь загрузки
	 */
	public ThreadFetcher(String path){
		super(path);
	}
	@Override
	protected ThreadContainer[] parseHtmlDocument(Document html) throws ParseException {
		List<ThreadContainer> posts = new ArrayList<ThreadContainer>();
		Elements elements = html.getElementsByAttributeValueStarting("id", "thread");
		IParser<ThreadContainer> parser = new ThreadParser();
		for(Element e : elements) {
			posts.add(parser.parse(e));
		}
		return posts.toArray(new ThreadContainer[posts.size()]);
	}
}