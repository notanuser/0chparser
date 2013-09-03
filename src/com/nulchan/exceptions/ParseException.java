package com.nulchan.exceptions;

/**
 * Вызывается при ошибках парсинга элемента.
 */
public class ParseException extends Exception {

	private static final long serialVersionUID = -456143004042967497L;

	public ParseException() {
		this("Unknown Source");
	}

	public ParseException(String where) {
		super(
				"Если вы это видите, значит  парсер обрабатывает что-то не то. Попробуйте что-нибудь другое.\n Ошибка "
						+ where);
	}
}
