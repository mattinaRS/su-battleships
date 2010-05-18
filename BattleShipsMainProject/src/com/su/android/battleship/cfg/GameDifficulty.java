package com.su.android.battleship.cfg;

/**
 * Enum, representing difficulty levels in the game
 * @author Ivaylo Stoykov
 */
public enum GameDifficulty {
	/**
	 * easy level
	 */
	EASY(1),
	
	/**
	 * normal level
	 */
	NORMAL(2),
	
	/**
	 * hard level 
	 */
	HARD(3);
	
	// future use
	private int mInfo;
	
	GameDifficulty(int aInfo) {
		mInfo = aInfo;
	}
	
	/**
	 * @return some info, which could be used in the future
	 */
	public int getInfo() {
		return mInfo;
	}		
}
