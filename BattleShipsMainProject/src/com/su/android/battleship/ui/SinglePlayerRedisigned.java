package com.su.android.battleship.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.su.android.battleship.ui.adapter.GameBoardImageAdapter;
import com.su.android.battleship.ui.adapter.MinimapImageAdapter;
import com.su.android.battleship.ui.data.ActivityShipComunicator;
import com.su.android.battleship.util.GameSounds;
import com.su.android.battleship.util.ShipUtil;

/**
 * Activity for a Game Tutorial NOTE : code has developed in time so ships
 * positions are now generated and the name should be changed NOTE : because of
 * lack of time , this Activity was used in Single player which is not correct.
 * This Activity IS and WILL BE TUTORIAL activiry - a SinglePlayer Acvitivy has
 * to be design and created.
 * 
 * @author vasko
 * 
 */
public class SinglePlayerRedisigned extends Activity {
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

	/**
	 * manages the game and provides AI moves
	 */
	protected GameAi game;
	/**
	 * GridView for the minimap
	 */
	protected GridView minimapGrid;
	/**
	 * ImageAdapter for the minimap
	 */
	protected MinimapImageAdapter minimapImageAdapter;
	/**
	 * game sounds
	 */
	protected GameSounds gameSounds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initGame();

		gameSounds = new GameSounds(this);
		if ((Boolean) GamePreferences.getPreference(this,
				GamePreferences.PREFERENCE_SOUND)) {
			gameSounds.playSound(1);
		}
	}

	/**
	 * 
	 */
	protected void initGame() {
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
			// Ship[] generatedShipPosition1 =
			// generator.getRandomShipsPosition();
			Ship[] generatedShipPosition1 = ships.getShips();
			Ship[] generatedShipPosition2 = generator.getRandomShipsPosition();

			GameDifficulty difficulty = (GameDifficulty) GamePreferences
					.getPreference(this, GamePreferences.PREFERENCE_DIFFICULTY);
			short code = (short) difficulty.getInfo();

			game = new GameAi((short) 0, generatedShipPosition1,
					generatedShipPosition2, code);

			displayGameScreen();
			attachActionListeners();
		}
	}

	/**
	 * Executes fire at the ImageView parameter
	 * 
	 * @param _iv
	 */
	protected void executeFire(ImageView _iv) {
		short newFieldStatus = game.executeMove((short) 0, (short) aimedField);
		if (BoardFieldStatus.isShipAttackedStatus(newFieldStatus)
				|| BoardFieldStatus.isShipDestroyedStatus(newFieldStatus)) {
			_iv.setImageResource(boardImageAdapter.getCrash());// mark as fired

			if ((Boolean) GamePreferences.getPreference(this,
					GamePreferences.PREFERENCE_VIBRATION)) {
				Vibrator v = (Vibrator) getSystemService(SinglePlayer.VIBRATOR_SERVICE);
				v.vibrate(600);
			}

			if ((Boolean) GamePreferences.getPreference(this,
					GamePreferences.PREFERENCE_SOUND)) {
				gameSounds.playSound(2);
			}
		} else {
			_iv.setImageResource(boardImageAdapter.getMiss());// mark as fired

			if ((Boolean) GamePreferences.getPreference(this,
					GamePreferences.PREFERENCE_VIBRATION)) {
				Vibrator v = (Vibrator) getSystemService(SinglePlayer.VIBRATOR_SERVICE);
				v.vibrate(200);
			}

			if ((Boolean) GamePreferences.getPreference(this,
					GamePreferences.PREFERENCE_SOUND)) {
				gameSounds.playSound(3);
			}
		}
		_iv.setClickable(false);// make imageView unclickable
		// _iv.setFocusable(false);
		aimedField = NO_FIELD_IS_AIMED;
		if (game.isGameOver()) {
			if ((Boolean) GamePreferences.getPreference(this,
					GamePreferences.PREFERENCE_SOUND)) {
				gameSounds.playSound(4);
			}
		}
		if (game.isGameOver()) {
			Toast.makeText(SinglePlayerRedisigned.this,
					"Game over.Winner is the player.", 1000).show();
			return;
		} else {
			continueGameProcess();
		}
	}

	/**
	 * 
	 */
	protected void continueGameProcess() {
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
		} else {
			minimapImageAdapter.setMiss(fieldAttackedByAi);
		}

		if (game.isGameOver()) {
			Toast.makeText(SinglePlayerRedisigned.this,
					"Game over.Winner is the AI.", 1000).show();
			if ((Boolean) GamePreferences.getPreference(this,
					GamePreferences.PREFERENCE_SOUND)) {
				gameSounds.playSound(5);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if ((Boolean) GamePreferences.getPreference(this,
				GamePreferences.PREFERENCE_SOUND)) {
			startService(new Intent(this, SoundService.class));
		}
	}

	/**
	 * @param _iv
	 * @param position
	 */
	protected void aimAtField(ImageView _iv, int position) {
		if (aimedField != NO_FIELD_IS_AIMED) {
			ImageView _oldSelectedField = (ImageView) boardImageAdapter
					.getItem(aimedField);
			_oldSelectedField.setImageResource(boardImageAdapter
					.getTransparent());
		}
		_iv.setImageResource(boardImageAdapter.getCrosair());
		aimedField = position;

	}

	private void displayGameScreen() {
		setContentView(R.layout.single_player_play);

		fireButton = (Button) findViewById(R.id.FireButton);

		boardGrid = (GridView) findViewById(R.id.GridViewAFD);
		boardImageAdapter = new GameBoardImageAdapter(this);
		boardGrid.setAdapter(boardImageAdapter);

		minimapGrid = (GridView) findViewById(R.id.GridViewMiniMap);
		short[] playerShipFields = ShipUtil.getShipsFields(game
				.getPlayerShips(GameAi.PLAYER_INDEX));
		minimapImageAdapter = new MinimapImageAdapter(this, playerShipFields);
		minimapGrid.setAdapter(minimapImageAdapter);
	}

	private void attachActionListeners() {
		// boardGame GridView listener sets the aimedField property and changes
		// aim color
		boardGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				ImageView _iv = (ImageView) v;

				// means that there is second click on the same button - FIRE !
				if (aimedField == position) {
					executeFire(_iv);
				} else {
					aimAtField(_iv, position);
				}
			}
		});

		fireButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (aimedField == NO_FIELD_IS_AIMED) {
					Toast.makeText(SinglePlayerRedisigned.this,
							"Aim board field in order to fire.", 1000).show();
				} else {
					// quite obvious error - communication with imageViews is
					// through the adapted , not through the gridView
					ImageView field = (ImageView) boardImageAdapter
							.getItem(aimedField);
					executeFire(field);
				}
			}
		});
	}

}
