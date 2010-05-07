package com.su.android.battleship.data;

import java.io.Serializable;

/**
 * This class stores the length of a game ship and indexes of the fields on which it resides
 * on the game board.It also sustain state of destruction for every ship
 * @author vasil.konstantinov
 *
 */
public class Ship implements Serializable {
	
	//TODO : implement logic that stores state of the Ship's attacked field in order
	//a ship to be able to return its "Life" status.
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Short[] board;
	
	private short length;
	private short hitCounts = 0;
	
	/*stores indexes of boardFields*/
	private short[] boardFields;
	
	/**
	 * @return - the length of the ship
	 */
	public short getLength(){
		return this.length;
	}
	
	/**
	 * @return the boardFields
	 */
	public short[] getBoardFields() {
		return boardFields;
	}
	
	public Ship(short length,short[] _fields) {
		this.length = length;
		//TODO : check for correctness of _fields length
		this.boardFields = _fields;
	}
	/**
	 * 
	 * @param attackedField - the attacked field 
	 * @return weather ship was hit or missed
	 */
	public boolean updateShipState(short attackedField){
		for(int i = 0 ; i < boardFields.length ; i++){
			if(boardFields[i] == attackedField){
				hitCounts++;				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return whether or not all ship fields are attacked
	 */
	public boolean isShipDestroyed(){
		return this.length == hitCounts;
	}
	
	/**
	 * @return the number of attacked fields of the ship
	 */
	public short getShipHitStatus(){
		return hitCounts;
	}
}
