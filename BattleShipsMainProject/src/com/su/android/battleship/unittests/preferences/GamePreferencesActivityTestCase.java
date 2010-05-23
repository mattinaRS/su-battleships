package com.su.android.battleship.unittests.preferences;

import junit.framework.Assert;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.su.android.battleship.cfg.GameDifficulty;
import com.su.android.battleship.cfg.GamePreferences;
import com.su.android.battleship.ui.PreferencesMenuScreen;

/**
 * This class tests the game preferences functionality.
 * Extends [ActivityInstrumentationTestCase2] not [ActivityInstrumentationTestCase],
 * because with ...2 we can:
 * - run any test method on the UI thread.
 * - inject custom Intents into your Activity.
 * And also [ActivityInstrumentationTestCase] is deprecated.
 * @author Ivaylo Stoykov
 *
 */
public class GamePreferencesActivityTestCase extends
		ActivityInstrumentationTestCase2<PreferencesMenuScreen> {
	
	private Context context;
	
	/**
	 * Default constructor. Must exists.
	 * Calls super with the tested Activity.
	 */
	public GamePreferencesActivityTestCase() {
		super("com.su.android.battleship.ui", PreferencesMenuScreen.class);
	}
	
	/**
	 * Initializes some common all tests stuff 
	 */
	@Override
	public void setUp() {
		try {
			super.setUp();
		} catch (Exception e) {
			// iibaasi zmiqta
			e.printStackTrace();
		}
		
		context = this.getInstrumentation().getContext();
	}
	
	/**
	 * Tests for null values
	 * @throws Exception
	 */
	public void testPreferencesForNullValues() throws Exception {
		Assert.assertNotNull(GamePreferences.getPreference(context, GamePreferences.PREFERENCE_SOUND));
		Assert.assertNotNull(GamePreferences.getPreference(context, GamePreferences.PREFERENCE_VIBRATION));
		Assert.assertNotNull(GamePreferences.getPreference(context, GamePreferences.PREFERENCE_DIFFICULTY));
	}
	
	/**
	 * Tests for correct preference save.
	 * @throws Exception
	 */
	public void testPreferencesSaveBehavior() throws Exception {
		boolean hasSound = false;
		GamePreferences.setPreference(context, GamePreferences.PREFERENCE_SOUND, hasSound);
		Assert.assertEquals(hasSound, ((Boolean) GamePreferences.getPreference(context, GamePreferences.PREFERENCE_SOUND)).booleanValue());
		
		boolean hasVibration = true;
		GamePreferences.setPreference(context, GamePreferences.PREFERENCE_VIBRATION, hasVibration);
		Assert.assertEquals(hasVibration, ((Boolean) GamePreferences.getPreference(context, GamePreferences.PREFERENCE_VIBRATION)).booleanValue());

		GameDifficulty gameDifficulty = GameDifficulty.HARD;
		GamePreferences.setPreference(context, GamePreferences.PREFERENCE_DIFFICULTY, gameDifficulty);
		Assert.assertEquals(gameDifficulty, (GameDifficulty) GamePreferences.getPreference(context, GamePreferences.PREFERENCE_DIFFICULTY));
		
		GamePreferences.savePreferences(context);
		Assert.assertEquals(hasSound, ((Boolean) GamePreferences.getPreference(context, GamePreferences.PREFERENCE_SOUND)).booleanValue());
		Assert.assertEquals(hasVibration, ((Boolean) GamePreferences.getPreference(context, GamePreferences.PREFERENCE_VIBRATION)).booleanValue());
		Assert.assertEquals(gameDifficulty, (GameDifficulty) GamePreferences.getPreference(context, GamePreferences.PREFERENCE_DIFFICULTY));
	}
}
