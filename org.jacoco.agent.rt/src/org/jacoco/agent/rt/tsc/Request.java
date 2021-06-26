/*******************************************************************************
 * Copyright (c) 2009, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.agent.rt.tsc;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author songyanc
 */
public class Request {

	public static final String APPLICATION_JSON = "application/json";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String ACCEPT = "Accept";
	public static final int SUCCESS_CODE = 200;

	public String post(String u, String jsonBody) {
		HttpURLConnection conn;
		try {
			URL url = new URL(u);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置为true
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 设置连接超时时间和读取超时时间
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(10000);
			conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
			conn.setRequestProperty(ACCEPT, APPLICATION_JSON);
			conn.connect();
		} catch (Exception e) {
			throw new RuntimeException("连接失败");
		}
		writeJsonBody(jsonBody, conn);
		return readRes(conn);
	}

	public String get(String u) {
		HttpURLConnection conn;
		try {
			// 创建远程url连接对象
			URL url = new URL(u);
			// 通过远程url连接对象打开一个连接，强转成HTTPURLConnection类
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			// 设置连接超时时间和读取超时时间
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(60000);
			conn.setRequestProperty(ACCEPT, APPLICATION_JSON);
			// 发送请求
			conn.connect();
		} catch (Exception e) {
			throw new RuntimeException("连接失败");
		}
		return readRes(conn);
	}

	private String readInputStream(InputStream inputStream) throws IOException {
		InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuilder resultBuffer = new StringBuilder();
		String tempLine;
		while ((tempLine = reader.readLine()) != null) {
			resultBuffer.append(tempLine);
			resultBuffer.append("\n");
		}
		return resultBuffer.toString();
	}

	private void writeJsonBody(String jsonBody, HttpURLConnection conn) {
		// 获取输出流
		OutputStream os = null;
		OutputStreamWriter out = null;
		try {
			os = conn.getOutputStream();
			out = new OutputStreamWriter(os);
			out.write(jsonBody);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeAll(os, out);
		}
	}

	private String readRes(HttpURLConnection conn) {
		// 取得输入流，并使用Reader读取
		int responseCode;
		try {
			responseCode = conn.getResponseCode();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		if (responseCode != SUCCESS_CODE) {
			throw new RuntimeException("请求失败");
		}
		return readSuccessRes(conn);
	}

	private String readSuccessRes(HttpURLConnection conn) {
		InputStreamReader is = null;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		try {
			is = new InputStreamReader(conn.getInputStream(), "utf-8");
			in = new BufferedReader(is);
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(is, in);
		}
		return result.toString();
	}

	private void closeAll(Closeable... c) {
		for (Closeable closeable : c) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
