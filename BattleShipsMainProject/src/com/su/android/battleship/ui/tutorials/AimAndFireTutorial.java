package com.su.android.battleship.ui.tutorials;

import java.util.ArrayList;

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
	
	/** collections holding boards hit/missed positions.
	 *  for restoring state purpose
	 */
	protected ArrayList<Integer> boardHits = new ArrayList<Integer>();
	/**
	 * misses
	 */
	protected ArrayList<Integer> boardMisses = new ArrayList<Integer>();
	
	/**
	 * aimed field bundle identifier
	 */
	protected static final String BUNDLE_AIMED_FIELD = "BSG_FSG_AF";
	/**
	 * board bundle identifier
	 */
	protected static final String BUNDLE_BOARD = "BSG_FSG_BRD";
	
	/**
	 * flag to check whether or not a field is targeted when a fire is executed
	 */
	protected static final int NO_FIELD_IS_AIMED = -1;
	
	/**
	 * boardImageAdapter
	 */
	protected GameBoardImageAdapter boardImageAdapter;
	
	/**
	 * used to save the currently marked field
	 */
	protected int aimedField = NO_FIELD_IS_AIMED;
	
	/**
	 * boardGrid
	 */
	protected GridView boardGrid;
	
	/**
	 * fireButton
	 */
	protected Button fireButton;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		if (savedInstanceState != null) {
			restoreState(savedInstanceState);
		} else {
			initGame();
		}
		
	}
	
	/**
	 * displays the GameScreen
	 */
	protected void displayGameScreen(){
		setContentView(R.layout.aim_fire_tutorial);		
		fireButton = (Button) findViewById(R.id.ImageViewFB);
		
		boardGrid = (GridView) findViewById(R.id.GridViewAFD);
		
		boardImageAdapter = new GameBoardImageAdapter(this);	
		boardGrid.setAdapter(boardImageAdapter);
	}
	
	/**
	 * attaches the ActionListeners
	 */
	protected void attachActionListeners(){
		//boardGame GridView listener sets the aimedField property and changes aim color
		boardGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
				ImageView _iv = (ImageView) v;
				//means that there is second click on the same button - FIRE !
				if(aimedField == position){
					executeFire(_iv, position);
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
					executeFire(field, aimedField);
				}
			}			
		});		
	}
	
	private void executeFire(ImageView _iv, int position) {
		_iv.setImageResource(R.drawable.red);//mark as fired
		boardHits.add(position);
		_iv.setClickable(false);//make imageView unclickable
		aimedField = NO_FIELD_IS_AIMED;
	}
	/**
	 * inits the Game
	 */
	protected void initGame(){
		displayGameScreen();
		attachActionListeners();
	}
	
	/**
	 * Restores the state of the arrange ships board as it was before 
	 * the app was killed 
	 * @param oldState state before app was killed
	 */
	@SuppressWarnings("unchecked")
	protected void restoreState(Bundle oldState) {
		displayGameScreen();
		attachActionListeners();
		
		ArrayList<ArrayList<Integer>> boardOldState = (ArrayList<ArrayList<Integer>>) oldState.getSerializable(BUNDLE_BOARD);
		boardHits = boardOldState.get(0);
		
		for (Integer i : boardHits) {
			ImageView view = (ImageView) boardGrid.getItemAtPosition(i);
			view.setImageResource(R.drawable.red);
		}
		
		aimedField = oldState.getInt(BUNDLE_AIMED_FIELD);
		if (aimedField != NO_FIELD_IS_AIMED) {
			ImageView view = (ImageView) boardGrid.getItemAtPosition(aimedField);
			view.setImageResource(R.drawable.yellow);
		}
	}
	
	/**
	 * The settings are saved in a Bundle.
	 * When the application become active again the settings will be
	 * loaded from the bundle
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(BUNDLE_AIMED_FIELD, aimedField);
		ArrayList<ArrayList<Integer>> boardList = new ArrayList<ArrayList<Integer>>();
		boardList.add(boardHits);
		boardList.add(boardMisses);
		outState.putSerializable(BUNDLE_BOARD, boardList);
	}
}
