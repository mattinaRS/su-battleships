package com.su.android.battleship.unittests.preferences;


import junit.framework.TestSuite;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

/**
 * The InstrumentationTestRunner must hold all of the tests. 
 * The tests will run at once. Of course they can be run separately.
 * This runner will run only those test which are added to the TestSuite.
 * This class serves the unit tests related to game preferences. 
 * Must be added to the android manifest.
 * @author Ivaylo Stoykov
 *
 */
public class PreferencesInstrumentationTestRunner extends
		InstrumentationTestRunner {

	@Override
	public TestSuite getAllTests() {
		InstrumentationTestSuite suite = new InstrumentationTestSuite(this);
		
		// add all the test this runner shout run
		suite.addTestSuite(GamePreferencesActivityTestCase.class);
		suite.addTestSuite(ExerciseTestCase.class);
		
		
		return suite;
	}

	@Override
	public ClassLoader getLoader() {
		return PreferencesInstrumentationTestRunner.class.getClassLoader();
	}

	
}
