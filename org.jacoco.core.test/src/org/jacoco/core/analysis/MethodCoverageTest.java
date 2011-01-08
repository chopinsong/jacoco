/*******************************************************************************
 * Copyright (c) 2009, 2010 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *    
 *******************************************************************************/
package org.jacoco.core.analysis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test for {@link MethodCoverage}.
 * 
 * @author Marc R. Hoffmann
 * @version $qualified.bundle.version$
 */
public class MethodCoverageTest {

	@Test
	public void testProperties() {
		// Example: java.util.Collections.emptySet()
		MethodCoverage node = new MethodCoverage("emptySet",
				"()Ljava/util/Set;",
				"<T:Ljava/lang/Object;>()Ljava/util/Set<TT;>;");
		assertEquals(ICoverageNode.ElementType.METHOD, node.getElementType());
		assertEquals("emptySet", node.getName());
		assertEquals("()Ljava/util/Set;", node.getDesc());
		assertEquals("<T:Ljava/lang/Object;>()Ljava/util/Set<TT;>;",
				node.getSignature());
	}

	@Test
	public void testEmptyMethod() {
		ICoverageNode node = new MethodCoverage("sample", "()V", null);

		assertEquals(CounterImpl.getInstance(0, 0), node.getLineCounter());
		assertEquals(CounterImpl.getInstance(0, 0),
				node.getInstructionCounter());
		assertEquals(CounterImpl.getInstance(0, 0), node.getBranchCounter());
		assertEquals(CounterImpl.getInstance(1, 0), node.getMethodCounter());
		assertEquals(CounterImpl.getInstance(0, 0), node.getClassCounter());
	}

	@Test
	public void testIncrementMissed() {
		MethodCoverage node = new MethodCoverage("sample", "()V", null);
		node.increment(CounterImpl.getInstance(25, 0), CounterImpl.COUNTER_0_0,
				3);

		assertEquals(CounterImpl.getInstance(1, 0), node.getMethodCounter());
	}

	@Test
	public void testIncrementCovered() {
		MethodCoverage node = new MethodCoverage("sample", "()V", null);
		node.increment(CounterImpl.getInstance(12, 13),
				CounterImpl.COUNTER_0_0, 3);

		assertEquals(CounterImpl.getInstance(0, 1), node.getMethodCounter());
	}

}
