package com.su.android.battleship.data.ai.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.su.android.battleship.data.Game;
import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.ai.AiPlayer;

/**
 * Random implementation of AiPlayer
 * @author vasko
 *
 */
public class AiRandomPlayer extends AiPlayer {
	
	private static final long serialVersionUID = 2120471770922165021L;
	
	private List<Short> possibleShots;	
	
	/**
	 * 
	 * @param game
	 */
	public AiRandomPlayer(GameAi game) {
		super.game = game;
		possibleShots = new ArrayList<Short>(Game.BOARD_SIZE);
		for(short i = 0 ; i < Game.BOARD_SIZE ; i++){
			possibleShots.add(i);
		}		
	}
	
	@Override
	public short generateMove() {		
		short possibleShotsCount = (short) possibleShots.size();
		
		Calendar calendar = Calendar.getInstance();
		long randomNumber = calendar.getTimeInMillis();

		Random rand = new Random(randomNumber);
		//TODO : handle exception if all possible moves are made and there is yet another
		// generateMove method call - this will throw AritmethicException - devide by zero
		int randomIndex = rand.nextInt() % possibleShotsCount;
		if (randomIndex < 0) {
			randomIndex += possibleShotsCount;
		}
		return possibleShots.remove(randomIndex);
	}

	@Override
	public void updateAfterMove(short filed,short newFieldStatus) {
		//nothing should be done here - this bot plays random every time without saving any game state
	}
}
