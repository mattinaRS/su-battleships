package com.android.test.soundservice;

import java.io.FileDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Binder;
import android.os.IBinder;

public class SoundService extends Service {

	private int spookeyStreamId;
	private int sinkShipStreamId;

	// TODO : obtain sound volume from Preferences
	private final static float LEFT_VOLUME = 1f;
	private final static float RIGHT_VOLUME = 1f;

	private final static int DEFAULT_SOUND_PRIORITY = 1;

	private Map<Integer, Integer> soundIdMap = new HashMap<Integer, Integer>();

	private MediaPlayer player;
	private SoundPool soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC,
			100);

	// This is the object that receives interactions from clients.See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new SoundServiceBinder();

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
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class SoundServiceBinder extends Binder {
		SoundService getService() {
			return SoundService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		initSoundPool(getBaseContext());
		player = MediaPlayer.create(this, getLigtsoutResId());
		player.start();
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

	public void playSound(int soundID, int loop) {
		// plays a sound - the soundId is returned from load
		soundPool.play(soundID, LEFT_VOLUME, RIGHT_VOLUME,
				DEFAULT_SOUND_PRIORITY, loop, 1);
	}

	public void testLoopTwoSounds() {
		System.out.println("TestLoopTwoSounds");
		int shipsinkSoundId = soundIdMap.get(getClickResId());
		spookeyStreamId = soundPool.play(shipsinkSoundId, LEFT_VOLUME,
				RIGHT_VOLUME, DEFAULT_SOUND_PRIORITY, -1, 1);
		sinkShipStreamId = soundPool.play(soundIdMap.get(getRockSplashResId()),
				LEFT_VOLUME, RIGHT_VOLUME, DEFAULT_SOUND_PRIORITY, -1, 1);
		System.out.println("TestLoopTwoSounds");
	}

	public void stopTestLoopTwoSounds() {
		soundPool.stop(sinkShipStreamId);
		soundPool.stop(spookeyStreamId);
	}

	public void loopSound(int soundID, int loopIterations) {

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
	
	private int getBackgroundSound() {
		// TODO : select background motive from the saved background preferences
		int resId = sounds[0];
		return resId;
	}

	/**
	 * Start the service
	 */
	public void onStart() {
		// player.start();
	}

	/**
	 * This method should pause the
	 */
	public void pause() {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		player.stop();
		player.release();

		Set<Integer> keySet = soundIdMap.keySet();
		for (Integer integer : keySet) {
			soundPool.stop(soundIdMap.get(integer));
		}
		soundPool.release();
	}	
}
