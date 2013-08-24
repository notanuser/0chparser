package com.nulchan.objects;

/**
 * @see PostEntity
 */
public final class ThreadEntity extends PostEntity {
	
	int images, posts;
	
	/**
	 * Конструктор, используемый парсером.
	 * @param id номер треда
	 * @param attachment вложение
	 * @param thumbnail миниатюра
	 * @param poster имя отправителя
	 * @param saged мудрец идетя во все поля
	 * @param date дата отправки
	 * @param subject тема сообщения
	 * @param trip трипкод, лол
	 * @param text текст сообщения
	 * @param posts количество сообщений в треде 
	 * @param images количество вложений в треде
	 */
	public ThreadEntity(String id, String attachment, String thumbnail, String poster,
			boolean saged, String date, String subject, String trip, String text, int posts, int images) {
		super(id, attachment, thumbnail, poster, saged, date, subject, trip, text);
		this.images = images;
		this.posts = posts;
	}
	
	/**
	 * Получает число изображений в треде.
	 */
	public int getImageCount() {
		return images;
	}
	
	/**
	 * Получает число сообщений в треде.
	 */
	public int getPostCount() {
		return posts;
	}
	
	
	@Override
	public String toString() {
		return  super.toString() + "\tEfgThread [id="+ id +", images=" + images + ", posts=" + posts + "]";
	}
	
	
}
