package com.su.android.battleship.ui;

import com.su.android.battleship.R;
import com.su.android.battleship.cfg.GamePreferences;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Handles sounds in the game
 * @author Vasko
 *
 */
public class SoundOptions extends Activity implements OnClickListener {

	private String mSoundTheme;
	private boolean mHasSound;
	private boolean mHasMusic;
	
	private Button buttonGameSound;
	private Button buttonGameMusic;
	private Button buttonSoundTheme;

	private Button buttonApply;
	private Button buttonBack;

	/**
	 * Called when activity is starting
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sound_settings);

		buttonSoundTheme = (Button) findViewById(R.id.ButtonSoundTheme);
		buttonSoundTheme.setOnClickListener(this);

		buttonGameSound = (Button) findViewById(R.id.ButtonGameSound);
		buttonGameSound.setOnClickListener(this);

		buttonGameMusic = (Button) findViewById(R.id.ButtonGameMusic);
		buttonGameMusic.setOnClickListener(this);

		buttonApply = (Button) findViewById(R.id.ButtonApplyPrefs);
		buttonApply.setOnClickListener(this);

		buttonBack = (Button) findViewById(R.id.ButtonBack);
		buttonBack.setOnClickListener(this);

		String[] oldState = null;
		if (savedInstanceState != null) {
			oldState = savedInstanceState
					.getStringArray(GamePreferences.BUNDLE_STATE);
		}

		if (oldState == null) {
			mHasSound = (Boolean) GamePreferences.getPreference(this,
					GamePreferences.PREFERENCE_SOUND);
			mHasMusic = (Boolean) GamePreferences.getPreference(this,
					GamePreferences.PREFERENCE_MUSIC);
			mSoundTheme = (String) GamePreferences.getPreference(this,
					GamePreferences.SOUND_THEME);
		} else {
			try {
				mHasSound = Boolean.parseBoolean(oldState[0]);
				mHasMusic = Boolean.parseBoolean(oldState[4]);
				mSoundTheme = (String) oldState[5];
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		setSoundThemeButtonText();
		setGameSoundButtonText();
		setGameMusicButtonText();
		
	}

	/**
	 * Not final. Experimental UI
	 */
	private void setGameSoundButtonText() {
		buttonGameSound.setText("Game Sound " + (mHasSound ? "[on]" : "[off]"));
	}

	/**
	 * Not final. Experimental UI
	 */
	private void setGameMusicButtonText() {
		buttonGameMusic.setText("Game Music " + (mHasMusic ? "[on]" : "[off]"));
	}

	/**
	 * Not final. Experimental UI
	 */
	private void setSoundThemeButtonText() {
		buttonSoundTheme.setText("Theme " + "[" + mSoundTheme + "]");
	}

	/**
	 * Called when a menu button has been clicked
	 * 
	 * @param button
	 *            menu button instance
	 */
	public void onClick(View button) {
		switch (button.getId()) {
		case R.id.ButtonGameSound:
			mHasSound = !mHasSound;
			setGameSoundButtonText();
			break;
		case R.id.ButtonGameMusic:
			mHasMusic = !mHasMusic;
			setGameMusicButtonText();
			break;
		case R.id.ButtonSoundTheme:
			if (GamePreferences.SOUND_THEME_LIGHTSOUT.equals(mSoundTheme)) {
				mSoundTheme = GamePreferences.SOUND_THEME_SPOOKEY;
			}else if(GamePreferences.SOUND_THEME_SPOOKEY.equals(mSoundTheme)) {
				mSoundTheme = GamePreferences.SOUND_THEME_LIGHTSOUT;
			}
			setSoundThemeButtonText();
			break;
		case R.id.ButtonApplyPrefs:			
			saveState();
			finish();
			break;
		case R.id.ButtonBack:
			finish();
			break;
		}
	}

	private void saveState() {
		GamePreferences.setPreference(this, GamePreferences.PREFERENCE_SOUND,
				mHasSound);
		GamePreferences.setPreference(this, GamePreferences.PREFERENCE_MUSIC,
				mHasMusic);
		GamePreferences.setPreference(this, GamePreferences.SOUND_THEME,
				mSoundTheme);

		GamePreferences.savePreferences(this);
	}
}
