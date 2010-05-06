package com.su.android.battleship.ui.tutorials;

import com.su.android.battleship.R;
import com.su.android.battleship.ui.adapter.MoveShipsAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageView;

public class MoveSingleShipTutorial extends Activity{
	private short shipPosition;
	private boolean shipIsSelected;
	protected MoveShipsAdapter moveShipsAdapter;
	protected GridView boardGrid;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		initGame();
		displayGameScreen();
		attachActionListeners();
	}

	protected void displayGameScreen(){
		setContentView(R.layout.move_single_ship);		
		
		boardGrid = (GridView) findViewById(R.id.GridViewMoveShips);
		
		short[] shipFields = {shipPosition};
		moveShipsAdapter = new MoveShipsAdapter(this, shipFields);	
		boardGrid.setAdapter(moveShipsAdapter);
	}

	protected void attachActionListeners(){
        boardGrid.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
		        short x, y, position;
		        ImageView iv;
		        //Vasko finds the numbers {4,4.5,27,28,31} "very well" and "obviously" co-relative 
		        //TODO : board imageViews' sizes will be hard-coded as @values and the coordinate formulas should depend only
		        //on those hard-coded values , if UI size properties are to be loosely coupled with the the coordinates business logic
		        //otherwise changes in the UI size properties will reflect on and induce changes in the formulas.
		        x = (short)((event.getX()-4)/31);
				if (x>9) x = 9;
				y = (short)((event.getY()-4.5)/28);
				if (y>9) y = 9;

		        position = (short)(y*10+x);
		        iv = (ImageView) moveShipsAdapter.getItem(position);
		        
//		        Toast.makeText(MoveSingleShipTutorial.this, "x: " + event.getX() +", y:" + event.getY(), 500).show();
		        
		        switch (event.getAction()) {
		        case MotionEvent.ACTION_DOWN:
		        	if(position == shipPosition) {
		        		shipIsSelected = true;
		        		((ImageView) iv).setImageResource(R.drawable.yellow);
		        	}
		            break;
		        case MotionEvent.ACTION_MOVE:
		        	if(shipIsSelected) {
		        		if(position!=shipPosition) {
		        			((ImageView)moveShipsAdapter.getItem(shipPosition)).setImageResource(R.drawable.blue);
		        			shipPosition = position;
		        			((ImageView) iv).setImageResource(R.drawable.yellow);
		        		}
		        	}
		            break;
		        case MotionEvent.ACTION_UP:
		        	if(shipIsSelected) {
		        		((ImageView) iv).setImageResource(R.drawable.green);
		        		shipIsSelected = false;
		        	}
		            break;
		        }
		        return true;
			}
		});

	}
	protected void initGame(){
		shipPosition = 2;
		shipIsSelected = false;
	}

}
