package com.nulchan;

import java.util.LinkedList;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.PostContainer;
import com.nulchan.objects.ThreadContainer;
import com.nulchan.parsers.IParser;
import com.nulchan.parsers.NewPostParser;



final class NewPostFetcher extends Fetcher<PostContainer> {

	/**
	 * Инициализирует экземпляр класса, используя заданные доску, тред и сообщение.
	 * @param board доска для загрузки.
	 * @param thread тред из которого загружать.
	 * @param lastPost сообщение, с которого начинать загрузку.
	 */
	public NewPostFetcher(String board, ThreadContainer thread, PostContainer lastPost) {
		super(String.format(Board.Settings.baseUrl 
				+ "/expand.php?after=%s&board=%s&threadid=%s"
				, lastPost.getId(), board, thread.getId()));
	}
	
	
	@Override
	protected PostContainer[] parseHtmlDocument(Document html)
			throws ParseException {
		LinkedList<PostContainer> posts = new LinkedList<>();
		IParser<PostContainer> parser = new NewPostParser();
		for (Element e : html.getElementsByClass("reply"))
			posts.add(parser.parse(e));
		return posts.size() > 0 ? 
				posts.toArray(new PostContainer[posts.size()]) : null;
	}
	
}