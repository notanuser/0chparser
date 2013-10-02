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
		this("Если вы это видите, значит  парсер обрабатывает что-то не то. Попробуйте что-нибудь другое.\n Ошибка "
				+ where, null);
	}

	public ParseException(String message, Throwable cause) {
		super("Если вы это видите, значит  парсер обрабатывает что-то не то. Попробуйте что-нибудь другое.\n" + message, cause);
	}
}
