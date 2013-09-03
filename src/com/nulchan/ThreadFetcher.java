package com.nulchan;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.ThreadEntity;
import com.nulchan.parsers.IParser;
import com.nulchan.parsers.ThreadParser;

final class ThreadFetcher extends Fetcher<ThreadEntity> {

	/**
	 * 
	 * @param path
	 *            путь загрузки
	 */
	public ThreadFetcher(String path) {
		super(path);
	}

	@Override
	protected ThreadEntity[] parseHtmlDocument(Document html)
			throws ParseException {
		List<ThreadEntity> posts = new ArrayList<ThreadEntity>();
		Elements elements = html.getElementsByAttributeValueStarting("id",
				"thread");
		IParser<ThreadEntity> parser = new ThreadParser();
		for (Element e : elements) {
			posts.add(parser.parse(e));
		}
		return posts.toArray(new ThreadEntity[posts.size()]);
	}
}