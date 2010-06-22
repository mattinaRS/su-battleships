package com.su.android.battleship.ui.tutorials;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.su.android.battleship.R;
import com.su.android.battleship.cfg.GamePreferences;
import com.su.android.battleship.data.Game;
import com.su.android.battleship.data.Ship;
import com.su.android.battleship.ui.ArrangeShipsRedisigned;
import com.su.android.battleship.ui.adapter.MoveShipsAdapter;
import com.su.android.battleship.util.GameSounds;
import com.su.android.battleship.util.ShipUtil;
import com.su.generator.rule.FieldForbiddingRuleSquareImpl;
import com.su.manager.ForbiddenPositionsManager;

/**
 * @author Tony
 * 
 */
public class ArrangeShipsTutorial extends ArrangeShipsRedisigned {

	private StringBuilder message;
	private static final int CORRECTIVE_DIALOG = -1;
	private static final int TUTORIAL_EXPLANATION = 0;
	private static final int INSTRUCTION_MODE = 1;
	private static final int PLAY_MODE = 2;
	private static final int DONE = 3;

	private Button btnShowLastInstruction;
	private int tutorialState;
	private String[] instructions;
	private int instructionCounter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void initGame() {
		selectedShipIndex = -1;
		ships = getFixedShips();
		short[] shipsFields = ShipUtil.getShipsFields(ships);
		moveShipsAdapter = new MoveShipsAdapter(this, shipsFields);

		rule = new FieldForbiddingRuleSquareImpl(Game.BOARD_SIDE);
		forbiddenPositions = new ForbiddenPositionsManager();
		for (short i : shipsFields) {
			List<Integer> forbiddenFields = rule.getForbiddenFields(i);
			for (int ff : forbiddenFields) {
				forbiddenPositions.addForbiddenField((short) ff);
			}
		}
		board = new short[Game.BOARD_SIDE * Game.BOARD_SIDE];
		for (int i = 0; i < board.length; i++)
			board[i] = -1;
		for (int shipIndex = 0; shipIndex < ships.length; shipIndex++) {
			Ship tmpShip = ships[shipIndex];
			for (short shipField : tmpShip.getBoardFields()) {
				board[shipField] = (short) shipIndex;
			}
		}
		selectedShip = lastPossiblePosition = null;

		displayGameScreen();
		attachActionListeners();

		gameSounds = new GameSounds(this);
		if ((Boolean) GamePreferences.getPreference(this,
				GamePreferences.PREFERENCE_SOUND)) {
			gameSounds.playSound(1);
		}

		message = new StringBuilder(getString(R.string.tutorial_arrange_ships_explanation));
		showDialog(TUTORIAL_EXPLANATION);

		Resources res = getResources();
		instructions = res.getStringArray(R.array.arrange_ships_tutorial);
		instructionCounter = 0;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CORRECTIVE_DIALOG:
			return new AlertDialog.Builder(ArrangeShipsTutorial.this).setIcon(
					R.drawable.help_icon).setTitle(
					"Please follow the instructions.").setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// does nothing
						}
					}).create();
		case TUTORIAL_EXPLANATION:
			return new AlertDialog.Builder(ArrangeShipsTutorial.this).setIcon(
					R.drawable.help_icon).setTitle(
					"Explanation of the tutorial")
					.setMessage("The Explanation").setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									tutorialState = INSTRUCTION_MODE;
									message.replace(0, message.length(),
											instructions[instructionCounter]);
									showDialog(INSTRUCTION_MODE);
								}
							}).create();
		case INSTRUCTION_MODE:
			return new AlertDialog.Builder(ArrangeShipsTutorial.this).setIcon(
					R.drawable.help_icon).setTitle(
					"Instruction N: " + (instructionCounter+1) + "/"
							+ instructions.length)
					.setMessage("The Explanation").setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// does nothing
								}
							}).create();
		case DONE:
			return new AlertDialog.Builder(ArrangeShipsTutorial.this).setIcon(
					R.drawable.help_icon).setTitle(
					"Are you done with the tutorial?").setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							finish();
						}
					}).setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// does nothing
						}
					}).create();
		}

		return null;
	}

	@Override
	protected void onPrepareDialog(int id, android.app.Dialog dialog) {
		switch (id) {
		case TUTORIAL_EXPLANATION:
			((AlertDialog) dialog).setTitle("Explanation of the tutorial");
			((AlertDialog) dialog).setMessage(message);
			break;
		case INSTRUCTION_MODE:
			((AlertDialog) dialog).setTitle("Instruction N: " + (instructionCounter+1) + "/" + instructions.length);
			((AlertDialog) dialog).setMessage(message);
			break;
		}
	}

	/**
	 * Displays the game screen
	 */
	private void displayGameScreen() {
		setContentView(R.layout.arrange_ships_tutorial);

		btnCancel = (Button) findViewById(R.id.ButtonDone);
		btnRotate = (Button) findViewById(R.id.ButtonRotate);
		btnShowLastInstruction = (Button) findViewById(R.id.ButtonShowLastInstruction);

		boardGrid = (GridView) findViewById(R.id.GridViewMoveShips);
		boardGrid.setAdapter(moveShipsAdapter);

	}

	/**
	 * attaches action listeners to: touch events clicking the buttons
	 */
	private void attachActionListeners() {

		boardGrid.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				short position;

				position = calculatePosition(event.getX(), event.getY());

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (board[position] != -1) {
						manageOnTouchDown(position);
						if (tutorialState == INSTRUCTION_MODE) {
							switch (instructionCounter) {
							case 0:
								instructionCounter++;
								Toast.makeText(ArrangeShipsTutorial.this,
										instructions[instructionCounter], 2000)
										.show();
								break;
							}
						}
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (selectedShip != null) {
						if (tutorialState == INSTRUCTION_MODE
								&& instructionCounter == 1
								&& position != oldCursorPosition) {
							Toast.makeText(ArrangeShipsTutorial.this,
									"Very good. Now lift your finger.", 2000)
									.show();
						}
						manageOnTouchMove(position);
					}
					break;
				case MotionEvent.ACTION_UP:
					if (selectedShip != null) {
						manageOnTouchUp(position);
						if (tutorialState == INSTRUCTION_MODE) {
							switch (instructionCounter) {
							case 1:
								nextInstruction();
								break;
							case 2:
								nextInstruction();
								break;
							}
						}
					}
					break;
				}
				return false;
			}
		});

		btnRotate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean executeAction;
				if (tutorialState == INSTRUCTION_MODE) {
					if (instructionCounter == 3) {
						executeAction = true;
						nextInstruction();
						tutorialState = PLAY_MODE;
					} else {
						showDialog(CORRECTIVE_DIALOG);
						executeAction = false;
					}
				} else {
					executeAction = true;
				}

				if (executeAction)
					rotate(v);
			}

		});

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DONE);
			}
		});

		btnShowLastInstruction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				message.replace(0, message.length(),
						instructions[instructionCounter]);
				showDialog(INSTRUCTION_MODE);
			}
		});
	}

	private void nextInstruction() {
		instructionCounter++;
		message.replace(0, message.length(), instructions[instructionCounter]);
		showDialog(INSTRUCTION_MODE);
	}

	private Ship[] getFixedShips() {
		Ship[] result = new Ship[5];
		result[0] = new Ship((short) 5, (new short[] { 1, 2, 3, 4, 5 }));
		result[1] = new Ship((short) 4, (new short[] { 38, 48, 58, 68 }));
		result[2] = new Ship((short) 3, (new short[] { 41, 42, 43 }));
		result[3] = new Ship((short) 2, (new short[] { 55, 65 }));
		result[4] = new Ship((short) 2, (new short[] { 72, 73 }));

		return result;
	}
}
