package com.su.android.battleship.ui.tutorials;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.su.android.battleship.R;

public class TutorialsScreen extends Activity implements OnClickListener{

	private Button mButtonAimAndFireTutorial;
	private Button mButtonFixShipGameTutorial;
	private Button mButtonMoveShipTutorial;
	private Button mButtonBack;
	
	
	/**
	 * Called when activity is starting
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorials_menu);				
		
		mButtonAimAndFireTutorial = (Button) findViewById(R.id.ButtonAimFireTutorial);
		mButtonAimAndFireTutorial.setOnClickListener(this);
		
		mButtonFixShipGameTutorial = (Button) findViewById(R.id.ButtonSetShipsGameTutorial);
		mButtonFixShipGameTutorial.setOnClickListener(this);
		
		mButtonMoveShipTutorial = (Button) findViewById(R.id.ButtonMoveShipTutorial);
		mButtonMoveShipTutorial.setOnClickListener(this);
		
		mButtonBack = (Button) findViewById(R.id.ButtonBackTutorials);
		mButtonBack.setOnClickListener(this);		
	}

	/**
	 * Called when a menu button has been clicked
	 * 
	 * @param button	menu button instance
	 */
	public void onClick(View button) {
		Intent mIntent = null;
		switch (button.getId()) {
		case R.id.ButtonAimFireTutorial:		
			mIntent = new Intent(button.getContext(),AimAndFireTutorial.class);
			startActivity(mIntent);			
			break;
		case R.id.ButtonSetShipsGameTutorial:
			mIntent = new Intent(button.getContext(),FixShipGameTutorial.class);
			startActivity(mIntent);
			break;
		case R.id.ButtonMoveShipTutorial:
			mIntent = new Intent(button.getContext(), MoveSingleShipTutorial.class);
			startActivity(mIntent);
			break;
		case R.id.ButtonBackTutorials:
			finish();
		default:
			break;
		}
	}
}
