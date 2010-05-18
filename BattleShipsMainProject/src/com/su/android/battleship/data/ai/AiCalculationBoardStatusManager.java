package com.su.android.battleship.data.ai;

/**
 * The Easy/Normal/Insane AI implementation use their own board for game state calculations
 * Thus field of this StatusManager class are used accordingly to support AI's state logic
 * @author vasko
 *
 */
public class AiCalculationBoardStatusManager {

	private AiCalculationBoardStatusManager() {
	}
	/**
	 * WATER field
	 */
	public static final short WATER = 0x00;
	
	/**
	 * CANT_BE_SHIP field
	 */
	public static final short CANT_BE_SHIP = 0x01;
	
	/**
	 * EMPTY field - field that was attacked and is found to be empty
	 */
	public static final short EMPTY = 0x10;
	
	/**
	 * SHIP field
	 */
	public static final short SHIP = 0x11;
	
	/**
	 * DESTROYED_SHIP field - field that is already attacked and holds a ship that is destroyed
	 */
	public static final short DESTROYED_SHIP = 0x12;
	
	
		
}
