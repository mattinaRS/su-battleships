package com.su.android.battleship.service;

import com.su.android.battleship.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class SoundService extends Service {
	private MediaPlayer player;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		player = MediaPlayer.create(this, R.raw.spooky);
		player.setLooping(true); // Set looping
		player.start();
	}
	
	public void onStart() {
		player.start();
	}
	
	public void onDestroy() {
		player.stop();
	}
}
