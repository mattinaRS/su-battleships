package com.su.android.battleship.ui.tutorials;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.su.android.battleship.R;

/**
 * 
 * @author Vasko and Tony
 *
 */
public class TutorialsScreen extends Activity implements OnClickListener{

	private Button mButtonPrearrangedGameTutorial;
	private Button mButtonArrangeShipsTutorial;
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
		
		mButtonPrearrangedGameTutorial = (Button) findViewById(R.id.ButtonPrearrangedGameTutorial);
		mButtonPrearrangedGameTutorial.setOnClickListener(this);
		
		mButtonArrangeShipsTutorial = (Button) findViewById(R.id.ButtonArrangeShipsTutorial);
		mButtonArrangeShipsTutorial.setOnClickListener(this);
		
		mButtonMoveShipTutorial = (Button) findViewById(R.id.ButtonRules);
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
		case R.id.ButtonPrearrangedGameTutorial:		
			mIntent = new Intent(button.getContext(), PrearrangedGameTutorial.class);
			startActivity(mIntent);			
			break;
		case R.id.ButtonArrangeShipsTutorial:
			mIntent = new Intent(button.getContext(), ArrangeShipsTutorial.class);
			startActivity(mIntent);
			break;
		case R.id.ButtonRules:
			// do nothing for now
			break;
		case R.id.ButtonBackTutorials:
			finish();
		default:
			break;
		}
	}
}
