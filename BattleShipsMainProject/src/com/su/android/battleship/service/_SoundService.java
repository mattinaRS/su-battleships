package com.su.android.battleship.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;

import com.su.android.battleship.R;

/**
 * 
 * @author Rosen
 *
 */
public class _SoundService extends Service {
	private MediaPlayer player;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		int resId = getSpooky();
		
		player = MediaPlayer.create(this,resId);
		player.setLooping(true); // Set looping
		player.start();
	}
	
	private int getBackgroundSound(){
		//TODO : select background motive from the saved background preferences
		int resId = sounds[0];
		return resId;
	}
	
	/**
	 * Start the service
	 */
	public void onStart() {
		player.start();
	}
	
	/**
	 * This method should pause the 
	 */
	public void pause(){
		SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		
		//loads sound resource
//		int soundID = soundPool.load(context, resId, priority);
		
		//plays a sound - the soundId is returned from load
//		soundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
	}
	
	public void onDestroy() {
		player.stop();
	}
	
	
	private int[] sounds = {R.raw.spooky,R.raw.shipsink};
	
	public int getSpooky(){
		return sounds[0];
	}
	
	public int getShipSink(){
		return sounds[1];
	}
}
