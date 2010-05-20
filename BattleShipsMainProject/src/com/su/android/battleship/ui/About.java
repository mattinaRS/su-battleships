package com.su.android.battleship.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.su.android.battleship.R;

public class About extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		Vibrator v = (Vibrator) getSystemService(About.VIBRATOR_SERVICE); 
		v.vibrate(3000);
		
		Button buttonBack = (Button) findViewById(R.id.ButtonBack);
		buttonBack.setOnClickListener(this);
	}
	
	
	public void onClick(View button) {
		switch(button.getId()) {
		case R.id.ButtonBack:
			finish();
			break;
		}
	}

}
