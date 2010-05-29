package com.su.android.battleship.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.su.android.battleship.R;

/**
 * Class for displaying the About screen
 * @author Rosen
 */


public class About extends Activity implements OnClickListener {
	/**
	 * Called when the activity is starting
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		Button buttonBack = (Button) findViewById(R.id.ButtonBack);
		buttonBack.setOnClickListener(this);
	}
	
	/**
	 * Called when a button is clicked 
	 * @param button
	 */
	public void onClick(View button) {
		switch(button.getId()) {
		case R.id.ButtonBack:
			finish();
			break;
		}
	}

}
