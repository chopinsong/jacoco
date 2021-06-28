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

public class Res {
	private int result;
	private String msg;

	public int getResult() {
		return result;
	}

	public Res setResult(int result) {
		this.result = result;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public Res setMsg(String msg) {
		this.msg = msg;
		return this;
	}
}
