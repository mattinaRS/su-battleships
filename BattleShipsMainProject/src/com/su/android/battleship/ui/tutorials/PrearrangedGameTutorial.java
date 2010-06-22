package com.su.android.battleship.ui.tutorials;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.su.android.battleship.R;
import com.su.android.battleship.cfg.GameDifficulty;
import com.su.android.battleship.cfg.GamePreferences;
import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.Ship;
import com.su.android.battleship.ui.SinglePlayer;
import com.su.android.battleship.ui.adapter.GameBoardImageAdapter;
import com.su.android.battleship.ui.adapter.MinimapImageAdapter;
import com.su.android.battleship.util.ShipUtil;

/**
 * @author Tony
 * 
 */
public class PrearrangedGameTutorial extends SinglePlayer {

	private Button doneButton;
	private Button examineButton;
	private StringBuilder message;
	private static final int CORRECTIVE_DIALOG = -1;
	private static final int TUTORIAL_EXPLANATION = 0;
	private static final int EXAMINATION_MODE = 1;
	private static final int INSTRUCTION_MODE = 2;
	private static final int PLAY_MODE = 3;
	private static final int DONE = 4;

	private Button lastInstructionButton;
	private int tutorialState;
	private int returnToState;
	private String[] instructions;
	private int instructionCounter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CORRECTIVE_DIALOG:
			return new AlertDialog.Builder(PrearrangedGameTutorial.this)
					.setIcon(R.drawable.help_icon).setTitle(
							"Please follow the instructions.")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// does nothing
								}
							}).create();
		case TUTORIAL_EXPLANATION:
			return new AlertDialog.Builder(PrearrangedGameTutorial.this)
					.setIcon(R.drawable.help_icon).setTitle(
							"Explanation of the tutorial").setMessage(
							"The Explanation").setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									tutorialState = INSTRUCTION_MODE;
									message.replace(0, message.length(),
											instructions[instructionCounter]);
									showDialog(INSTRUCTION_MODE);
								}
							}).create();
		case EXAMINATION_MODE:
			return new AlertDialog.Builder(PrearrangedGameTutorial.this)
					.setIcon(R.drawable.help_icon).setTitle(
							"Explanation of the touched item").setMessage(
							"The Explanation").setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									tutorialState = returnToState;
								}
							}).create();
		case INSTRUCTION_MODE:
			return new AlertDialog.Builder(PrearrangedGameTutorial.this)
					.setIcon(R.drawable.help_icon).setTitle(
							"Instruction N:").setMessage(
							"The Explanation").setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// does nothing
								}
							}).create();
		case DONE:
			return new AlertDialog.Builder(PrearrangedGameTutorial.this)
					.setIcon(R.drawable.help_icon).setTitle(
							"Are you done with the tutorial?")
					.setPositiveButton("Yes",
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
		case EXAMINATION_MODE:
			((AlertDialog) dialog).setMessage(message);
			if (instructionCounter == 4) {
				((AlertDialog) dialog).setButton(AlertDialog.BUTTON_POSITIVE,
						"OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								tutorialState = returnToState;
								nextInstruction();
								((ImageView)boardImageAdapter.getItem(12)).setImageResource(R.drawable.yellow);
							}
						});
			} else {
				((AlertDialog) dialog).setButton(AlertDialog.BUTTON_POSITIVE,
						"OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								tutorialState = returnToState;
							}
						});
			}
			break;
		case INSTRUCTION_MODE:
			((AlertDialog) dialog).setTitle("Instruction N: " + (instructionCounter+1) + "/" + instructions.length);
			((AlertDialog) dialog).setMessage(message);
			break;
		}
	}

	@Override
	protected void initGame() {
		tutorialState = EXAMINATION_MODE;

		Ship[] generatedShipPosition1 = getFixedShips();
		Ship[] generatedShipPosition2 = getFixedShips();

		GameDifficulty difficulty = (GameDifficulty) GamePreferences
				.getPreference(this, GamePreferences.PREFERENCE_DIFFICULTY);
		short code = (short) difficulty.getInfo();

		game = new GameAi((short) 0, generatedShipPosition1,
				generatedShipPosition2, code);

		displayGameScreen();
		attachActionListeners();

		message = new StringBuilder(getString(R.string.tutorial_prearranged_game_explanation));
		showDialog(TUTORIAL_EXPLANATION);

		Resources res = getResources();
		instructions = res
				.getStringArray(R.array.prearanged_game_tutorial_instructions);
		instructionCounter = 0;
		returnToState = INSTRUCTION_MODE;
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

	private void displayGameScreen() {
		setContentView(R.layout.prearanged_game_tutorial);

		lastInstructionButton = (Button) findViewById(R.id.LastInstructionButton);

		fireButton = (Button) findViewById(R.id.FireButton);

		boardGrid = (GridView) findViewById(R.id.GridViewAFD);
		boardImageAdapter = new GameBoardImageAdapter(this);
		boardGrid.setAdapter(boardImageAdapter);

		minimapGrid = (GridView) findViewById(R.id.GridViewMiniMap);
		short[] playerShipFields = ShipUtil.getShipsFields(game
				.getPlayerShips(GameAi.PLAYER_INDEX));
		minimapImageAdapter = new MinimapImageAdapter(this, playerShipFields);
		minimapGrid.setAdapter(minimapImageAdapter);
		doneButton = (Button) findViewById(R.id.DoneButton);
		examineButton = (Button) findViewById(R.id.ExamineButton);
		animationView = (View) findViewById(R.id.AnimationView);
	}

	private void attachActionListeners() {
		// boardGame GridView listener sets the aimedField property and changes
		// aim color
		boardGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				boolean executeAction;
				switch (tutorialState) {
				case EXAMINATION_MODE:
					executeAction = false;
					message.replace(0, message.length(),
							getString(R.string.tutorial_game_board_explanation));
					showDialog(EXAMINATION_MODE);
					break;
				case INSTRUCTION_MODE:
					executeAction = true;
					switch (instructionCounter) {
					case 0:
						nextInstruction();
						((ImageView) boardImageAdapter.getItem(2))
								.setImageResource(R.drawable.yellow);
						break;
					case 1:
						if (position == 2) {
							nextInstruction();
							break;
						}
					case 5:
						if (position == 12) {
							nextInstruction();
							break;
						}
					default:
						showDialog(CORRECTIVE_DIALOG);
						executeAction = false;
					}
					break;
				case PLAY_MODE:
					executeAction = true;
					break;
				default:
					executeAction = true;
				}

				if (executeAction) {
					ImageView _iv = (ImageView) v;

					// means that there is second click on the same button - FIRE !
					if (aimedField == position) {
						if (shotsMap.containsKey(position)) {
							Toast.makeText(PrearrangedGameTutorial.this,
									"Already fired this spot.", 1000).show();
						} else {
							executeFire(_iv, position);
						}					
					} else {
						aimAtField(_iv, position);
					}
				}
			}

		});

		fireButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean executeAction;
				switch (tutorialState) {
				case EXAMINATION_MODE:
					message.replace(0, message.length(),
							getString(R.string.tutorial_fire_button_explanation));
					showDialog(EXAMINATION_MODE);
					executeAction = false;
					break;
				case INSTRUCTION_MODE:
					executeAction = true;
					switch (instructionCounter) {
					case 2:
						nextInstruction();
						break;
					case 6:
						nextInstruction();
						tutorialState = PLAY_MODE;
						returnToState = PLAY_MODE;
						break;
					default:
						executeAction = false;
						showDialog(CORRECTIVE_DIALOG);
					}
					break;
				case PLAY_MODE:
					executeAction = true;
					break;
				default:
					executeAction = true;
				}

				if (executeAction) {
					if (aimedField == NO_FIELD_IS_AIMED) {
						Toast.makeText(PrearrangedGameTutorial.this,
								"Aim board field in order to fire.", 1000).show();
					} else {
						if (shotsMap.containsKey(aimedField)) {
							Toast.makeText(PrearrangedGameTutorial.this,
									"Already fired this spot.", 1000).show();
						} else {
							// quite obvious error - communication with imageViews is
							// through the adapted , not through the gridView
							ImageView field = (ImageView) boardImageAdapter
									.getItem(aimedField);
							executeFire(field, aimedField);
						}					
					}
				}
			}
		});

		minimapGrid.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (tutorialState == EXAMINATION_MODE
						|| (tutorialState == INSTRUCTION_MODE && instructionCounter == 4)) {
					message.replace(0, message.length(),
							getString(R.string.tutorial_minimap_explanation));
					showDialog(EXAMINATION_MODE);
				}
				return true;
			}
		});

		lastInstructionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tutorialState == EXAMINATION_MODE) {
					message.replace(0, message.length(),
							getString(R.string.tutorial_show_last_instruction_btn_explanation));
					showDialog(tutorialState);
				} else {
					message.replace(0, message.length(),
							instructions[instructionCounter]);
					showDialog(INSTRUCTION_MODE);
				}
			}
		});

		doneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DONE);
			}
		});

		examineButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean executeAction;
				switch (tutorialState) {
				case INSTRUCTION_MODE:
					if (instructionCounter <= 4) {
						executeAction = false;
						if (instructionCounter == 3) {
							nextInstruction();
						} else {
							showDialog(CORRECTIVE_DIALOG);
						}
					} else {
						executeAction = true;
					}
					break;
				case EXAMINATION_MODE:
					executeAction = false;
					message.replace(0, message.length(),
							getString(R.string.tutorial_help_button_explanation));
					showDialog(tutorialState);
					break;
				default:
					executeAction = true;
				}

				if (executeAction) {
					tutorialState = EXAMINATION_MODE;
				}
			}
		});
	}

	private void nextInstruction() {
		instructionCounter++;
		message.replace(0, message.length(), instructions[instructionCounter]);
		showDialog(INSTRUCTION_MODE);
	}

}
