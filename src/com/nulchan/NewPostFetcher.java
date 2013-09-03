package com.nulchan;

import java.util.LinkedList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.PostEntity;
import com.nulchan.objects.ThreadEntity;
import com.nulchan.parsers.IParser;
import com.nulchan.parsers.NewPostParser;

final class NewPostFetcher extends Fetcher<PostEntity> {

	/**
	 * Инициализирует экземпляр класса, используя заданные доску, тред и
	 * сообщение.
	 * 
	 * @param board
	 *            доска для загрузки.
	 * @param thread
	 *            тред из которого загружать.
	 * @param lastPost
	 *            сообщение, с которого начинать загрузку.
	 */
	public NewPostFetcher(String board, ThreadEntity thread, PostEntity lastPost) {
		super(String.format("%s/expand.php?after=%s&board=%s&threadid=%s",
				Board.Settings.getFullUrl(),
				lastPost.getId(), board, thread.getId()));
	}

	@Override
	protected PostEntity[] parseHtmlDocument(Document html)
			throws ParseException {
		LinkedList<PostEntity> posts = new LinkedList<PostEntity>();
		IParser<PostEntity> parser = new NewPostParser();
		for (Element e : html.getElementsByClass("reply"))
			posts.add(parser.parse(e));
		return posts.size() > 0 ? posts.toArray(new PostEntity[posts.size()])
				: null;
	}

}