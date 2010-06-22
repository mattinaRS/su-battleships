package com.su.android.battleship.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.su.android.battleship.R;
import com.su.android.battleship.cfg.GamePreferences;
import com.su.android.battleship.data.Game;
import com.su.android.battleship.data.Ship;
import com.su.android.battleship.data.ShipPositionGenerator;
import com.su.android.battleship.data.transformer.ShipRepresentationTransformer;
import com.su.android.battleship.ui.adapter.MoveShipsAdapter;
import com.su.android.battleship.ui.data.ActivityShipComunicator;
import com.su.android.battleship.util.GameSounds;
import com.su.android.battleship.util.ShipUtil;
import com.su.data.Direction;
import com.su.data.ShipFieldsHolder;
import com.su.generator.rule.FieldForbiddingRule;
import com.su.generator.rule.FieldForbiddingRuleSquareImpl;
import com.su.manager.ForbiddenPositionsManager;

/**
 * A UI screen for arranging ships
 * @author Tony
 * 
 */
public class ArrangeShips extends Activity {

	private static final String BUNDLE_SHIPS = "BSG_ARS_SHP";
	private static final String BUNDLE_FORBIDDEN_POSITION = "BSG_ARS_FPOS";
	private static final String BUNDLE_BOARD = "BSG_ARS_BRD";
	
	/**
	 * value returned to the single player activity 
	 * when the back button is pressed
	 */
	public static final int BACK = 0;
	/**
	 * value returned to the single player activity 
	 * when the apply button is pressed
	 */
	public static final int ACCEPT = 1;

	/**
	 * The forbiding rule used to determine which field is forbidden
	 */
	protected FieldForbiddingRule rule;
	/**
	 * Players ships
	 */
	protected Ship[] ships;
	/**
	 * manages forbidden fields when ships are moved about
	 */
	protected ForbiddenPositionsManager forbiddenPositions;
	/**
	 * the game board on which the ships are moved around
	 */
	protected short[] board;

	/**
	 * stores the selected ship
	 */
	protected ShipFieldsHolder selectedShip;
	/**
	 * 
	 */
	protected short selectedShipField;
	/**
	 * the index in the ships array of the selected ship
	 */
	protected int selectedShipIndex;
	/**
	 * last possible location of the moved ship. When an imposible arrangement is attempted 
	 * the moved ship returns to this position 
	 */
	protected ShipFieldsHolder lastPossiblePosition;
	/**
	 * the position last position of the cursor measured in game board positions
	 */
	protected short oldCursorPosition;

	/**
	 * ImageAdapter for moving ships
	 */
	protected MoveShipsAdapter moveShipsAdapter;
	/**
	 * the board
	 */
	protected GridView boardGrid;
	/**
	 * accept button
	 */
	protected Button btnAccept;
	/**
	 * cancel button
	 */
	protected Button btnCancel;
	/**
	 * rotate button
	 */
	protected Button btnRotate;
	
	/**
	 * game sounds
	 */
	protected GameSounds gameSounds;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			restoreState(savedInstanceState);
		} else {
			initGame();
		}		
	}

	/**
	 * Tries to rotate a ship
	 * @param v
	 */
	protected void rotate(View v) {
		if (selectedShipIndex != -1) {
			boolean isRotatable = true;
			Ship ship = ships[selectedShipIndex];
			ShipFieldsHolder oldSFH = ShipRepresentationTransformer
					.getShipFieldsHolderFromShip(ship);
			ShipFieldsHolder tmpSFH = ShipRepresentationTransformer
					.getShipFieldsHolderFromShip(ship);
			for (short forbiddenField : ShipUtil
					.getForbiddenFieldsFromShip(ship, rule)) {
				forbiddenPositions.removeForbiddenField(forbiddenField);
			}
			if (tmpSFH.getDirection() == Direction.HORIZONTAL) {
				int newLastFieldVerticalCoordinate;
				newLastFieldVerticalCoordinate = tmpSFH.getFirstField()/Game.BOARD_SIDE;
				newLastFieldVerticalCoordinate += (tmpSFH.getLength() - 1);
				if(newLastFieldVerticalCoordinate >= Game.BOARD_SIDE)
					isRotatable = false;
				tmpSFH.setDirection(Direction.VERTICAL);
			} else {
				int newLastFieldHorizontalCoordinate;
				newLastFieldHorizontalCoordinate = tmpSFH.getFirstField()%Game.BOARD_SIDE;
				newLastFieldHorizontalCoordinate += (tmpSFH.getLength() - 1);
				if(newLastFieldHorizontalCoordinate >= Game.BOARD_SIDE)
					isRotatable = false;
				tmpSFH.setDirection(Direction.HORIZONTAL);
			}
			if (isShipOk(tmpSFH) && isRotatable) {
				paint(oldSFH, moveShipsAdapter.getSeaPicture(),
						moveShipsAdapter.getSeaPicture(),
						PaintAction.RESTORE);
				for (int i : ship.getBoardFields()) {
					board[i] = -1;
				}
				paint(tmpSFH, moveShipsAdapter.getShipPicture(),
						moveShipsAdapter.getShipPicture(),
						PaintAction.PAINT_OVER);
				ship = ShipRepresentationTransformer
						.getShipFromShipFieldsHolder(tmpSFH);
				ships[selectedShipIndex] = ship;
				for (int i : ship.getBoardFields()) {
					board[i] = (short) selectedShipIndex;
				}
			} else {
				Toast.makeText(ArrangeShips.this,
						"Last touched ship cannot be rotated.", 3000).show();
			}
			for (short forbiddenField : ShipUtil
					.getForbiddenFieldsFromShip(ship, rule)) {
				forbiddenPositions.addForbiddenField(forbiddenField);
			}
		}
	}

	private boolean isShipOk(ShipFieldsHolder ship) {
		Ship tmpShip = ShipRepresentationTransformer
				.getShipFromShipFieldsHolder(ship);
		boolean shipIsOk = true;

		for (short position : tmpShip.getBoardFields()) {
			if (position < 0 || position > Game.BOARD_SIDE * Game.BOARD_SIDE
					|| forbiddenPositions.isForbidden((short) position)) {
				shipIsOk = false;
			}
		}
		return shipIsOk;
	}

	/**
	 * manages an OnTouchDown event
	 * @param position
	 */
	protected void manageOnTouchDown(short position) {
		selectedShipIndex = board[position];
		Ship ship = ships[selectedShipIndex];
		for (short shipField : ship.getBoardFields()) {
			board[shipField] = -1;
		}
		selectedShip = ShipRepresentationTransformer
				.getShipFieldsHolderFromShip(ship);
		lastPossiblePosition = new ShipFieldsHolder(selectedShip.getLength(),
				selectedShip.getFirstField(), selectedShip.getDirection());
		if (selectedShip.getDirection() == Direction.HORIZONTAL) {
			selectedShipField = (short) (position - selectedShip
					.getFirstField());
		} else {
			selectedShipField = (short) ((position - selectedShip
					.getFirstField()) / Game.BOARD_SIDE);
		}
		oldCursorPosition = position;
		for (short forbiddenField : ShipUtil.getForbiddenFieldsFromShip(ship,
				rule)) {
			forbiddenPositions.removeForbiddenField(forbiddenField);
		}
		paint(selectedShip, moveShipsAdapter.getSelectedShipPicture(),
				moveShipsAdapter.getForbiddenFieldPicture(),
				PaintAction.PAINT_OVER);
	}

	/** 
	 * manages a OnTouchMove event
	 * @param position
	 */
	protected void manageOnTouchMove(short position) {
		if (position != oldCursorPosition) {
			paint(selectedShip, moveShipsAdapter.getSeaPicture(),
					moveShipsAdapter.getShipPicture(), PaintAction.RESTORE);
			int oldFirstField = selectedShip.getFirstField();
			selectedShip.setFirstField(oldFirstField += position
					- oldCursorPosition);
			oldCursorPosition = position;
			if (!paint(selectedShip, moveShipsAdapter.getSelectedShipPicture(),
					moveShipsAdapter.getForbiddenFieldPicture(),
					PaintAction.PAINT_OVER)) {
				lastPossiblePosition = new ShipFieldsHolder(selectedShip
						.getLength(), selectedShip.getFirstField(),
						selectedShip.getDirection());
			}
		}
	}

	/**
	 * Manages a OnTouchUp event
	 * @param position
	 */
	protected void manageOnTouchUp(short position) {
		paint(selectedShip, moveShipsAdapter.getSeaPicture(), moveShipsAdapter
				.getShipPicture(), PaintAction.RESTORE);
		paint(lastPossiblePosition, moveShipsAdapter.getShipPicture(),
				moveShipsAdapter.getShipPicture(), PaintAction.PAINT_OVER);
		Ship newShip = ShipRepresentationTransformer
				.getShipFromShipFieldsHolder(lastPossiblePosition);
		ships[selectedShipIndex] = newShip;
		for (short shipField : newShip.getBoardFields()) {
			board[shipField] = (short) selectedShipIndex;
		}
		for (short forbiddenField : ShipUtil.getForbiddenFieldsFromShip(
				newShip, rule)) {
			forbiddenPositions.addForbiddenField(forbiddenField);
		}
		if (selectedShip.getFirstField() != lastPossiblePosition
				.getFirstField()) {
			Toast
					.makeText(
							ArrangeShips.this,
							"Forbidden arrangement! Ship restored to last possible arrangement",
							3000).show();
		}
		selectedShip = lastPossiblePosition = null;

	}

	/**
	 * Initializes the state of the board
	 */
	protected void initGame() {
		selectedShipIndex = -1;
		ShipPositionGenerator generator = new ShipPositionGenerator();
		ships = generator.getRandomShipsPosition();
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
		if ( (Boolean)GamePreferences.getPreference(this, GamePreferences.PREFERENCE_SOUND) ) {
			gameSounds.playSound(1);
		}
	}
	
	/**
	 * Restores the state of the arrange ships board as it was before 
	 * the app was killed 
	 * @param oldState state before app was killed
	 */
	private void restoreState(Bundle oldState) {
		selectedShipIndex = -1;
		
		ships = (Ship[]) oldState.getSerializable(BUNDLE_SHIPS);
		short[] shipsFields = ShipUtil.getShipsFields(ships);
		moveShipsAdapter = new MoveShipsAdapter(this, shipsFields);
		
		rule = new FieldForbiddingRuleSquareImpl(Game.BOARD_SIDE);
		
		forbiddenPositions = (ForbiddenPositionsManager) oldState.getSerializable(BUNDLE_FORBIDDEN_POSITION);
		board = (short[]) oldState.getSerializable(BUNDLE_BOARD);
		selectedShip = null;
		lastPossiblePosition = null;
		
	}

	/**
	 * This function paints the fields of a ship that is being moved or restores
	 * the previous state of board fields when the ship is moved to a new
	 * position.
	 * 
	 * @param ship
	 *            holds the ship fields that are to be painted
	 * @param defaultColor
	 *            the color that is used to paint when the field isn't
	 *            exceptional
	 * @param alternativeColor
	 *            the color that is used to paint when the field is exceptional
	 * @param action
	 * @return If there is a forbidden field in the ship fields than the
	 *         function returns true
	 */
	private boolean paint(ShipFieldsHolder ship, int defaultColor,
			int alternativeColor, PaintAction action) {
		Ship tmpShip = ShipRepresentationTransformer
				.getShipFromShipFieldsHolder(ship);
		boolean notPossibleShipPosition = false;
		ImageView iv;

		for (short position : tmpShip.getBoardFields()) {
			iv = (ImageView) moveShipsAdapter.getItem(position);

			if (action == PaintAction.PAINT_OVER) {
				if (forbiddenPositions.isForbidden((short) position)) {
					((ImageView) iv).setImageResource(alternativeColor);
					notPossibleShipPosition = true;
				} else
					((ImageView) iv).setImageResource(defaultColor);
			}
			if (action == PaintAction.RESTORE) {
				if (board[position] != -1)
					((ImageView) iv).setImageResource(alternativeColor);
				else
					((ImageView) iv).setImageResource(defaultColor);
			}
		}
		return notPossibleShipPosition;
	}

	/**
	 * Description of calculatePosition This function calculates a position
	 * given coordinates from an onTouch event. It takes into account the size
	 * of the board, the touched field of the ship being moved and the size of
	 * the ship. If a horizontal ship with size 5 is being moved (dragged) by
	 * its top left field and the user's cursor gets next to the left border of
	 * the board this function shall return a position that is as if his cursor
	 * was 5 fields to the left.
	 * 
	 * @param coordinateX
	 *            the x coordinate of the user's cursor
	 * @param coordinateY
	 *            the x coordinate of the user's cursor
	 * @return the calculated position of the user's cursor in a linear massive
	 */
	protected short calculatePosition(float coordinateX, float coordinateY) {
		short x, y;
		short leftBorder;
		short rightBorder;
		short topBorder;
		short bottomBorder;
		// TODO : getPadding() should be getVerticalSpacing, but I don't know
		// how to get it. There is a set method but
		// no get method. Need research in google.

		int imageWidth = (boardGrid.getWidth() - (Game.BOARD_SIDE + 1)
				* boardGrid.getPaddingLeft())
				/ Game.BOARD_SIDE;
		int imageHeight = (boardGrid.getHeight() - (Game.BOARD_SIDE + 1)
				* boardGrid.getPaddingTop())
				/ Game.BOARD_SIDE;

		x = (short) (coordinateX / (imageWidth + boardGrid.getPaddingLeft()));
		y = (short) (coordinateY / (imageHeight + boardGrid.getPaddingTop()));
		topBorder = 0;
		bottomBorder = Game.BOARD_SIDE - 1;
		leftBorder = 0;
		rightBorder = Game.BOARD_SIDE - 1;

		if (selectedShip != null) {
			if (selectedShip.getDirection() == Direction.HORIZONTAL) {
				leftBorder = selectedShipField;
				rightBorder = (short) (Game.BOARD_SIDE
						- selectedShip.getLength() + selectedShipField);
			} else {
				topBorder = selectedShipField;
				bottomBorder = (short) (Game.BOARD_SIDE
						- selectedShip.getLength() + selectedShipField);
			}
		}

		if (x < leftBorder)
			x = leftBorder;
		if (x > rightBorder)
			x = rightBorder;
		if (y < topBorder)
			y = topBorder;
		if (y > bottomBorder)
			y = bottomBorder;

		if (y * Game.BOARD_SIDE + x < 0) {
			int i = 0;
			i++;
		}

		return (short) (y * Game.BOARD_SIDE + x);
	}

	private enum PaintAction {
		PAINT_OVER, RESTORE
	}
	
	/**
	 * The settings are saved in a Bundle.
	 * When the application become active again the settings will be
	 * loaded from the bundle
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(BUNDLE_SHIPS, ships);
		outState.putSerializable(BUNDLE_FORBIDDEN_POSITION, forbiddenPositions);
		outState.putSerializable(BUNDLE_BOARD, board);				
	}
			
	/**
	 * Displays the game screen
	 */
	private void displayGameScreen() {
		setContentView(R.layout.arrange_ships);

		btnAccept = (Button) findViewById(R.id.ButtonAccept);
		btnCancel = (Button) findViewById(R.id.ButtonCancel);
		btnRotate = (Button) findViewById(R.id.ButtonRotate);

		boardGrid = (GridView) findViewById(R.id.GridViewMoveShips);
		boardGrid.setAdapter(moveShipsAdapter);

	}
	
	/**
	 * attaches action listeners to:
	 * touch events
	 * clicking the buttons
	 */
	private void attachActionListeners() {

		boardGrid.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				short position;

				position = calculatePosition(event.getX(), event.getY());

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (board[position] != -1)
						manageOnTouchDown(position);
					break;
				case MotionEvent.ACTION_MOVE:
					if (selectedShip != null)
						manageOnTouchMove(position);
					break;
				case MotionEvent.ACTION_UP:
					if (selectedShip != null)
						manageOnTouchUp(position);
					break;
				}
				return false;
			}
		});

		btnRotate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rotate(v);
			}

		});

		btnAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				ActivityShipComunicator comm = new ActivityShipComunicator(
						ships);
				b.putSerializable("ships", comm);
				Intent intent = getIntent();
				intent.putExtra("myBundle", b);

				setResult(ACCEPT, intent);
				finish();
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(BACK, new Intent());
				finish();
			}
		});
	}
}
