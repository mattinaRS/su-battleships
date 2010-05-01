package com.su.android.battleship.data.ai;

import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.Ship;

public abstract class AiPlayer {
	protected GameAi game;	
	 	
	public abstract short generateMove();
}