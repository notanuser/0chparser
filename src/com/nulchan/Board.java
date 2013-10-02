package com.nulchan;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.nulchan.exceptions.BoardException;
import com.nulchan.objects.PostEntity;
import com.nulchan.objects.ThreadEntity;

public class Board {
	public abstract static class Settings {
		/**
		 * Список досок.
		 */
		public final static String[] boards = { "a", "b", "d", "r", "0", "e",
			"t", "hw", "s", "c", "vg", "8", "bg", "wh", "au", "bo", "co",
			"cook", "f", "fa", "fl", "m", "med", "ph", "tv", "wp", "war",
			"h", "g", "fur" };

		protected static Map<String, String> cookies;
		/**
		 * Пароль на удаление.
		 */
		public static String password = "";

		public final static String protocol = "http://";

		protected final static String domain = "0chan.hk";

		protected final static String userAgent = "Mozilla";

		/**
		 * Возвращает адрес сайта, комбинируя поля protocol и domain.
		 * @return
		 */
		protected final static String getFullUrl() {
			return protocol + domain + "/";
		}
		/**
		 * Проверяет, установлены ли куки.
		 * 
		 * @return <code>true</code> - если куки установлены, <code>false</code>
		 *         если куки не установлены.
		 */
		protected static boolean isSetCookies() {
			return cookies != null && !cookies.isEmpty();
		}

	}

	String board, url, name;
	int page;

	/**
	 * Инициализация выбранной доски.
	 * 
	 * @param board
	 *            доска (можно открывать определенную страницу (например, "b/1" установит первую страницу вместо нулевой)).
	 * @throws BoardException
	 *             Если не удалось подключиться.
	 */
	public Board(String board) throws BoardException {
		if(board == null || board.trim() == "")
			throw new BoardException("Не задана доска.");
		String split[] = board.split("/");
		this.board = split[0];
		if (split.length > 1)
			try {
				setPage(Integer.parseInt(split[1]));
			} catch (Exception e) {
			}
		else
			page = 0;
		url = Settings.getFullUrl() + this.board;
		try {
			Response response = Jsoup.connect(url)
					.userAgent(Settings.userAgent).referrer(Settings.getFullUrl())
					.execute();
			if (response.statusCode() == 404)
				throw new BoardException(response.statusMessage());
			else if (!Settings.isSetCookies())
				Settings.cookies = response.cookies();
			name = Jsoup.parse(response.body()).getElementsByTag("title")
					.get(0).ownText();
		} catch(SocketTimeoutException ste) {
			Throwable cause = ste.getCause();
			if(cause!=null && cause.getMessage()!=null)
				throw new BoardException("Невозможно подключиться. (" + cause.getMessage() + ")", cause);
			else if(ste.getMessage()!=null)
				throw new BoardException("Невозможно подключиться. (" + ste.getMessage() + ")", cause);
			else throw new BoardException("Невозможно подключиться.");
		}
		catch (IOException e) {
			String msg = e.getMessage() != null ? "(" + e.getMessage() + ")" : "";
			throw new BoardException("Невозможно подключиться. " + msg, e);
		}
	}

	/**
	 * Создает загрузчик тредов.
	 * 
	 * @return {@link IFetcher}, загружающий треды с доски.
	 */
	public IFetcher<ThreadEntity> getFetcher() {
		return new ThreadFetcher(page > 0 ? url + "/" + page + ".html" : url);
	}

	/**
	 * Создает загрузчик сообщений из треда.
	 * 
	 * @param thread
	 *            заданный тред.
	 * @return {@link IFetcher}, заггружающий сообщения треда.
	 */
	public IFetcher<PostEntity> getFetcher(ThreadEntity thread) {
		return new PostFetcher(url, thread);
	}

	/**
	 * Создает загрузчик для новых сообщений из треда.
	 * 
	 * @param thread
	 *            заданный тред
	 * @param post
	 *            сообщение, с которого начинать загрузку.
	 * @return {@link IFetcher}, загружающий сообщения, идущие после заданного.
	 */
	public IFetcher<PostEntity> getFetcher(ThreadEntity thread, PostEntity post) {
		return new NewPostFetcher(board, thread, post);
	}

	/**
	 * Возвращает название доски.
	 */
	public String getName() {
		return name == null ? "" : name;
	}

	/**
	 * Создает отправщик сообщений.
	 * 
	 * @param thread
	 *            тред в который отправляется сообщение.
	 * @param post
	 *            отправляемое сообщение
	 * @return отправщик сообщений, служащий для отправки сообщения в тред.
	 */
	public IPostSender getSender(ThreadEntity thread, PostEntity post) {
		return new PostSender(board, thread, post);
	};

	/**
	 * Создает отправщик сообщений, для создания нового треда.
	 * 
	 * @param OP
	 *            отправляемое сообщение.
	 * @return отправщик сообщений, создающий новый тред.
	 */
	public IPostSender getThreadMaker(PostEntity OP) {
		return new PostSender(board, OP);
	}

	/**
	 * Установка страницы на доске, с которой загружать треды.
	 * 
	 * @param page
	 *            страница для загрузки (>=0).
	 */
	public void setPage(int page) {
		if (page >= 0)
			this.page = page;
	}

	@Override
	public String toString() {
		return name;
	}
}
