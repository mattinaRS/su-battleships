package com.su.android.battleship.util;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.su.android.battleship.R;

/**
 * Class for sound playback during the game
 * @author Rosen
 *
 */
public class GameSounds {
	
	/**
	 * constructor
	 * @param context Activity context
	 */
	public GameSounds(Context context) {
		super();
		myContext = context;
		this.initSounds();
	}

	/**
	 * sound on game start
	 */
	public static final int SOUND_GAME_START = 1;
	
	/**
	 * sound on hitting an opponent's ship
	 */
	public static final int SOUND_HIT = 2;
	
	/**
	 * sound on missing a shot
	 */
	public static final int SOUND_MISS = 3;
	
	/**
	 * sound on winning the game
	 */
	public static final int SOUND_YOU_WIN = 4;
	
	/**
	 * sound on losing the game
	 */
	public static final int SOUND_YOU_LOSE = 5;

	
	private SoundPool soundPool;
	
	private HashMap<Integer, Integer> soundPoolMap;
	
	private Context myContext;
	
	/**
	 * Method for sounds init in memory
	 */
	private void initSounds() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		
		soundPoolMap.put(SOUND_GAME_START, soundPool.load(myContext, R.raw.start, 1));
		soundPoolMap.put(SOUND_HIT, soundPool.load(myContext, R.raw.hit, 2));
		soundPoolMap.put(SOUND_MISS, soundPool.load(myContext, R.raw.miss, 3));
		soundPoolMap.put(SOUND_YOU_WIN, soundPool.load(myContext, R.raw.win, 4));
		soundPoolMap.put(SOUND_YOU_LOSE, soundPool.load(myContext, R.raw.lose, 5));
	}
	
	/**
	 * Method for playing a sound
	 * @param sound - index of the sound to be played
	 */
	public void playSound(int sound) {
	        AudioManager mgr = (AudioManager)myContext.getSystemService(Context.AUDIO_SERVICE);
	        int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	        soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1, 0, 1f);
	}
}
