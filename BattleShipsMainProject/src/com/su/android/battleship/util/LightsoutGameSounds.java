package com.su.android.battleship.util;

import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import com.su.android.battleship.R;

public class LightsoutGameSounds {

	// TODO : obtain sound volume from Preferences
	private final static float LEFT_VOLUME = 1f;
	private final static float RIGHT_VOLUME = 1f;
	
	private final static int DEFAULT_SOUND_PRIORITY = 1;

	private Map<Integer, Integer> soundIdMap = new HashMap<Integer, Integer>();

	
	private SoundPool soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC,100);


	private int[] sounds = { R.raw.rocksplash, R.raw.click, R.raw.lightsout,
			R.raw.missileshootsmall, R.raw.explodelarge , R.raw.shipsink };

	private int getRockSplashResId() {
		return sounds[0];
	}

	private int getClickResId() {
		return sounds[1];
	}

	private int getLigtsoutResId() {
		return sounds[2];
	}

	private int getMissleShot() {
		return sounds[3];
	}

	private int getExplosion() {
		return sounds[4];
	}
	
	private int getShipSink() {
		return sounds[5];
	}

	/**
	 * constructor
	 * @param context Activity context
	 */
	public LightsoutGameSounds(Context context) {
		super();		
		this.initSoundPool(context);
	}	
	

	private void initSoundPool(Context context) {

		// loads sound resource
		int rockSplashSoundID = soundPool.load(context, getRockSplashResId(), 100);
		soundIdMap.put(getRockSplashResId(), rockSplashSoundID);

		int clickSoundID = soundPool.load(context, getClickResId(), 100);
		soundIdMap.put(getClickResId(), clickSoundID);

		int missleshotSoundID = soundPool.load(context, getMissleShot(), 100);
		soundIdMap.put(getMissleShot(), missleshotSoundID);

		int explosionSoundID = soundPool.load(context, getExplosion(), 100);
		soundIdMap.put(getExplosion(), explosionSoundID);	
		
		int shipSinkSoundID = soundPool.load(context, getShipSink(), 100);
		soundIdMap.put(getShipSink(), shipSinkSoundID);
	}

	
	public void playRockSplash(){
		soundPool.play(soundIdMap.get(getRockSplashResId()), LEFT_VOLUME,
				RIGHT_VOLUME, DEFAULT_SOUND_PRIORITY, 0, 1);
	}
	
	public void playExplosion(){
		soundPool.play(soundIdMap.get(getExplosion()), LEFT_VOLUME,
				RIGHT_VOLUME, DEFAULT_SOUND_PRIORITY, 0, 1);
	}

	public void playClick(){
		soundPool.play(soundIdMap.get(getClickResId()), LEFT_VOLUME,
				RIGHT_VOLUME, DEFAULT_SOUND_PRIORITY, 0, 1);
	}
	
	public void playMissleShot(){
		soundPool.play(soundIdMap.get(getMissleShot()), LEFT_VOLUME,
				RIGHT_VOLUME, DEFAULT_SOUND_PRIORITY, 0, 1);
	}
	
	public void playShipSink(){
		soundPool.play(soundIdMap.get(getShipSink()), LEFT_VOLUME,
				RIGHT_VOLUME, DEFAULT_SOUND_PRIORITY, 0, 1);
	}
}
