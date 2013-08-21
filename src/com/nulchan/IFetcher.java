package com.nulchan;

import java.io.IOException;

import com.nulchan.exceptions.BoardException;
import com.nulchan.exceptions.ParseException;
import com.nulchan.objects.PostContainer;



/**
 * Предоставляет возможность загрузки сообщений с доски.
 */
public interface IFetcher<T extends PostContainer> {
	/**
	 * Метод для получения сообщений. В зависимости от выбранной цели возвращает либо сообщения в треде, либо треды.
	 * @return Сообщения.
	 * @throws IOException
	 * @throws BoardException 
	 */
	T[] fetch() throws  ParseException, BoardException;
}
