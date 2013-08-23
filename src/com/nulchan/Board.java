package com.nulchan;

import java.io.IOException;
import java.util.Map;


import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.nulchan.exceptions.BoardException;
import com.nulchan.objects.PostContainer;
import com.nulchan.objects.ThreadContainer;

public class Board {
	public abstract static class Settings {
		protected final static String baseUrl = "http://0chan.hk/";
		/**
		 * Список досок.
		 */
		public final static String[] boards =
			{"a", "b", "d", "r", "0", "e", "t", "hw", "s", "c", "vg", "8", "bg","wh", "au", "bo"
			, "co", "cook", "f", "fa", "fl", "m", "med", "ph", "tv", "wp", "war", "h", "g", "fur" };
		protected static Map<String, String> cookies;
		/**
		 * Пароль на удаление.
		 */
		public static String password = "";
		protected final static String userAgent = "Mozilla";
		/**
		 * Проверяет, установлены ли куки.
		 * @return <code>true</code> - если куки установлены, <code>false</code> если куки не установлены.
		 */
		protected static boolean isSetCookies() {
			return cookies != null && !cookies.isEmpty();
		}
		
	}

	String board;
	int page;
	String url;
	
	/**
	 * Инициализация выбранной доски.
	 * @param board доска.
	 * @throws BoardException Если не удалось подключиться.
	 */
	public Board(String board) throws BoardException {
		this.board = board;
		this.url = Settings.baseUrl + board;
		page = 0;
		try {
			Response response = Jsoup.connect(url)
					.userAgent(Settings.userAgent)
					.referrer(Settings.baseUrl)
					.execute();
			if (response.statusCode() == 404)
				throw new BoardException(response.statusMessage());
			else if(!Settings.isSetCookies())
				Settings.cookies = response.cookies();
		} catch(IOException e) {
			throw new BoardException("Невозможно подключиться.");
		}
	}
	
	/**
	 * Создает загрузчик тредов.
	 * @return {@link IFetcher}, загружающий треды с доски.
	 */
	public IFetcher<ThreadContainer> getFetcher() {
		return new ThreadFetcher(page > 0 ? url+"/" + page + ".html" : url);
	}
	
	/**
	 * Создает загрузчик сообщений из треда.
	 * @param thread заданный тред.
	 * @return {@link IFetcher}, заггружающий сообщения треда.
	 */
	public IFetcher<PostContainer> getFetcher(ThreadContainer thread) {
		return new PostFetcher(url, thread);
	}
	
	/**
	 * Создает загрузчик для новых сообщений из треда.
	 * @param thread заданный тред
	 * @param post сообщение, с которого начинать загрузку.
	 * @return {@link IFetcher}, загружающий сообщения, идущие после заданного.
	 */
	public IFetcher<PostContainer> getFetcher(ThreadContainer thread, PostContainer post) {
		return new NewPostFetcher(board, thread, post);
	}
	/**
	 * Создает отправщик сообщений.
	 * @param thread тред в который отправляется сообщение.
	 * @param post отправляемое сообщение
	 * @return отправщик сообщений, служащий для отправки сообщения в тред.
	 */
	public IPostSender getSender(ThreadContainer thread, PostContainer post) {
		return new PostSender(board, thread, post);
	}
	
	/**
	 * Создает отправщик сообщений, для создания нового треда.
	 * @param OP отправляемое сообщение.
	 * @return отправщик сообщений, создающий новый тред.
	 */
	public IPostSender getThreadMaker(PostContainer OP) {
		return new PostSender(board, OP);
	}
	
	/**
	 * Установка страницы на доске, с которой загружать треды.
	 * @param page страница для загрузки (>=0).
	 */
	public void setPage(int page) {
		if(page>=0)
			this.page = page; 
	}
}
