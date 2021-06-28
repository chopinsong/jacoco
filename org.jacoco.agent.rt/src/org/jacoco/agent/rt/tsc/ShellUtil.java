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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;

/**
 * @author songyanc
 */
public class ShellUtil {

	private static final String RES_ENCODING = "UTF-8";

	public static String exec(String shell) {
		return exec(shell, null);
	}

	/**
	 * 执行系统命令, 返回执行结果
	 *
	 * @param shell
	 *            需要执行的命令
	 * @param dir
	 *            执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
	 */
	public static String exec(String shell, File dir) {
		StringBuilder result = new StringBuilder();
		Process process = null;
		BufferedReader bufrIn = null;
		BufferedReader bufrError = null;
		try {
			String[] commond = { "/bin/bash", "-c", shell };
			// 执行命令, 返回一个子进程对象（命令在子进程中执行）
			process = Runtime.getRuntime().exec(commond, null, dir);
			// 等待命令执行完成（成功会返回0）
			int processResult = process.waitFor();
			System.out.println("shell执行结果:" + processResult);
			// 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（子进程的输出就是主进程的输入）
			bufrIn = new BufferedReader(new InputStreamReader(
					process.getInputStream(), RES_ENCODING));
			String line;
			while ((line = bufrIn.readLine()) != null) {
				result.append(line).append('\n');
			}
			while ((line = bufrError.readLine()) != null) {
				result.append(line).append('\n');
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			closeStream(bufrIn, bufrError);
			if (process != null) {
				process.destroy();
			}
		}
		return result.toString();
	}

	private static void closeStream(Closeable... streams) {
		for (Closeable stream : streams) {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

}
