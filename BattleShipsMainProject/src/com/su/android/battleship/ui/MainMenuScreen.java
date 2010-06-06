package com.su.android.battleship.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.su.android.battleship.R;
import com.su.android.battleship.cfg.GamePreferences;
import com.su.android.battleship.service.SoundService;

/**
 * First application screen. The main menu - Play, Options, Credits, Quit
 * 
 * @author Nikola
 * 
 */
public class MainMenuScreen extends Activity implements OnClickListener {

	/**
	 * This is configuration flag - whether to show loading screen or not
	 * TODO : design and implement loading screen as a self containing widget that can be attached 
	 * to every activity configured to support loading screen 
	 */
	private static final boolean isLoadingScreen = true;

	/**
	 * Called when activity is starting
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// start gameLoading thread
		GameLoadingThread gameLoadingThread = new GameLoadingThread(handler);
		gameLoadingThread.execute("Loading...");

		if (isLoadingScreen) {
			displayLoadingScreen();
		} else {
			displayGameMenu();
		}
	}

	/**
	 * This method holds the logic for loading the main game menu
	 */
	private void displayGameMenu() {
		setContentView(R.layout.main_menu);

		Button buttonPlay = (Button) findViewById(R.id.ButtonPlay);
		buttonPlay.setOnClickListener(this);

		Button buttonOptions = (Button) findViewById(R.id.ButtonOptions);
		buttonOptions.setOnClickListener(this);

		Button buttonAbout = (Button) findViewById(R.id.ButtonAbout);
		buttonAbout.setOnClickListener(this);

		Button buttonQuit = (Button) findViewById(R.id.ButtonQuit);
		buttonQuit.setOnClickListener(this);
		
		if ( (Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND) ) {
			startService(new Intent(this, SoundService.class));
		}
		
		if ( !(Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND) ) {
			stopService(new Intent(this, SoundService.class));
		}
	}	

	/**
	 * Called when a menu button has been clicked
	 * 
	 * @param button
	 *            menu button instance
	 */
	public void onClick(View button) {
		switch (button.getId()) {
		case R.id.ButtonPlay:
			Intent intent = new Intent(this, GameMenuScreen.class);
			startActivity(intent);
			break;
		case R.id.ButtonOptions:
			Intent intentPrefs = new Intent(this, PreferencesMenuScreen.class);
			startActivity(intentPrefs);
			break;
		case R.id.ButtonAbout:
			Intent intentAbout = new Intent(this, About.class);
			startActivity(intentAbout);
			break;
		case R.id.ButtonQuit:
			stopService(new Intent(this, SoundService.class));
			finish();
			break;
		}
	}
	
	//==============================================================================================================
	//Logic supporting loading screen

	private ProgressBar progressBar;
	private LoadScreenAsyncTask progressBarTask;


	// Define the Handler that receives messages from the gameLoading thread and
	// update the
	// speed of the progressBar thread
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (!isLoadingScreen) {
				return;
			}
			boolean isLoaded = msg.getData().getBoolean("isLoaded");
			if (isLoaded) {
				progressBarTask.setStatus(LoadScreenAsyncTask.STATUS_FINISHED);
			}
		}
	};	
	
	/**
	 * This method holds the logic for loading the loading_screen Remember that
	 * this Activity is configurable and the call to this method can be disabled
	 */
	private void displayLoadingScreen() {
		setContentView(R.layout.progressbar_loadscreen);

		// start progressBar worker thread 
		progressBarTask = new LoadScreenAsyncTask();
		progressBarTask.execute("Loading");
	}

	
	/**
	 * This class represents a background worker thread that will do all the
	 * game loading job.This is the common and desirable behavior - the UI
	 * thread should not be charged with back-end calculations.They should be
	 * transfered to another thread instead. This thread communicates with the
	 * progressBarUpdate thread via a Handler objects
	 * 
	 * @author vasko
	 * 
	 */
	private class GameLoadingThread extends AsyncTask<Object, Void, Void> {

		private Handler mHandler;

		public GameLoadingThread(Handler h) {
			mHandler = h;
		}

		@Override
		protected Void doInBackground(Object... params) {
			// simulate activity here
//			TestAI_SPPFD.test();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO: handle exception
			}

			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putBoolean("isLoaded", true);
			msg.setData(b);
			mHandler.sendMessage(msg);
			return null;
		}

	}

	/**
	 * This class represents background thread responsible for ProgressBar updates.
	 * 
	 * @author vasko
	 */
	private class LoadScreenAsyncTask extends
			AsyncTask<String, Integer, String> {

		private static final int MAX_PROGRESS_STEPS = 15;
		private int status = STATUS_RUNNING_NORMAL;
		public static final int STATUS_RUNNING_NORMAL = 1;;
		//public static final int STATUS_RUNNING_FAST = 2;
		public static final int STATUS_FINISHED = 3;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setContentView(R.layout.progressbar_loadscreen);
			progressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
			progressBar.setMax(MAX_PROGRESS_STEPS);
			progressBar.setProgressDrawable(getResources().getDrawable(
					R.drawable.progress_horizontal));
		}

		protected void onProgressUpdate(Integer... progress) {
			progressBar.setProgress(progress[0]);
		}

		@Override
		protected String doInBackground(String... params) {
			int i = 0;
			while (status == STATUS_RUNNING_NORMAL) {
				try {
					Thread.sleep(200);
					i++;
				} catch (InterruptedException e) {

				}
				publishProgress(i);
			}
			while (i < MAX_PROGRESS_STEPS) {
				try {
					Thread.sleep(20);
					i++;
				} catch (InterruptedException e) {

				}
				publishProgress(i);
			}
			return "MyAsyncTask done successfully";
		}

		/**
		 * Executed by UI thread after task has finished its job
		 */
		protected void onPostExecute(String result) {
			displayGameMenu();
		}

		/**
		 * This method is used to tell the thread whether it is time to force
		 * faster progress of the progressBar and whether it is time to
		 * terminate
		 * 
		 * @param status
		 */
		public void setStatus(int status) {
			this.status = status;
		}

	}
	
		/**
		 * Stops the background music when finishing the application
		 */
	    protected void onDestroy() {
		  super.onDestroy();
		  stopService(new Intent(this, SoundService.class));
		}
	    
	    /**
	     * Starts/stops sound when the activity is restarted 
	     * (useful when returned to after changes in settings menu)
	     */
		@Override
		protected void onRestart() {
			if ( (Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND) ) {
				startService(new Intent(this, SoundService.class));
			}
	    	
	    	if ( !(Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND) ) {
				stopService(new Intent(this, SoundService.class));
			}
			// TODO Auto-generated method stub
			super.onRestart();
		}

}
