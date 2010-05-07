package com.su.android.battleship.data;

public class BoardFieldStatus {
	
	private BoardFieldStatus(){		
	}
	
	public static final short WATER = 0x0F;
	public static final short WATER_ATTACKED = 0x1F;
	
	/*Ship index is corresponding to the length of the ship 
	 *The lower hex byte - the position in the Ship[] array*/
	public static final short SHIP_0 = 0x00;
	public static final short SHIP_0_ATTACKED = 0x10;
	public static final short SHIP_0_DESTROYED = 0x20;
	
	public static final short SHIP_1 = 0x01;
	public static final short SHIP_1_ATTACKED = 0x11;
	public static final short SHIP_1_DESTROYED = 0x21;
	
	public static final short SHIP_2 = 0x02;
	public static final short SHIP_2_ATTACKED = 0x12;
	public static final short SHIP_2_DESTROYED = 0x22;
	
	public static final short SHIP_3 = 0x03;
	public static final short SHIP_3_ATTACKED = 0x13;
	public static final short SHIP_3_DESTROYED = 0x23;
	
	public static final short SHIP_4 = 0x04;
	public static final short SHIP_4_ATTACKED = 0x14;
	public static final short SHIP_4_DESTROYED = 0x24;
	
	public static short getAttackedFieldCode(short notAttackedFieldCode){
		return (short) (notAttackedFieldCode + 0x10);
	}
	
	//TODO : refactor this method to use & operator
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
	
	public static boolean isAttackedFieldStatus(short code){
		return (code & 0xF0) == 0x10;
	}
	
	public static boolean isShipDestroyedStatus(short code){
		return (code & 0xF0) == 0x20;
	}
}
