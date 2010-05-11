package com.su.android.battleship.ui;

import com.su.android.battleship.R;
import com.su.android.battleship.ui.tutorials.FixShipGameTutorial;
import com.su.android.battleship.ui.tutorials.TutorialsScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Game menu screen - Singleplayer, Multiplayer, Tutorials, Back
 * 
 * @author Nikola
 *
 */
public class GameMenuScreen extends Activity implements OnClickListener {

	/**
	 * Called when activity is starting
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_menu);
		
		Button buttonSingle = (Button) findViewById(R.id.ButtonSingle);
		buttonSingle.setOnClickListener(this);
		
		Button buttonMulti = (Button) findViewById(R.id.ButtonMulti);
		buttonMulti.setOnClickListener(this);
		
		Button buttonTutorials = (Button) findViewById(R.id.ButtonTutorials);
		buttonTutorials.setOnClickListener(this);
		
		Button buttonBack = (Button) findViewById(R.id.ButtonBack);
		buttonBack.setOnClickListener(this);
	}

	/**
	 * Called when a menu button has been clicked
	 * 
	 * @param button	menu button instance
	 */
	public void onClick(View button) {
		Intent intent = null;
		switch (button.getId()) {
		case R.id.ButtonSingle:
			intent = new Intent(this, FixShipGameTutorial.class);
			startActivity(intent);
			break;
		case R.id.ButtonMulti:
			break;
		case R.id.ButtonTutorials:
//			intent = new Intent(this, TutorialsScreen.class);
//			startActivity(intent);
			break;
		case R.id.ButtonBack:
			finish();
			break;
		}
	}

}
