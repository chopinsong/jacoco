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

import org.apache.http.Consts;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.Map;

/**
 * @author songyanc
 */
public class DoRequest {
	private DoRequest() {
	}

	/**
	 * 上传附件
	 *
	 * @param fileName
	 *            服务端定义的附件名
	 * @param body
	 *            请求带的参数
	 * @return JSONObject
	 */
	public static String upload(String url, String fileName, File file,
			FileUploadDto body) {
		HttpPost httpPost = new HttpPost(url);
		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addBinaryBody(fileName, file,
					ContentType.create("multipart/form-data", Consts.UTF_8),
					file.getName());
			Map<String, String> params = body.toMap();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				builder.addPart(entry.getKey(), new StringBody(entry.getValue(),
						ContentType.create("text/plain", Consts.UTF_8)));
			}
			httpPost.setEntity(builder.build());
			CloseableHttpResponse response = HttpClientBuilder.create().build()
					.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
