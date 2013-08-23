package com.nulchan;

import java.io.IOException;

import org.jsoup.Connection.Response;

import org.jsoup.Jsoup;

import com.nulchan.exceptions.BoardException;
import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.PostContainer;



abstract class Fetcher<T extends PostContainer> implements IFetcher<T> {
	String uri; //страничка.
	
	/**
	 * Ну ты понял. Создаем загрузчик, устанавливаем страницу загрузки.
	 * @param path Путь полный до цели, блин.
	 */
	public Fetcher(final String path){
			uri = path;
	}
	/**
	 * Получаем html документ из Jsoup (работаем в Jsoup формате).
	 * @return Jsoup Document
	 * @throws IOException если что-то не так.
	 * @throws BoardException 
	 */
	private org.jsoup.nodes.Document fetchURL() throws BoardException {
		try {
			Response response = Jsoup
					.connect(uri)
					.userAgent(Board.Settings.userAgent)
					.maxBodySize(3145728)
					.execute();
			if(response.statusCode() == 404)
				throw new BoardException(response.statusMessage());
			return Jsoup.parse(response.body(), uri);
		} catch (Exception e) {
			//e.printStackTrace();
			throw new BoardException(e.getMessage());
		}
	}
	/**
	 * Парсит заданный документ в массив.
	 * @param html Что парсим
	 * @return массив сообщений
	 * @throws BoardException 
	 */
	protected abstract T[] parseHtmlDocument(final org.jsoup.nodes.Document html) throws ParseException;
	
	@Override
	public T[] fetch() throws BoardException, ParseException{			
		return parseHtmlDocument(fetchURL());
	}
}