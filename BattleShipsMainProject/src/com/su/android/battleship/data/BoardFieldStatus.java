package com.su.android.battleship.data;

/**
 * This class holds the values of the game board fields and business methods to manipulate them
 * @author vasko
 *
 */
public class BoardFieldStatus {
	
	private BoardFieldStatus(){		
	}
	
	/**
	 * Code for a WATER FIELD
	 */
	public static final short WATER = 0x0F;
	
	/**
	 * Code for a ATTACKE WATER FIELD
	 */
	public static final short WATER_ATTACKED = 0x1F;
	
	/*Ship index is corresponding to the length of the ship 
	 *The lower hex byte - the position in the Ship[] array*/
	private static final short SHIP_0 = 0x00;
	private static final short SHIP_0_ATTACKED = 0x10;
	@SuppressWarnings("unused")
	private static final short SHIP_0_DESTROYED = 0x20;
	
	private static final short SHIP_1 = 0x01;
	private static final short SHIP_1_ATTACKED = 0x11;
	@SuppressWarnings("unused")
	private static final short SHIP_1_DESTROYED = 0x21;
	
	private static final short SHIP_2 = 0x02;
	private static final short SHIP_2_ATTACKED = 0x12;
	@SuppressWarnings("unused")
	private static final short SHIP_2_DESTROYED = 0x22;
	
	private static final short SHIP_3 = 0x03;
	private static final short SHIP_3_ATTACKED = 0x13;
	@SuppressWarnings("unused")
	private static final short SHIP_3_DESTROYED = 0x23;
	
	private static final short SHIP_4 = 0x04;
	private static final short SHIP_4_ATTACKED = 0x14;
	@SuppressWarnings("unused")
	private static final short SHIP_4_DESTROYED = 0x24;
	
	/**
	 * 
	 * @param notAttackedFieldCode
	 * @return corresponding attackedFieldCode
	 */
	public static short getAttackedFieldCode(short notAttackedFieldCode){
		return (short) (notAttackedFieldCode + 0x10);
	}
	
	/**
	 * 
	 * @param shipIndex
	 * @return the shipMarkerCode that is hold as a value in this manager
	 */
	public static short getShipMarkerCode(short shipIndex){
		short boardMarker;
		switch (shipIndex) {
		case 0:
			boardMarker = BoardFieldStatus.SHIP_0;
			break;
		case 1:
			boardMarker = BoardFieldStatus.SHIP_1;
			break;
		case 2:
			boardMarker = BoardFieldStatus.SHIP_2;
			break;
		case 3:
			boardMarker = BoardFieldStatus.SHIP_3;
			break;
		case 4:
			boardMarker = BoardFieldStatus.SHIP_4;
			break;

		default:
			boardMarker = -1;
			break;
		}
		return boardMarker;		
	}
	
	/**
	 * 
	 * @param index
	 * @return the attacked ship marker value
	 */
	public static short getDestroyedShipCode(short index){
		return (short) (index + 0x20);
	}
	
	/**
	 * TODO : recreate this method to use binary operators & and |
	 * @param code
	 * @return whether code is a ship that IS NOT attacked
	 */
	public static boolean isShipNotAttackedStatus(short code){
		return (code == SHIP_0) || (code == SHIP_1) ||(code == SHIP_2)
				|| (code == SHIP_3) || (code == SHIP_4);
	}
	
	/**
	 * TODO : recreate this method to use binary operators & and |
	 * @param code
	 * @return whether code is a ship that IS attacked
	 */
	public static boolean isShipAttackedStatus(short code){
		return (code == SHIP_0_ATTACKED) || (code == SHIP_1_ATTACKED)||
		(code == SHIP_2_ATTACKED)|| (code == SHIP_3_ATTACKED)|| (code == SHIP_4_ATTACKED);
	}
	
	/**
	 * 
	 * @param num - the shipCode - not_attacked , attacked or destroyed
	 * @return - returns ship index - lower byte of the shipCode corresponds to the ship's game number
	 */
	public static short getShipIndex(short num){
		if(!isShipNotAttackedStatus(num) && !isShipAttackedStatus(num) && !isShipDestroyedStatus(num)){
			//TODO : throw custom exception - can't get shipIndex from no-ship status
			return -1;
		}else{			
			int shipIndex = num & 0x0F;
			return (short) shipIndex;
		}
	}
	
	/**
	 * 
	 * @param code
	 * @return whether or not the field is attacked
	 */
	public static boolean isAttackedFieldStatus(short code){
		return (code & 0xF0) == 0x10;
	}
	
	/**
	 * 
	 * @param code
	 * @return whether or not this is a code of a destroyed ship
	 */
	public static boolean isShipDestroyedStatus(short code){
		return (code & 0xF0) == 0x20;
	}
}
