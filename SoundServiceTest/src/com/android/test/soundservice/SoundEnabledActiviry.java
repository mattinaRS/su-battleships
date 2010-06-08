package com.android.test.soundservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

public class SoundEnabledActiviry extends Activity {
	
	private SoundService soundService;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initSoundService();
	}
	
	public SoundService getSoundService(){
		return soundService;
	}
	
	
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
	
	private void initSoundService() {
		bindService(new Intent(SoundEnabledActiviry.this, SoundService.class),
				mConnection, Context.BIND_AUTO_CREATE);
	}
	
	
	@Override
	protected void onDestroy() {	
		super.onDestroy();
		soundService.stopSelf();
	}
	
}
