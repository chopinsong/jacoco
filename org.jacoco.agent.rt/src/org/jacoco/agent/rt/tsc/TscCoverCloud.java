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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author songyanc
 */
public class TscCoverCloud {

    public static final String           KUBERNETES_POD_IP = "KUBERNETES_POD_IP";
    public static final String           APP_NAME          = "APP_NAME";
    public static final String           SUCCESS_CODE      = "1";
    private             IExceptionLogger logger;

    public TscCoverCloud(IExceptionLogger logger) {
        this.logger = logger;
    }

    public void push() {
        try {
            char[] chars = {'t', 's', 'c', '.', 'i', '4', 'p', 'x', '.', 'c',
                    'o', 'm'};
            String testHost = new String(chars);
            String kubernetesPodIp = System.getenv(KUBERNETES_POD_IP);
            String appName = System.getenv(APP_NAME);
            String post = new Request().post(
                    "http://" + testHost + "/coverCloud/put",
                    "{\"appName\":\"" + appName + "\",\"host\":\""
                            + kubernetesPodIp + "\"}");
            Pattern compile = Pattern.compile("result\":(\\d)");
            Matcher m = compile.matcher(post);
            if (m.find()) {
                String group = m.group(1);
                if (SUCCESS_CODE.equals(group)) {
                    System.out.println("推送成功");
                }
            }
        } catch (Exception e) {
            logger.logExeption(e);
        }

    }

    public void pushJar() {
        String appName = System.getenv(APP_NAME);
        String jarPath = "/root/" + appName + "/target/" + appName + ".jar";
        File file = new File(jarPath);
        if (!file.exists()) {
            System.out.println("jar文件不存在：" + jarPath);
        }
    }
}
