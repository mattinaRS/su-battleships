package com.su.android.battleship.unittests.preferences;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * This class is for exercise purpose.
 * It runs normal junit test.
 * No Android activities or contexts involved.
 * @author Ivaylo Stoykov
 *
 */
public class ExerciseTestCase extends TestCase {
	@Override
	protected void setUp() throws Exception {
		// some initialization to all unit tests
	}

	/**
	 * test something
	 * @throws Exception
	 */
	public void testSmth() throws Exception {
		// should succeed :)
		Assert.assertEquals(true, true);
	}
}
