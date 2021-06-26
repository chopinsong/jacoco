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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songyanc
 */
public class FileUploadDto implements Serializable {
	private static final long serialVersionUID = 1L;
	String bucketName;

	String fileUrl;
	String accessControl;
	String projectName;

	String remoteFileName;
	String expirationDate;

	@Override
	public String toString() {
		return "{\"bucketName\":\"" + bucketName + "\"," + "\"fileUrl\":\""
				+ fileUrl + "\"," + "\"accessControl\":\"" + accessControl
				+ "\"," + "\"projectName\":\"" + projectName + "\","
				+ "\"remoteFileName\":\"" + remoteFileName + "\","
				+ "\"expirationDate\":\"" + expirationDate + "\"}";
	}

	public Map<String, String> toMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				Object o = field.get(this);
				if (o != null) {
					map.put(field.getName(), String.valueOf(o));
				}
			} catch (IllegalAccessException e) {
				System.out.println(e);
			}
		}
		return map;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getBucketName() {
		return bucketName;
	}

	public FileUploadDto setBucketName(String bucketName) {
		this.bucketName = bucketName;
		return this;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public FileUploadDto setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
		return this;
	}

	public String getAccessControl() {
		return accessControl;
	}

	public FileUploadDto setAccessControl(String accessControl) {
		this.accessControl = accessControl;
		return this;
	}

	public String getProjectName() {
		return projectName;
	}

	public FileUploadDto setProjectName(String projectName) {
		this.projectName = projectName;
		return this;
	}

	public String getRemoteFileName() {
		return remoteFileName;
	}

	public FileUploadDto setRemoteFileName(String remoteFileName) {
		this.remoteFileName = remoteFileName;
		return this;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public FileUploadDto setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
		return this;
	}
}
