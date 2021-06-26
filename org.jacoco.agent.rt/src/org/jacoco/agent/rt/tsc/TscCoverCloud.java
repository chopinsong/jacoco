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

import org.jacoco.agent.rt.internal.IExceptionLogger;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * @author songyanc
 */
public class TscCoverCloud {

	public static final String FILE_UPLOAD_SERVICE_HOST = "http://upload.uat.i4px.com/";
	public static final String KUBERNETES_POD_IP = "KUBERNETES_POD_IP";
	public static final String PRE_SIGNED_URL = "upload/getPresignedUrl";
	public static final int SUCCESS_CODE = 1;
	public static final String UPLOAD_URL = "upload/uploadFileGetUrl";
	public static final String APP_NAME = "APP_NAME";
	private IExceptionLogger logger;
	private static final char[] HOST_CHARS = new char[] { 't', 's', 'c', '.',
			'i', '4', 'p', 'x', '.', 'c', 'o', 'm' };

	public TscCoverCloud(IExceptionLogger logger) {
		this.logger = logger;
	}

	public void push() {
		try {
			String testHost = new String(HOST_CHARS);
			String kubernetesPodIp = System.getenv(KUBERNETES_POD_IP);
			String appName = System.getenv(APP_NAME);
			String post = new Request().post(
					"http://" + testHost + "/coverCloud/put",
					"{\"appName\":\"" + appName + "\",\"host\":\""
							+ kubernetesPodIp + "\"}");
			Res res = JsonUtil.parse(post, Res.class);
			if (res.getResult() == SUCCESS_CODE) {
				System.out.println("推送成功");
			}
		} catch (Exception e) {
			logger.logExeption(e);
		}

	}

	public void pushJar() {
		String s = uploadJar();
		String testHost = new String(HOST_CHARS);
		String post = new Request().post(
				"http://" + testHost + "/coverCloud/putJar",
				"{\"url\":\"" + s + "\"}");
		Res res = JsonUtil.parse(post, Res.class);
		if (res.getResult() == SUCCESS_CODE) {
			System.out.println("推送成功");
		}
	}

	public String uploadJar() {
		String appName = System.getenv(APP_NAME);
		String jarPath = "/root/" + appName + "/target/" + appName + ".jar";
		File file = new File(jarPath);
		if (!file.exists()) {
			System.out.println("jar文件不存在：" + jarPath);
		}
		FileUploadDto fileUploadDto = new FileUploadDto();
		fileUploadDto.setProjectName(appName);
		fileUploadDto.setAccessControl("true");
		fileUploadDto.setFileUrl(file.getAbsolutePath());
		fileUploadDto.setBucketName("fpx-bss-oss");
		FileUploadResDto res = upload(file, fileUploadDto);
		if (res.getErrCode() != 0) {
			throw new RuntimeException("上传文件失败");
		}
		FileUploadDto preSignDto = new FileUploadDto();
		preSignDto.setBucketName(fileUploadDto.getBucketName());
		preSignDto.setRemoteFileName(getRemoteFileName(res));
		preSignDto.setExpirationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(System.currentTimeMillis() + 5 * 60 * 60 * 1000));
		FileUploadResDto preSignedUrl = getPreSignedUrl(fileUploadDto);
		return preSignedUrl.getUrl();
	}

	public FileUploadResDto getPreSignedUrl(FileUploadDto dto) {
		String url = FILE_UPLOAD_SERVICE_HOST + PRE_SIGNED_URL;
		try {
			String res = new Request().post(url, dto.toString());
			return JsonUtil.parse(res, FileUploadResDto.class);
		} catch (Exception e) {
			throw new RuntimeException("获取外链请求失败");
		}
	}

	public String getRemoteFileName(FileUploadResDto dto) {
		String url = dto.getUrl();
		if (url.contains("/")) {
			String[] split = url.split("/");
			return split[split.length - 1];
		}
		throw new RuntimeException("返回的路径中没有远程名称");
	}

	public FileUploadResDto upload(File file, FileUploadDto fileUploadDto) {
		String url = FILE_UPLOAD_SERVICE_HOST + UPLOAD_URL;

		fileUploadDto.setFileUrl(null);
		String res;
		try {
			res = DoRequest.upload(url, "file", file, fileUploadDto);
			FileUploadResDto dto = JsonUtil.parse(res, FileUploadResDto.class);
			return dto;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
