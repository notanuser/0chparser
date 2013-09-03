package com.nulchan.objects;

/**
 * Просто контейнер.
 */
public class PostEntity {

	String id, attachment, thumbnail, poster, date, subject, trip, text;
	boolean sage;

	/**
	 * Конструктор для создания сообщения.
	 * 
	 * @param attachment
	 *            путь к прикладываемому файлу.
	 * @param poster
	 *            имя отправителя
	 * @param subject
	 *            тема сообщения
	 * @param text
	 *            текст сообщения
	 * @param sage
	 *            мудрец идет во все поля.
	 */
	public PostEntity(String attachment, String poster, String subject,
			String text, boolean sage) {
		super();
		this.attachment = attachment;
		this.poster = poster;
		this.subject = subject;
		this.text = text;
		this.sage = sage;
		id = thumbnail = date = trip = null;
	}

	/**
	 * Конструктор, используемый парсером.
	 * 
	 * @param id
	 *            номер треда
	 * @param attachment
	 *            вложение
	 * @param thumbnail
	 *            миниатюра
	 * @param poster
	 *            имя отправителя
	 * @param saged
	 *            мудрец идетя во все поля
	 * @param date
	 *            дата отправки
	 * @param subject
	 *            тема сообщения
	 * @param trip
	 *            трипкод, лол
	 * @param text
	 *            текст сообщения
	 */
	public PostEntity(String id, String attachment, String thumbnail,
			String poster, boolean saged, String date, String subject,
			String trip, String text) {
		this(attachment, poster, subject, text, saged);
		this.id = id;
		this.thumbnail = thumbnail;
		this.date = date;
		this.trip = trip;
		this.sage = saged;
	}

	/**
	 * Получает путь к вложению.
	 * 
	 * @return путь к прикрепленному файлу в файловой системе или на сервере.
	 */
	public String getAttachment() {
		return attachment;
	}

	/**
	 * Получает дату отправки сообщения.
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Получает номер сообщения.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Получает имя отправителя.
	 */
	public String getPoster() {
		return poster;
	}

	/**
	 * Получает тему сообщения.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Получает текст сообщения.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Получает миниатюру
	 * 
	 * @return путь к миниатюре на сервере.
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * Получает трипкод отправителя.
	 */
	public String getTrip() {
		return trip;
	}

	/**
	 * Определяет, отмечено ли сообщение сажей.
	 * 
	 * @return <code>true</code> - если была добавлена сажа и <code>false</code>
	 *         - если нет.
	 */
	public boolean isSaged() {
		return sage;
	}

	@Override
	public String toString() {
		return "NulchPost [id=" + id + ", date=" + date + ", title=" + subject
				+ "]";
	}
}
