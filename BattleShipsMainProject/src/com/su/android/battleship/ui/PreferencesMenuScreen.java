package com.su.android.battleship.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.su.android.battleship.R;
import com.su.android.battleship.cfg.GameDifficulty;
import com.su.android.battleship.cfg.GamePreferences;

/**
 * Menu screen for changing game preferences
 * @author Ivaylo Stoykov
 *
 */
public class PreferencesMenuScreen extends Activity implements OnClickListener {

	private boolean mHasSound;
	private boolean mHasVibration;
	private GameDifficulty mGameDifficulty;
	
	private Button mButtonSound;
	private Button mButtonVibration;
	private Button mButtonDifficulty;
	private Button mButtonApply;
	private Button mButtonBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);				
		
		mButtonSound = (Button) findViewById(R.id.ButtonSound);
		mButtonSound.setOnClickListener(this);
		
		mButtonVibration = (Button) findViewById(R.id.ButtonVibration);
		mButtonVibration.setOnClickListener(this);
		
		mButtonDifficulty = (Button) findViewById(R.id.ButtonDifficulty);
		mButtonDifficulty.setOnClickListener(this);
		
		mButtonApply = (Button) findViewById(R.id.ButtonApplyPrefs);
		mButtonApply.setOnClickListener(this);
		
		mButtonBack = (Button) findViewById(R.id.ButtonBack);
		mButtonBack.setOnClickListener(this);
		
		mHasSound = (Boolean) GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND);
		mHasVibration = (Boolean) GamePreferences.getPreference(this, GamePreferences.PREFERENCE_VIBRATION);
		mGameDifficulty = (GameDifficulty) GamePreferences.getPreference(this, GamePreferences.PREFERENCE_DIFFICULTY);
				
		setSoundButtonText();
		setVibrationButtonText();
		setDifficultyButtonText();
	}

	/**
	 * Called when a menu button has been clicked
	 * 
	 * @param button	menu button instance
	 */
	public void onClick(View button) {
		switch (button.getId()) {
		case R.id.ButtonSound:
			mHasSound = !mHasSound;
			setSoundButtonText();
			break;
		case R.id.ButtonVibration:
			mHasVibration = !mHasVibration;
			setVibrationButtonText();
			break;
		case R.id.ButtonDifficulty:
			GameDifficulty[] list = GameDifficulty.values();
			for (int i = 0; i < list.length; i++) {
				if (list[i].equals(mGameDifficulty)) {
					if (i == (list.length - 1)) {
						mGameDifficulty = list[0];
					} else {
						mGameDifficulty = list[i + 1];
					}
					break;
				}
			}
			setDifficultyButtonText();
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
		GamePreferences.setPreference(this, GamePreferences.PREFERENCE_SOUND, mHasSound);
		GamePreferences.setPreference(this, GamePreferences.PREFERENCE_VIBRATION, mHasVibration);
		GamePreferences.setPreference(this, GamePreferences.PREFERENCE_DIFFICULTY, mGameDifficulty);
		GamePreferences.savePreferences(this);
	}
	
	/**
	 * The settings are not saved in a Bundle. They are saved in the phone.
	 * When the application become active again the settings will be
	 * loaded from the phone when needed
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
	}

	/**
	 * Not final. Experimental UI
	 */
	private void setSoundButtonText() {
		if (mHasSound) {
			mButtonSound.setText("Sound [on]");
		} else {
			mButtonSound.setText("Sound [off]");
		}		
	}
	
	/**
	 * Not final. Experimental UI
	 */
	private void setVibrationButtonText() {
		if (mHasVibration) {
			mButtonVibration.setText("Vibration [on]");
		} else {
			mButtonVibration.setText("Vibration [off]");
		}		
	}
	
	/**
	 * Not final. Experimental UI
	 */
	private void setDifficultyButtonText() {
		mButtonDifficulty.setText("Difficulty [" + mGameDifficulty.toString() + "]");		
	}
}
