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

/**
 * @author songyanc
 */
public class TscCoverCloud {

	public static final String KUBERNETES_POD_IP = "KUBERNETES_POD_IP";
	public static final String APP_NAME = "APP_NAME";
	public static final int SUCCESS_CODE = 1;
	private IExceptionLogger logger;
	public static final char[] REMOTE_HOST = new char[] { 't', 's', 'c', '.',
			'i', '4', 'p', 'x', '.', 'c', 'o', 'm' };

	public TscCoverCloud(IExceptionLogger logger) {
		this.logger = logger;
	}

	public void push() {
		try {
			String testHost = new String(REMOTE_HOST);
			String kubernetesPodIp = System.getenv(KUBERNETES_POD_IP);
			String appName = System.getenv(APP_NAME);
			String post = new Request().post(
					"http://" + testHost + "/coverCloud/put",
					"{\"appName\":\"" + appName + "\",\"host\":\""
							+ kubernetesPodIp + "\"}");
			Res res = JsonUtil.parse(post, Res.class);
			if (SUCCESS_CODE == res.getResult()) {
				System.out.println("推送appName:ip成功");
				pushJar(res.getMsg());
			}
		} catch (Exception e) {
			logger.logExeption(e);
		}

	}

	public void pushJar(String csId) {
		if (null == csId || csId.length() == 0) {
			return;
		}
		String appName = System.getenv(APP_NAME);
		String jarPath = "/root/" + appName + "/target/" + appName + ".jar";
		File file = new File(jarPath);
		if (!file.exists()) {
			System.out.println("jar文件不存在：" + jarPath);
		}
		ShellUtil.exec("yum install -y sshpass");
		ShellUtil.exec(
				"sshpass -p password ssh  -q -o StrictHostKeyChecking=no root@"
						+ new String(REMOTE_HOST));
		String cmd = "JAR_NAME=\"" + appName + ".jar\"&&JAR_PATH=\"/root/"
				+ appName + "/target/" + appName + ".jar\"&&SERVICE_ID=\""
				+ csId + "\" && JAR_SH=` curl http://" + new String(REMOTE_HOST)
				+ "/coverService/sh -s --connect-time 5` && [[ -n \"$JAR_SH\" ]] && eval \"$JAR_SH\"";
		System.out.println("推送命令:" + cmd);
		String exec = ShellUtil.exec(cmd);
		System.out.println("推送结果:" + exec);
	}
}
