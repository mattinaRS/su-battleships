package com.su.android.battleship.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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
	
	private String mNickname;
	private EditText mTextViewNickname;
	
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
		
		mTextViewNickname = (EditText) findViewById(R.id.Nickname); 
		
		String[] oldState = null;
		if (savedInstanceState != null) {
			oldState = savedInstanceState.getStringArray(GamePreferences.BUNDLE_STATE);
		}
		
		if (oldState == null) {
			mHasSound = (Boolean) GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND);
			mHasVibration = (Boolean) GamePreferences.getPreference(this, GamePreferences.PREFERENCE_VIBRATION);
			mGameDifficulty = (GameDifficulty) GamePreferences.getPreference(this, GamePreferences.PREFERENCE_DIFFICULTY);
			mNickname = (String) GamePreferences.getPreference(this, GamePreferences.PREFERENCE_NICKNAME);
			//mNickname = "yeah";
		}		
		else {
			try {
				mHasSound = Boolean.parseBoolean(oldState[0]);
				mHasVibration = Boolean.parseBoolean(oldState[1]);
				mGameDifficulty = GameDifficulty.valueOf(oldState[2]);
				mNickname = (String)oldState[3];
			} catch (Exception e) {
				e.printStackTrace();
				mHasSound = (Boolean) GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND);
				mHasVibration = (Boolean) GamePreferences.getPreference(this, GamePreferences.PREFERENCE_VIBRATION);
				mGameDifficulty = (GameDifficulty) GamePreferences.getPreference(this, GamePreferences.PREFERENCE_DIFFICULTY);
				mNickname = (String) GamePreferences.getPreference(this, GamePreferences.PREFERENCE_NICKNAME);
			}
		}
				
		setSoundButtonText();
		setVibrationButtonText();
		setDifficultyButtonText();
		setNicknameText();
		
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
			mNickname = mTextViewNickname.getText().toString();
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
		GamePreferences.setPreference(this, GamePreferences.PREFERENCE_NICKNAME, mNickname);
		
		GamePreferences.savePreferences(this);
	}
	
	/**
	 * The settings are saved in a Bundle.
	 * When the application become active again the settings will be
	 * loaded from the bundle
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putStringArray(GamePreferences.BUNDLE_STATE, new String[] {mHasSound + "", mHasVibration + "", mGameDifficulty.toString()});
	}

	/**
	 * Not final. Experimental UI
	 */
	private void setSoundButtonText() {
		mButtonSound.setText("Sound " + (mHasSound?"[on]":"[off]"));
	}
	
	/**
	 * Not final. Experimental UI
	 */
	private void setVibrationButtonText() {
		mButtonVibration.setText("Vibration " + (mHasVibration?"[on]":"[off]"));
	}
	
	/**
	 * Not final. Experimental UI
	 */
	private void setDifficultyButtonText() {
		mButtonDifficulty.setText("Difficulty [" + mGameDifficulty.toString().toLowerCase() + "]");		
	}
	
	private void setNicknameText() {
		mTextViewNickname.setText(mNickname);

	}
}
