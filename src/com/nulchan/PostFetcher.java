package com.nulchan;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.PostEntity;
import com.nulchan.objects.ThreadEntity;
import com.nulchan.parsers.IParser;
import com.nulchan.parsers.OPParser;
import com.nulchan.parsers.PostParser;

final class PostFetcher extends Fetcher<PostEntity> {

	/**
	 * Инициализирует загрузчик для заданной доски и треда
	 * 
	 * @param boardUrl
	 *            путь к доске (например, "http://0chan.hk/0/")
	 * @param thread
	 *            тред
	 */
	public PostFetcher(String boardUrl, ThreadEntity thread) {
		super(boardUrl + "/res/" + thread.getId() + ".html");
	}

	@Override
	protected PostEntity[] parseHtmlDocument(Document html)
			throws ParseException {
		List<PostEntity> posts = new ArrayList<PostEntity>();
		final Elements containers = html.getElementsByClass("postnode");
		IParser<PostEntity> parser = new OPParser();
		posts.add(parser.parse(containers.first()));
		parser = new PostParser();
		for (int i = 1; i < containers.size(); ++i)
			posts.add(parser.parse(containers.get(i)));
		return posts.toArray(new PostEntity[posts.size()]);
	}
}