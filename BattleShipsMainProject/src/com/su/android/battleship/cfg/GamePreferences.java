package com.su.android.battleship.cfg;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class for working with the game preferences
 * @author Ivaylo Stoykov
 */
public class GamePreferences {
	
	public static final String PREFERENCE_SOUND = "BSG_PREFERENCE_SOUND";
	public static final String PREFERENCE_VIBRATION = "BSG_PREFERENCE_VIBRATION";
	public static final String PREFERENCE_DIFFICULTY = "BSG_PREFERENCE_DIFFICULTY";
	
	public static final String BUNDLE_STATE = "BSG_PREFERENCES";
	
	private static HashMap<String, Object> mPreferences;
	
	/**
	 * Loads saved settings. If there aren't any - loads default values
	 * @param aCtx context (activity)
	 */
	private static void setPreferenceValues(Context aCtx) {
		mPreferences = new HashMap<String, Object>();
		
		SharedPreferences settings = aCtx.getSharedPreferences(BUNDLE_STATE, 0);
		
		String dif = settings.getString(PREFERENCE_DIFFICULTY, GameDifficulty.NORMAL.toString());
		mPreferences.put(PREFERENCE_DIFFICULTY, GameDifficulty.valueOf(dif));
		mPreferences.put(PREFERENCE_SOUND, settings.getBoolean(PREFERENCE_SOUND, true));
		mPreferences.put(PREFERENCE_VIBRATION, settings.getBoolean(PREFERENCE_VIBRATION, true));
	}
	
	/**
	 * @param aCtx context (activity)
	 * @param aPreferenceId id of the preference
	 * @return preference value
	 */
	public static Object getPreference(Context aCtx, String aPreferenceId) {
		if (mPreferences == null) {
			setPreferenceValues(aCtx);
		}
		
		return mPreferences.get(aPreferenceId);
	}
	
	/**
	 * Set preference
	 * @param aCtx context (activity)
	 * @param aPreferenceId id of the preference
	 * @param aValue preference value
	 */
	public static void setPreference(Context aCtx, String aPreferenceId, Object aValue) {
		if (mPreferences == null) {
			setPreferenceValues(aCtx);
		}
		
		mPreferences.put(aPreferenceId, aValue);
	}
	
	/**
	 * Saves the preferences in the phone
	 * @param aCtx context (activity)
	 */
	public static void savePreferences(Context aCtx) {
		if (mPreferences == null) {
			setPreferenceValues(aCtx);
		}
		
		SharedPreferences settings = aCtx.getSharedPreferences(BUNDLE_STATE, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(PREFERENCE_SOUND, (Boolean) mPreferences.get(PREFERENCE_SOUND));
		editor.putBoolean(PREFERENCE_VIBRATION, (Boolean) mPreferences.get(PREFERENCE_VIBRATION));
		editor.putString(PREFERENCE_DIFFICULTY, mPreferences.get(PREFERENCE_DIFFICULTY).toString());
		
		editor.commit();
	}
	
}
