package com.su.android.battleship.data.ai;

public class AiCalculationBoardStatusManager {

	private AiCalculationBoardStatusManager() {
	}
	
	public static final short WATER = 0x00;
	public static final short EMPTY = 0x01;
	public static final short SHIP = 0x02;
	public static final short DESTROYED_SHIP = 0x03;
	
}
