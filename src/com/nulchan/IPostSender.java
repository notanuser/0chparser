package com.nulchan;

import com.nulchan.exceptions.BoardException;

public interface IPostSender {

	/**
	 * Получает изображение капчи в виде массива байт.
	 * 
	 * @return массив байт с изображением капчи.
	 * @throws BoardException
	 *             вызывается, если капчу не загрузить.
	 */
	public abstract byte[] getCaptcha() throws BoardException;

	/**
	 * Отправляет сообщение.
	 * 
	 * @param captcha
	 *            подтверждение.
	 * @throws BoardException
	 *             вызывается при ошибке подключения или отправки сообщения.
	 */
	public abstract void send(String captcha) throws BoardException;

}