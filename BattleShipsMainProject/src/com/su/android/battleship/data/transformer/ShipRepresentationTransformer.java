package com.su.android.battleship.data.transformer;

import com.su.android.battleship.data.Game;
import com.su.android.battleship.data.Ship;
import com.su.data.Direction;
import com.su.data.ShipFieldsHolder;


/**
 * Class that supports transformations between Ship's fields representations
 * @author vasil.konstantinov
 *
 */
public class ShipRepresentationTransformer {
	
	/**
	 * 
	 * @param ship
	 * @return different representation of the ship's fields - the method name says enough
	 */
	public static ShipFieldsHolder getShipFieldsHolderFromShip(Ship ship){
		int len = ship.getLength();
		
		short[] fields = ship.getBoardFields();
		int firstField = getMinField(fields);
		
		Direction d = getShipDirection(fields[0], fields[1]);
		
		ShipFieldsHolder result = new ShipFieldsHolder(len,firstField,d);
		return result;
	}
	
	private static short getMinField(short[] fields){		
		short min = Short.MAX_VALUE;
		for(int i = 0 ; i < fields.length ; i++){
			if(min > fields[i]){
				min = fields[i];
			}
		}
		return min;
	}
	
	private static Direction getShipDirection(short f1,short f2){
		short boardSide = Game.BOARD_SIDE;
		if( (f1-f2) % boardSide == 0){
			return Direction.VERTICAL;
		}else if( (f1 - f2) / boardSide == 0){
			return Direction.HORIZONTAL;
		}else{
			//TODO : impossible case - handle exception
			return null;
		}
	}
	
	
	/**
	 * @param holder
	 * @return different representation of the ship's fields - the method name says enough
	 */
	public static Ship getShipFromShipFieldsHolder(ShipFieldsHolder holder){
		int length = holder.getLength();
		short[] fields = new short[length];
		
		Direction direction = holder.getDirection();
		
		switch (direction) {
		case HORIZONTAL:
			for(int i = 0 ; i < length ; i++){
				fields[i] = (short)(holder.getFirstField() + i);
			}
			break;
		case VERTICAL:
			short boardSide = Game.BOARD_SIDE;
			for(int i = 0 ; i < length ; i++){
				fields[i] = (short)(holder.getFirstField() + i*boardSide);
			}
		default:
			break;
		}
		
		Ship result = new Ship((short)length,fields) ;
		return result;
	}	
}
