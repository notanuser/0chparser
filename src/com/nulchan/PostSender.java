package com.nulchan;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.nulchan.Board.Settings;
import com.nulchan.exceptions.BoardException;
import com.nulchan.objects.PostContainer;
import com.nulchan.objects.ThreadContainer;

public class PostSender implements IPostSender {
	static Charset charset = Charset.forName("UTF-8");
	final String threadId;
	final String board;
	final PostContainer post;
	Map<String,String> cookies;
	final DefaultHttpClient client;
	
	/**
	 * Создает отправщик и устанавливает его на отправку сообщения в тред.
	 * @param board доска для отправки.
	 * @param thread тред для отправки.
	 * @param post отправляемое сообщение.
	 */
	protected PostSender(String board, ThreadContainer thread, PostContainer post) {
		this.board = board;
		this.threadId = thread == null ? "0" : thread.getId();
		this.post = post;
		this.cookies = Board.Settings.isSetCookies() ? Board.Settings.cookies : null;
		client = new DefaultHttpClient();
    	client.setRedirectStrategy(new LaxRedirectStrategy());
	}
	
	/**
	 * Создает отправщик и устанавливает его на создание треда.
	 * @param board доска на которой создается тред.
	 * @param post оригинальное сообщение.
	 */
	protected PostSender(String board, PostContainer post) {
		this(board, null, post);	
	}
	
	/*
	 * @see com.nulchan.IPostSender#getCaptcha()
	 */
	@Override
	public byte[] getCaptcha() throws BoardException {
		try {
			Connection.Response response = cookies == null ? Jsoup
					.connect(Settings.baseUrl + "captcha.php?" + Math.random())
					.userAgent(Settings.userAgent)
					.referrer(Settings.baseUrl)
					.ignoreContentType(true)
					.execute() 
					: Jsoup
					.connect(Settings.baseUrl + "captcha.php?" + Math.random())
					.userAgent(Settings.userAgent)
					.cookies(cookies)
					.referrer(Settings.baseUrl)
					.ignoreContentType(true)
					.execute();
			cookies = response.cookies();
			return response.bodyAsBytes();		
		} catch (Exception e) {
            throw new BoardException("Не могу загрузить капчу.");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nulchan.IPostSender#send(java.lang.String)
	 */
	@Override
	public void send(String captcha) throws BoardException {
		
        MultipartEntity entity = prepareEntity(captcha);
        HttpPost request = preparePostRequest(entity);
        //TODO Установить куки используя HttpContext?
        String resultText = "";
        try {
	        HttpResponse response = client.execute(request);
	        String html = EntityUtils.toString(response.getEntity());
	        Document doc = Jsoup.parse(html, request.getURI().toString());
	        resultText = doc.body().text();
        } catch (Exception e) {
        	throw new BoardException("Сбой при отправке.");
        }
        if(resultText.contains("﻿Ошибка"));
        	throw new BoardException(resultText.substring(resultText.indexOf("Ошибка")+7));
	}

	private HttpPost preparePostRequest(MultipartEntity entity) {
		//Генерируем запрос.
		HttpPost request = new HttpPost(Settings.baseUrl + "board.php?dir=" + board);
		if(cookies!= null && !cookies.isEmpty()) {
			StringBuilder cookieStringBuilder = new StringBuilder();
			for(String key : cookies.keySet())
				cookieStringBuilder.append(key + "=" + cookies.get(key) + ";");
	        request.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
			request.setHeader("Cookie", cookieStringBuilder.toString());
		}
		request.setHeader("Referer", Settings.baseUrl + board);
        request.setHeader("User-Agent", Settings.userAgent);
        request.setEntity(entity);
		return request;
	}

	private MultipartEntity prepareEntity(String captcha) throws BoardException {
		try {
			//Заполнение тела сообщения
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, charset);
	        String tmp;
	        entity.addPart("board", new StringBody(board, charset));
	        entity.addPart("replythread", new StringBody(threadId, charset));
	        entity.addPart("MAX_FILE_SIZE", new StringBody("2048000", charset));
	        tmp = post.getPoster();
	        if(!"".equals(tmp))
	            entity.addPart("name", new StringBody(tmp, charset));
	        if(post.isSaged())
	            entity.addPart("em", new StringBody("sage", charset));
	        entity.addPart("captcha", new StringBody(captcha, charset));
	        entity.addPart("subject", new StringBody(post.getSubject(), charset));
	        entity.addPart("message", new StringBody(post.getText(), charset));
	        tmp = post.getAttachment();
	        File f;
	        if(tmp != null && (f = new File(tmp)).isFile()) {
	        	entity.addPart("imagefile", new FileBody(f));
	        } else entity.addPart("nofile", new StringBody("on", charset));
	        entity.addPart("postpassword", new StringBody(Settings.password, charset));
	        //TODO добавить возможность вставки видео?
			return entity;
		} catch(Exception e) {
			throw new BoardException("Сбой при отправке.");
		}
	}	

}

	