package com.su.android.battleship.ui;

import java.util.List;

import com.su.android.battleship.R;
import com.su.android.battleship.data.Game;
import com.su.android.battleship.data.Ship;
import com.su.android.battleship.data.ShipPositionGenerator;
import com.su.android.battleship.data.transformer.ShipRepresentationTransformer;
import com.su.android.battleship.ui.adapter.MoveShipsAdapter;
import com.su.android.battleship.util.ShipUtil;
import com.su.data.Direction;
import com.su.data.ShipFieldsHolder;
import com.su.generator.rule.FieldForbiddingRule;
import com.su.generator.rule.FieldForbiddingRuleSquareImpl;
import com.su.manager.ForbiddenPositionsManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author User
 *
 */
public class ArrangeShips extends Activity{
	private FieldForbiddingRule rule;
	private Ship[] ships;
	private ForbiddenPositionsManager forbiddenPositions;
	private short[] board; 
	
	private ShipFieldsHolder selectedShip;
	private short selectedShipField;
	private int selectedShipIndex;
	private ShipFieldsHolder lastPossiblePosition;
	private short oldCursorPosition;
	
	protected MoveShipsAdapter moveShipsAdapter;
	protected GridView boardGrid;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		initGame();
		displayGameScreen();
		attachActionListeners();
	}

	protected void displayGameScreen(){
		setContentView(R.layout.arrange_ships);		
		
		boardGrid = (GridView) findViewById(R.id.GridViewMoveShips);
		boardGrid.setAdapter(moveShipsAdapter);
	}

	protected void attachActionListeners(){
        boardGrid.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
		        short position;

		        position = calculatePosition(event.getX(), event.getY());
		        
		        switch (event.getAction()) {
		        case MotionEvent.ACTION_DOWN:
		    		if(board[position]!=-1)
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
		        return true;
			}
		});
	}
	
	private void manageOnTouchDown(short position) {
		selectedShipIndex = board[position];
		Ship ship = ships[selectedShipIndex];
		for(short shipField : ship.getBoardFields()) {
			board[shipField] = -1;
		}
		selectedShip = ShipRepresentationTransformer.getShipFieldsHolderFromShip(ship);
		lastPossiblePosition = new ShipFieldsHolder(selectedShip.getLength(), 
				selectedShip.getFirstField(), selectedShip.getDirection());
		if (selectedShip.getDirection() == Direction.HORIZONTAL) {
			selectedShipField = (short)(position - selectedShip.getFirstField());
		}
		else {
			selectedShipField = (short)((position - selectedShip.getFirstField())/Game.BOARD_SIDE);
		}
		oldCursorPosition = position;
		for(short forbiddenField : ShipUtil.getForbiddenFieldsFromShip(ship, rule)) {
			forbiddenPositions.removeForbiddenField(forbiddenField);
		}
		paint(selectedShip, R.drawable.yellow, R.drawable.red, PaintAction.PAINT_OVER);
	}
	
	private void manageOnTouchMove(short position) {
		if(position != oldCursorPosition) {
			paint(selectedShip, R.drawable.blue, R.drawable.green, PaintAction.RESTORE);
			int oldFirstField = selectedShip.getFirstField();
			selectedShip.setFirstField(oldFirstField += position - oldCursorPosition);
			oldCursorPosition = position;
			if(!paint(selectedShip, R.drawable.yellow, R.drawable.red, PaintAction.PAINT_OVER)) {
	    		lastPossiblePosition = new ShipFieldsHolder(selectedShip.getLength(), 
                        selectedShip.getFirstField(), selectedShip.getDirection());
			}
		}
	}
	
	private void manageOnTouchUp(short position) {
		paint(selectedShip, R.drawable.blue, R.drawable.green, PaintAction.RESTORE);
		paint(lastPossiblePosition, R.drawable.green, R.drawable.green, PaintAction.PAINT_OVER);
		Ship newShip = ShipRepresentationTransformer.getShipFromShipFieldsHolder(lastPossiblePosition);
		ships[selectedShipIndex] = newShip;
		for(short shipField : newShip.getBoardFields()) {
			board[shipField] = (short) selectedShipIndex;
		}
		for(short forbiddenField : ShipUtil.getForbiddenFieldsFromShip(newShip, rule)) {
			forbiddenPositions.addForbiddenField(forbiddenField);
		}
		if(selectedShip.getFirstField() != lastPossiblePosition.getFirstField()) {
			Toast.makeText(ArrangeShips.this, "Forbidden arrangement! Ship restored to last possible arrangement", 3000).show();
		}
		selectedShip = lastPossiblePosition = null;
		
	}
	
	protected void initGame(){
		ShipPositionGenerator generator = new ShipPositionGenerator(); 
		ships = generator.getRandomShipsPosition();
		short[] shipsFields = ShipUtil.getShipsFields(ships);
		moveShipsAdapter = new MoveShipsAdapter(this, shipsFields);

		rule = new FieldForbiddingRuleSquareImpl(Game.BOARD_SIDE);
		forbiddenPositions = new ForbiddenPositionsManager();
		for(short i : shipsFields) {
			List<Integer> forbiddenFields = rule.getForbiddenFields(i); 
			for(int ff : forbiddenFields) {
				forbiddenPositions.addForbiddenField((short) ff);
			}
		}
		board = new short[Game.BOARD_SIDE*Game.BOARD_SIDE];
		for(int i=0; i<board.length; i++) board[i] = -1;
		for(int shipIndex = 0; shipIndex<ships.length; shipIndex++) {
			Ship tmpShip = ships[shipIndex];
			for(short shipField : tmpShip.getBoardFields()) {
				board[shipField] = (short)shipIndex;
			}
		}
		selectedShip = lastPossiblePosition = null;
	}
	
	
	
	/** This function paints the fields of a ship that is being moved or restores the previous state of board fields
	 *  when the ship is moved to a new position.
	 * @param ship                holds the ship fields that are to be painted 
	 * @param defaultColor        the color that is used to paint when the field isn't exceptional
	 * @param alternativeColor    the color that is used to paint when the field is exceptional
	 * @param action              
	 * @return                    If there is a forbidden field in the ship fields than the function returns true
	 */
	private boolean paint(ShipFieldsHolder ship, int defaultColor, int alternativeColor, PaintAction action) {
		short positionIterator;
		boolean notPossibleShipPosition = false;
		ImageView iv;
		
		if(ship.getDirection() == Direction.HORIZONTAL) positionIterator = 1;
		else positionIterator = Game.BOARD_SIDE;
		
		int position = ship.getFirstField();
		for(int i=0; i<ship.getLength(); i++){
			iv = (ImageView) moveShipsAdapter.getItem(position);

			if(action == PaintAction.PAINT_OVER) {
				if(forbiddenPositions.isForbidden((short) position)) {
					((ImageView) iv).setImageResource(alternativeColor);
					notPossibleShipPosition = true;
				}
				else 
					((ImageView) iv).setImageResource(defaultColor);
			}
			if(action == PaintAction.RESTORE) {
				if(board[position] != -1)
					((ImageView) iv).setImageResource(alternativeColor);
				else
					((ImageView) iv).setImageResource(defaultColor);
			}
			position += positionIterator;
		}
		return notPossibleShipPosition;
	}
	
	/** Description of calculatePosition
	 * This function calculates a position given coordinates from an onTouch event.
	 * It takes into account the size of the board, the touched field of the ship being moved
	 * and the size of the ship. If a horizontal ship with size 5 is being moved (dragged) 
	 * by its top left field and the user's cursor gets next to the left border of the board
	 * this function shall return a position that is as if his cursor was 5 fields to the left.
	 * 
	 * @param coordinateX    the x coordinate of the user's cursor
	 * @param coordinateY    the x coordinate of the user's cursor
	 * @return               the calculated position of the user's cursor in a linear massive 
	 */
	private short calculatePosition(float coordinateX, float coordinateY){
        short x, y;
        short leftBorder;
        short rightBorder;
        short topBorder;
        short bottomBorder;
        //Vasko finds the numbers {4,4.5,28,31} "very well" and "obviously" co-relative 
        //TODO : board imageViews' sizes will be hard-coded as @values and the coordinate formulas should depend only
        //on those hard-coded values , if UI size properties are to be loosely coupled with the the coordinates business logic
        //otherwise changes in the UI size properties will reflect on and induce changes in the formulas.
        
        x = (short)((coordinateX - 4)/31);
		y = (short)((coordinateY - 4.5)/28);
		topBorder = 0;
		bottomBorder = Game.BOARD_SIDE - 1;
		leftBorder = 0;
		rightBorder = Game.BOARD_SIDE - 1;

		if(selectedShip != null) {
			if (selectedShip.getDirection() == Direction.HORIZONTAL) {
				leftBorder = selectedShipField;
				rightBorder = (short)(Game.BOARD_SIDE - selectedShip.getLength() + selectedShipField);
			}
			else {
				topBorder = selectedShipField;
				bottomBorder = (short)(Game.BOARD_SIDE - selectedShip.getLength() + selectedShipField);
			}
		}
		
		if (x < leftBorder) x = leftBorder;
		if (x > rightBorder) x = rightBorder;
		if (y <topBorder) y = topBorder;
		if (y > bottomBorder) y = bottomBorder;
		
		if(y*Game.BOARD_SIDE + x < 0) {
			int i = 0;
			i++;
		}

        return (short)(y*Game.BOARD_SIDE + x);
	}
	
	private enum PaintAction {
		PAINT_OVER,
		RESTORE
	};
	
}
