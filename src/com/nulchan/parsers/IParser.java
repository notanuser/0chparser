package com.nulchan.parsers;


import org.jsoup.nodes.Element;

import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.PostContainer;

/**
 * Предоставляет интерфейс для обработки сообщений.
 */
public interface IParser<T extends PostContainer> {
	/**
	 * Преобразует передаваемый элемент {@link Element} в сообщение {@link PostContainer}. 
	 * @param element элемент для парсинга.
	 * @return сообщение.
	 * @throws ParseException вызывается при ошибке парсинга.
	 */
	T parse(final Element element) throws ParseException;
}
