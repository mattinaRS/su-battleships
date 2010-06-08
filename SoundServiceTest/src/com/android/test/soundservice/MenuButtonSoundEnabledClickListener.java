package com.android.test.soundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class MenuButtonSoundEnabledClickListener implements OnClickListener {
	
	private static boolean isSoundEnabled = loadFromSoundPreferences();
	
	
	
	@Override
	public void onClick(View v) {	
		if(isSoundEnabled){
			playMenuButtonSound();
		}
		handleClick(v);
	}
	
	/**
	 * This method substitutes the standard onClick method for each button that will
	 * be soundEnabled - the business logic behind the button must go into this method 
	 * @param v - the View that was clicked - given from the onClick method
	 */
	public abstract void handleClick(View v);
	
	
	public abstract void playMenuButtonSound();
	
	
	/**
	 * A check is made int the sound preferences to see whether sound is on. 
	 * @return
	 */
	private static boolean loadFromSoundPreferences(){
		boolean isSoundLoaded = true;
		//TODO : make a real preferences load here
		return isSoundLoaded;
	}
	
	/**
	 * This method will be used from sound enable/disable Activity to
	 * refresh the current isSoundEnabledState
	 * @param enabled - the new value of isSoundEnabled
	 */
	public static void updateSoundEnabled(boolean enabled){
		isSoundEnabled = enabled;
	}

}
