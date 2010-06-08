package com.android.test.soundservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SoundServiceTest extends SoundEnabledActiviry {
	/** Called when the activity is first created. */

	private Button clickSoundBtn;
	private Button missleSoundBtn;
	private Button rockSplashSoundBtn;
	private Button explosionSoundBtn;
	private Button shipSinkSoundBtn;
	

//	private SoundService soundService;
	
	private MenuButtonSoundEnabledClickListener onClickListener = new MenuButtonSoundEnabledClickListener() {
		
		@Override
		public void playMenuButtonSound() {
			//for test demo menu default sound is disabled
		}
		
		@Override
		public void handleClick(View v) {
			switch (v.getId()) {
			case R.id.Button01:
				getSoundService().playClick();
				break;
			case R.id.Button02:
				getSoundService().playMissleShot();
				break;
			case R.id.Button03:
				getSoundService().playRockSplash();
				break;				
			case R.id.Button04:
				getSoundService().playExplosion();
				break;
			case R.id.Button05:
				getSoundService().playShipSink();
				break;
			default:
				break;
			}
		}
	};
/*
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			// This is called when the connection with the service has been
			// established, giving us the service object we can use to
			// interact with the service. Because we have bound to a explicit
			// service that we know is running in our own process, we can
			// cast its IBinder to a concrete class and directly access it.
			soundService = ((SoundService.SoundServiceBinder) service)
					.getService();
		}

		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.
			// Because it is running in our same process, we should never
			// see this happen.
			soundService = null;
		}
	};
*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		clickSoundBtn = (Button) findViewById(R.id.Button01);
		missleSoundBtn = (Button) findViewById(R.id.Button02);
		rockSplashSoundBtn = (Button) findViewById(R.id.Button03);
		explosionSoundBtn = (Button) findViewById(R.id.Button04);
		shipSinkSoundBtn = (Button) findViewById(R.id.Button05);
		
		clickSoundBtn.setOnClickListener(this.onClickListener);
		missleSoundBtn.setOnClickListener(this.onClickListener);
		explosionSoundBtn.setOnClickListener(this.onClickListener);
		shipSinkSoundBtn.setOnClickListener(this.onClickListener);
		rockSplashSoundBtn.setOnClickListener(this.onClickListener);
	}

	@Override
	protected void onDestroy() {	
		super.onDestroy();
//		soundService.stopSelf();
	}
/*
	private void initSoundService() {
		bindService(new Intent(SoundServiceTest.this, SoundService.class),
				mConnection, Context.BIND_AUTO_CREATE);
	}
*/
	
}