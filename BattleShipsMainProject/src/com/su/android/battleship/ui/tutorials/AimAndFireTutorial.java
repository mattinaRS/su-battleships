package com.su.android.battleship.ui.tutorials;

import com.su.android.battleship.ui.adapter.GameBoardImageAdapter;

import com.su.android.battleship.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This Activity renders simple 10x10 buttons-grid and a big Fire button
 * The UI user can aim by touching grid's buttons and after aim is made ,she can 
 * fire using the Fire button.
 * 
 * Demo is adopting simple aim logic and dislpay - not 'sights' supported yet
 * and no real data objects are used yet (i.e. - Game , Board , Ship etc.)
 *  
 * @author vasil.konstantinov
 *
 */
public class AimAndFireTutorial extends Activity {
	
	protected static final int NO_FIELD_IS_AIMED = -1;
	
	protected GameBoardImageAdapter boardImageAdapter;
	protected int aimedField = NO_FIELD_IS_AIMED;
	
	protected GridView boardGrid;
	protected Button fireButton;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		initGame();
	}
	
	protected void displayGameScreen(){
		setContentView(R.layout.aim_fire_tutorial);		
		fireButton = (Button) findViewById(R.id.ImageViewFB);
		
		boardGrid = (GridView) findViewById(R.id.GridViewAFD);
		
		boardImageAdapter = new GameBoardImageAdapter(this);	
		boardGrid.setAdapter(boardImageAdapter);
	}
	
	protected void attachActionListeners(){
		//boardGame GridView listener sets the aimedField property and changes aim color
		boardGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
				ImageView _iv = (ImageView) v;
				//means that there is second click on the same button - FIRE !
				if(aimedField == position){
					executeFire(_iv);
				}else{
					if(aimedField != NO_FIELD_IS_AIMED){
						ImageView _oldSelectedField = (ImageView)boardImageAdapter.getItem(aimedField);
						_oldSelectedField.setImageResource(R.drawable.blue);
					}
					_iv.setImageResource(R.drawable.yellow);
					aimedField = position;
				}				
			}
		});
		
		fireButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(aimedField == NO_FIELD_IS_AIMED){
					Toast.makeText(AimAndFireTutorial.this, "Aim board field in order to fire.", 1000).show();
				}else{
					ImageView field = (ImageView) boardGrid.getItemAtPosition(aimedField);
					executeFire(field);
				}
			}			
		});		
	}
	
	private void executeFire(ImageView _iv){
		_iv.setImageResource(R.drawable.red);//mark as fired
		_iv.setClickable(false);//make imageView unclickable
		aimedField = NO_FIELD_IS_AIMED;
	}
	
	protected void initGame(){
		displayGameScreen();
		attachActionListeners();
	}
}
