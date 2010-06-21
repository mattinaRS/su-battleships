package com.su.android.battleship.ui.tutorials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.su.android.battleship.R;
import com.su.android.battleship.cfg.GameDifficulty;
import com.su.android.battleship.cfg.GamePreferences;
import com.su.android.battleship.data.BoardFieldStatus;
import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.Ship;
import com.su.android.battleship.data.ShipPositionGenerator;
import com.su.android.battleship.service.SoundService;
import com.su.android.battleship.ui.ArrangeShips;
import com.su.android.battleship.ui.adapter.GameBoardImageAdapter;
import com.su.android.battleship.ui.adapter.MinimapImageAdapter;
import com.su.android.battleship.ui.data.ActivityShipComunicator;
import com.su.android.battleship.util.GameSounds;

/**
 * Activity for a Game Tutorial
 * NOTE : code has developed in time so ships positions are now generated and the name should be changed 
 * NOTE : because of lack of time , this Activity was used in Single player which is not correct.
 * This Activity IS and WILL BE TUTORIAL activiry - a SinglePlayer Acvitivy has to be design and created. 
 * @author vasko
 *
 */
public class FixShipGameTutorial extends AimAndFireTutorial {

	private GameAi game;
	private GridView minimapGrid;
	private MinimapImageAdapter minimapImageAdapter;
	private GameSounds gameSounds;
	
	private View animationView;
	private AnimationDrawable animation;
	
	// collections holding boards hit/missed positions.
	// for restoring state purpose	
	private ArrayList<Short> minimapHits = new ArrayList<Short>();
	private ArrayList<Short> minimapMisses = new ArrayList<Short>();
	
	private static final String BUNDLE_GAME = "BSG_FSG";	
	private static final String BUNDLE_MINIMAP = "BSG_FSG_MM";
	
	private HashMap<Integer, Integer> shotsMap = new HashMap<Integer, Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		gameSounds = new GameSounds(this);
		if ( (Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND) ) {
			gameSounds.playSound(1);
		}
	}
	
	protected void displayGameScreen() {
		setContentView(R.layout.fixed_ship_game);

		fireButton = (Button) findViewById(R.id.FireButton);

		boardGrid = (GridView) findViewById(R.id.GridViewAFD);
		boardImageAdapter = new GameBoardImageAdapter(this);
		boardGrid.setAdapter(boardImageAdapter);

		minimapGrid = (GridView) findViewById(R.id.GridViewMiniMap);
		short[] playerShipFields = getPlayerShipFields();
		minimapImageAdapter = new MinimapImageAdapter(this, playerShipFields);
		minimapGrid.setAdapter(minimapImageAdapter);
		
		TextView textYou = (TextView) findViewById(R.id.text_nickname);
		if ( GamePreferences.getPreference(this, GamePreferences.PREFERENCE_NICKNAME).toString().equals("") ) {
			textYou.setText("Player");
		} else {
			textYou.setText(GamePreferences.getPreference(this, GamePreferences.PREFERENCE_NICKNAME).toString());
		}
		
		animationView = (View) findViewById(R.id.AnimationView);
	}

	protected void attachActionListeners() {
		// boardGame GridView listener sets the aimedField property and changes
		// aim color
		boardGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				ImageView _iv = (ImageView) v;

				// means that there is second click on the same button - FIRE !
				if (aimedField == position) {
					if (shotsMap.containsKey(position)) {
						Toast.makeText(FixShipGameTutorial.this,
								"Already fired this spot.", 1000).show();
					} else {
						executeFire(_iv, position);
						if (game.isGameOver()) {
							Toast.makeText(FixShipGameTutorial.this,
									"Game over.Winner is the player.", 1000).show();
							return;
						} else {
							continueGameProcess();
						}
					}					
				} else {
					if (aimedField != NO_FIELD_IS_AIMED) {
						ImageView _oldSelectedField = (ImageView) boardImageAdapter
								.getItem(aimedField);
						Integer oldImageResource = shotsMap.get(aimedField);
						if (oldImageResource != null) {
							_oldSelectedField.setImageResource(oldImageResource);
						} else {
							_oldSelectedField.setImageResource(boardImageAdapter.getTransparent());
						}						
					}
					_iv.setImageResource(boardImageAdapter.getCrosair());
					aimedField = position;
				}
			}
		});

		fireButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (aimedField == NO_FIELD_IS_AIMED) {
					Toast.makeText(FixShipGameTutorial.this,
							"Aim board field in order to fire.", 1000).show();
				} else {
					if (shotsMap.containsKey(aimedField)) {
						Toast.makeText(FixShipGameTutorial.this,
								"Already fired this spot.", 1000).show();
					} else {
						// quite obvious error - communication with imageViews is
						// through the adapted , not through the gridView
						ImageView field = (ImageView) boardImageAdapter
								.getItem(aimedField);
						executeFire(field, aimedField);
						if (game.isGameOver()) {
							Toast.makeText(FixShipGameTutorial.this,
									"Game over.Winner is the player.", 1000).show();
							return;
						} else {
							continueGameProcess();
						}
					}					
				}
			}
		});
	}

	private void executeFire(ImageView _iv, int position) {
		short newFieldStatus = game.executeMove((short) 0, (short) aimedField);
		if (BoardFieldStatus.isShipAttackedStatus(newFieldStatus) || BoardFieldStatus.isShipDestroyedStatus(newFieldStatus)) {
			shotsMap.put(position, boardImageAdapter.getCrash());
			_iv.setImageResource(boardImageAdapter.getCrash());// mark as fired
			
			// start explode animation
			startAnimation(aimedField % 10, aimedField / 10, R.drawable.explosion);
			
			boardHits.add(position);
			if ( (Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_VIBRATION) ) {
				Vibrator v = (Vibrator) getSystemService(FixShipGameTutorial.VIBRATOR_SERVICE); 
				v.vibrate(600);
			}
			
			if ( (Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND) ) {
				gameSounds.playSound(2);
			}
		} else {
			shotsMap.put(position, boardImageAdapter.getMiss());
			_iv.setImageResource(boardImageAdapter.getMiss());// mark as fired
			
			// start splash animation
			startAnimation(aimedField % 10, aimedField / 10, R.drawable.splash);
			
			boardMisses.add(position);
			if ( (Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_VIBRATION) ) {
				Vibrator v = (Vibrator) getSystemService(FixShipGameTutorial.VIBRATOR_SERVICE); 
				v.vibrate(200);
			}
			
			if ( (Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND) ) {
				gameSounds.playSound(3);
			}
		}
		_iv.setClickable(false);// make imageView unclickable
		// _iv.setFocusable(false);
		aimedField = NO_FIELD_IS_AIMED;
		if (game.isGameOver()) {
			if ( (Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND) ) {
				gameSounds.playSound(4);
			}
		}
	}

	protected void initGame() {
		// Ship[] generatedShipPosition1 = generator.getHardcodedShipPosition();
		// Ship[] generatedShipPosition2 = generator.getHardcodedShipPosition();
		stopService(new Intent(this, SoundService.class));
		
		Intent intent = new Intent(this, ArrangeShips.class);
		startActivityForResult(intent, 1);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == ArrangeShips.BACK) {
			finish();
		} else {
			ShipPositionGenerator generator = new ShipPositionGenerator();
			ActivityShipComunicator ships = (ActivityShipComunicator) data
					.getBundleExtra("myBundle").getSerializable("ships");
//			Ship[] generatedShipPosition1 = generator.getRandomShipsPosition();
			Ship[] generatedShipPosition1 = ships.getShips();
			Ship[] generatedShipPosition2 = generator.getRandomShipsPosition();
			
			
				
			GameDifficulty difficulty = (GameDifficulty)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_DIFFICULTY);
			short code = (short) difficulty.getInfo();
			

			game = new GameAi((short) 0, generatedShipPosition1,
					generatedShipPosition2,code);
			
			displayGameScreen();
			attachActionListeners();
		}
	}

	private void continueGameProcess() {
		// NOTE : here should be implemented logic that communicates with a
		// GameProcessWrapper
		// in order to continue the game process - to obtain second players move
		// from a
		// configurate place - Ai , BlueTooth , Http client/server communication
		short fieldAttackedByAi = game.makeMoveForAi();
		short fieldStatus = game.getPlayerBoard(GameAi.PLAYER_INDEX)[fieldAttackedByAi];
		
		if (BoardFieldStatus.isShipAttackedStatus(fieldStatus)
				|| BoardFieldStatus.isShipDestroyedStatus(fieldStatus)) {
			minimapImageAdapter.setCrash(fieldAttackedByAi);
			minimapHits.add(fieldAttackedByAi);
		} else {
			minimapImageAdapter.setMiss(fieldAttackedByAi);
			minimapMisses.add(fieldAttackedByAi);
		}

		/*ImageView _iv = (ImageView) minimapImageAdapter
				.getItem(fieldAttackedByAi);
		if (BoardFieldStatus.isShipAttackedStatus(fieldStatus)
				|| BoardFieldStatus.isShipDestroyedStatus(fieldStatus)) {
			// change minimap field color to red - ship is hit
			_iv.setImageResource(R.drawable.crash_mini);
			// other way is to use public resource ids stored in the adapter as
			// it should be responsible for the gridView's rendering
		} else {
			// change it to grey - water is hit
			_iv.setImageResource(R.drawable.miss_mini);
		}*/

		if (game.isGameOver()) {
			Toast.makeText(FixShipGameTutorial.this,
					"Game over.Winner is the AI.", 1000).show();
			if ( (Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND) ) {
				gameSounds.playSound(5);
			}
		}
	}

	private short[] getPlayerShipFields() {
		List<Short> resultList = new ArrayList<Short>();
		Ship[] playerShips = game.getPlayerShips(GameAi.PLAYER_INDEX);
		Ship tempShip;
		for (short i = 0; i < playerShips.length; i++) {
			tempShip = playerShips[i];
			for (short j = 0; j < tempShip.getLength(); j++) {
				resultList.add(tempShip.getBoardFields()[j]);
			}
		}
		short[] result = new short[resultList.size()];
		for (short k = 0; k < result.length; k++) {
			result[k] = resultList.get(k);
		}
		return result;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if ( (Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND) ) {
			startService(new Intent(this, SoundService.class));
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
		outState.putSerializable(BUNDLE_GAME, game);
		ArrayList<ArrayList<Short>> minimapList = new ArrayList<ArrayList<Short>>();
		minimapList.add(minimapHits);
		minimapList.add(minimapMisses);
		outState.putSerializable(BUNDLE_MINIMAP, minimapList);
	}
	
	/**
	 * Restores the state of the arrange ships board as it was before 
	 * the app was killed 
	 * @param oldState state before app was killed
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void restoreState(Bundle oldState) {
		game = (GameAi) oldState.getSerializable(BUNDLE_GAME);
		
		displayGameScreen();
		attachActionListeners();
		
		ArrayList<ArrayList<Integer>> boardOldState = (ArrayList<ArrayList<Integer>>) oldState.getSerializable(BUNDLE_BOARD);
		boardHits = boardOldState.get(0);
		boardMisses = boardOldState.get(1);
		
		for (Integer i : boardHits) {
			ImageView view = (ImageView) boardGrid.getItemAtPosition(i);
			view.setImageResource(boardImageAdapter.getCrash());
		}
		for (Integer i : boardMisses) {
			ImageView view = (ImageView) boardGrid.getItemAtPosition(i);
			view.setImageResource(boardImageAdapter.getMiss());
		}
		
		ArrayList<ArrayList<Short>> minimapOldState = (ArrayList<ArrayList<Short>>) oldState.getSerializable(BUNDLE_MINIMAP);
		minimapHits = minimapOldState.get(0);
		minimapMisses = minimapOldState.get(1);				
		
		for (Short i : minimapHits) {
			minimapImageAdapter.setCrash(i);
		}
		for (Short i : minimapMisses) {
			minimapImageAdapter.setMiss(i);
		}
		
		aimedField = oldState.getInt(BUNDLE_AIMED_FIELD);
		if (aimedField != NO_FIELD_IS_AIMED) {
			ImageView view = (ImageView) boardGrid.getItemAtPosition(aimedField);
			view.setImageResource(boardImageAdapter.getCrosair());
		}
	}
	
	/**
	 * Execute frame animation at given grid position (x, y)
	 * 
	 * @param x			Grid column index
	 * @param y			Grid row index
	 * @param resource	Animation resource to play
	 */
	protected void startAnimation(int x, int y, int resource) {
		moveAnimationView(x, y);
		
		animationView.setBackgroundResource(resource);
		animation = (AnimationDrawable) animationView.getBackground();
		
		if (animation.isRunning()) {
			animation.stop();
		}
		animation.start();
	}
	
	/**
	 * Move animation view to some column and row
	 * 
	 * @param x	Grid column index
	 * @param y	Grid row index
	 */
	protected void moveAnimationView(int x, int y) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) animationView.getLayoutParams();
		
		float dpi = getResources().getDisplayMetrics().density;
		params.width = params.height = (int) (50 * dpi);
		params.leftMargin = (int) ((x * 30 - 10) * dpi);
		params.topMargin = (int) ((y * 30 - 10) * dpi);
		
		animationView.setLayoutParams(params);
	}
}
