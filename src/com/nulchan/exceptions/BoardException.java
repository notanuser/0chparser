package com.nulchan.exceptions;

/**
 * Вызывается при ошибках подключения и загрузки. 
 */
public class BoardException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4033303727480082902L;
	/**
	 * Вызывает исключение с заданным сообщением об ошибке.
	 * @param message сообщение об ошибке.
	 */
	public BoardException(String message) {
		super("Ой, что-то не так: " + message);
	}

}
