package com.su.android.battleship.data.ai;

import com.su.android.battleship.data.GameAi;

public abstract class AiPlayer {
	protected GameAi game;	
	 	
	public abstract short generateMove();
	
	public abstract void updateAfterMove(short filed,short newFieldStatus);
}