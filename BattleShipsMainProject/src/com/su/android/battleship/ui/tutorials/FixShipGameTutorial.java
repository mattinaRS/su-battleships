package com.su.android.battleship.ui.tutorials;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
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
import com.su.android.battleship.ui.ArrangeShips;
import com.su.android.battleship.ui.adapter.GameBoardImageAdapter;
import com.su.android.battleship.ui.adapter.MinimapImageAdapter;
import com.su.android.battleship.ui.data.ActivityShipComunicator;

public class FixShipGameTutorial extends AimAndFireTutorial {

	private GameAi game;
	private GridView minimapGrid;
	private MinimapImageAdapter minimapImageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
					executeFire(_iv);
					if (game.isGameOver()) {
						Toast.makeText(FixShipGameTutorial.this,
								"Game over.Winner is the player.", 1000).show();
						return;
					} else {
						continueGameProcess();
					}
				} else {
					if (aimedField != NO_FIELD_IS_AIMED) {
						ImageView _oldSelectedField = (ImageView) boardImageAdapter
								.getItem(aimedField);
						_oldSelectedField.setImageResource(boardImageAdapter.getTransparent());
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
					// quite obvious error - communication with imageViews is
					// through the adapted , not through the gridView
					ImageView field = (ImageView) boardImageAdapter
							.getItem(aimedField);
					executeFire(field);
					if (game.isGameOver()) {
						Toast.makeText(FixShipGameTutorial.this,
								"Game over.Winner is the player.", 1000).show();
						return;
					} else {
						continueGameProcess();
					}
				}
			}
		});
	}

	private void executeFire(ImageView _iv) {
		short newFieldStatus = game.executeMove((short) 0, (short) aimedField);
		if (BoardFieldStatus.isShipAttackedStatus(newFieldStatus) || BoardFieldStatus.isShipDestroyedStatus(newFieldStatus)) {
			_iv.setImageResource(boardImageAdapter.getCrash());// mark as fired
		} else {
			_iv.setImageResource(boardImageAdapter.getMiss());// mark as fired
		}
		_iv.setClickable(false);// make imageView unclickable
		// _iv.setFocusable(false);
		aimedField = NO_FIELD_IS_AIMED;
	}

	protected void initGame() {
		// Ship[] generatedShipPosition1 = generator.getHardcodedShipPosition();
		// Ship[] generatedShipPosition2 = generator.getHardcodedShipPosition();

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
		// TODO : here should be implemented logic that communicates with a
		// GameProcessWrapper
		// in order to continue the game process - to obtain second players move
		// from a
		// configurate place - Ai , BlueTooth , Http client/server communication
		short fieldAttackedByAi = game.makeMoveForAi();
		short fieldStatus = game.getPlayerBoard(GameAi.PLAYER_INDEX)[fieldAttackedByAi];

		ImageView _iv = (ImageView) minimapImageAdapter
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
		}

		if (game.isGameOver()) {
			Toast.makeText(FixShipGameTutorial.this,
					"Game over.Winner is the AI.", 1000).show();
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
}
