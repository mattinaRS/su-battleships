package com.su.android.battleship.cfg;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class for working with the game preferences
 * @author Ivaylo Stoykov
 */
public class GamePreferences {
	
	public static final String SOUND_THEME_LIGHTSOUT = "Lightsout";
	public static final String SOUND_THEME_SPOOKEY = "Spookey";
	
	/**
	 * variable ,representing the sound theme used in the game - configurable from SoundOptionsScreen
	 */
	public static final String SOUND_THEME = "BSG_SOUND_THEME";
		
	/**
	 * variable, representing music setting in a bundle 
	 */
	public static final String PREFERENCE_MUSIC = "BSG_PREFERENCE_MUSIC";
	
	/**
	 * variable, representing sound setting in a bundle 
	 */
	public static final String PREFERENCE_SOUND = "BSG_PREFERENCE_SOUND";
	
	/**
	 * variable, representing vibration setting in a bundle
	 */
	public static final String PREFERENCE_VIBRATION = "BSG_PREFERENCE_VIBRATION";
	
	/**
	 * variable, representing difficulty level setting in a bundle
	 */
	public static final String PREFERENCE_DIFFICULTY = "BSG_PREFERENCE_DIFFICULTY";
	
	/**
	 * variable, representing nickname setting in a bundle
	 */
	public static final String PREFERENCE_NICKNAME = "BSG_PREFERENCE_NICKNAME";
	
	/**
	 * variable, representing saved settings in a bundle
	 */
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
		mPreferences.put(PREFERENCE_NICKNAME, settings.getString(PREFERENCE_NICKNAME, "Player"));
		mPreferences.put(SOUND_THEME, SOUND_THEME_SPOOKEY);
		mPreferences.put(PREFERENCE_MUSIC,true);
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
		editor.putString(PREFERENCE_NICKNAME, mPreferences.get(PREFERENCE_NICKNAME).toString());
		editor.putBoolean(PREFERENCE_MUSIC, (Boolean) mPreferences.get(PREFERENCE_MUSIC));
		editor.putString(SOUND_THEME, mPreferences.get(SOUND_THEME).toString());
		editor.commit();
	}
	
}
