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

/**
 * @author songyanc
 */
public class FileUploadResDto implements Serializable {
	private static final long serialVersionUID = 1L;
	int errCode;
	String url;
	String msg;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getErrCode() {
		return errCode;
	}

	public FileUploadResDto setErrCode(int errCode) {
		this.errCode = errCode;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public FileUploadResDto setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public FileUploadResDto setMsg(String msg) {
		this.msg = msg;
		return this;
	}
}
