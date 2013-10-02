package com.nulchan;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.nulchan.exceptions.BoardException;
import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.PostEntity;

public abstract class Fetcher<T extends PostEntity> implements IFetcher<T> {
	String uri; // страничка.

	/**
	 * Ну ты понял. Создаем загрузчик, устанавливаем страницу загрузки.
	 * 
	 * @param path
	 *            Путь полный до цели.
	 */
	public Fetcher(final String path) {
		uri = path;
	}

	@Override
	public T[] fetch() throws BoardException, ParseException {
		return parseHtmlDocument(fetchURL());
	}

	/**
	 * Получаем html документ из Jsoup (работаем в Jsoup формате).
	 * 
	 * @return Jsoup Document
	 * @throws BoardException если что-то не так.
	 */
	protected org.jsoup.nodes.Document fetchURL() throws BoardException {
		try {
			Response response = Jsoup.connect(uri)
					.userAgent(Board.Settings.userAgent).maxBodySize(3145728)
					.execute();
			if (response.statusCode() == 404)
				throw new BoardException(response.statusMessage());
			return Jsoup.parse(response.body(), uri);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new BoardException(e.getMessage(), e);
		}
	}

	/**
	 * Парсит заданный документ в массив.
	 * 
	 * @param html
	 *            Что парсим
	 * @return массив сообщений
	 * @throws ParseException вызывается при ошибках парсера.
	 */
	protected abstract T[] parseHtmlDocument(final org.jsoup.nodes.Document html)
			throws ParseException;
}