/**
 *  文件名:AsyncDownloadTask.java
 *  修改人:lihangjie
 *  创建时间:2015-11-3 下午5:59:15
 */
package com.sharemob.tinchat.lib.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.os.Handler;

/**
 * 
 * <一句话功能简述>
 * 
 * @author lihangjie version [版本号,2015-11-3 下午5:59:15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 * 
 */
public class UpdateDBDownload {
	private String urlAddr = "";
	private String cacheFilePath=null;
	private int BUFFER_SIZE = 1024 * 1024;

	public UpdateDBDownload(Context context) {
		this.urlAddr="http://119.29.99.152/Cache/DazzleParkour.sqlite";
		this.cacheFilePath = String.format("%s%sres%sDazzleParkour.sqlite", context.getFilesDir(),File.separator,File.separator);
	}


	public void loading() {
		
		File zipFile = new File(cacheFilePath);
		if (zipFile.exists()) {
			try {
				zipFile.delete();
				zipFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 开始下载
		FileOutputStream fos = null;
		InputStream inputStream = null;
		BufferedInputStream bis = null;
		HttpURLConnection conn = null;
		int size = 0;
		long startPosition = 0;
		long needDownloadFilelength = 0;
		long completeLength = 0;
		URL url = null;
		try {
			url = new URL(urlAddr);
			startPosition = zipFile.length();
			System.out.println("File startPosition:" + startPosition);

			conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			inputStream = conn.getInputStream();
			bis = new BufferedInputStream(inputStream);
			fos = new FileOutputStream(zipFile);
			byte[] buf = new byte[BUFFER_SIZE];
			while ((size = bis.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}
			fos.flush();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
				if (zipFile.length() == completeLength) {
					System.out.println("File 成功下载到本地SD卡中");
				} else {
					System.out.println("File 下载失败");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}

