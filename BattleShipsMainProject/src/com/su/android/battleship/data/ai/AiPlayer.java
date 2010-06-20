package com.su.android.battleship.data.ai;

import java.io.Serializable;

import com.su.android.battleship.data.GameAi;
/**
 * Abstract class for the AiPlayers
 * @author vasko
 *
 */
public abstract class AiPlayer implements Serializable {
	
	private static final long serialVersionUID = 304881475627007210L;
	
	/**
	 * the game for the AiPlayer to participate in
	 */
	protected GameAi game;	
	 	
	/**
	 * 
	 * @return the next move that the AiPlayer chooses to make
	 */
	public abstract short generateMove();
	
	/**
	 * Updates the game state that AiPlayer hold after its move.From this state the Ai chooses what to play next 
	 * @param filed
	 * @param newFieldStatus
	 */
	public abstract void updateAfterMove(short filed,short newFieldStatus);
}