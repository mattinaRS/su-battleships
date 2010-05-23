package com.su.android.battleship.data.ai;

import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.ai.impl.AiRandomPlayer;
import com.su.android.battleship.data.ai.impl.sppfd.AiPlayerEasy;
import com.su.android.battleship.data.ai.impl.sppfd.AiPlayerInsane;
import com.su.android.battleship.data.ai.impl.sppfd.AiPlayerNormal;

/**
 * Factory that creates AI objects
 * @author vasko
 *
 */
public class AiFactory {
	
	
	private static final short AI_EASY = 0;
	private static final short AI_NORMAL = 1;
	private static final short AI_HARD = 2;	
	private static final short AI_INSANE = 3;
	
	private AiFactory() {
	}
	
	private static AiFactory instance = new AiFactory();
	
	/**
	 * 
	 * @return the single instance of the Factory
	 */
	public static AiFactory getInstance(){
		return instance;
	}
	
	/**
	 * Produces ai player from:
	 * @param type - ai player type
	 * @param game - game in which the ai will participate
	 * @return the created AiPlayer instance
	 */
	public AiPlayer produceAiPlayer(int type,GameAi game){
		switch (type) {
		case AI_EASY:
			return new AiRandomPlayer(game);
		case AI_NORMAL:
			return new AiPlayerEasy(game);
		case AI_HARD:
			return new AiPlayerNormal(game);
		case AI_INSANE:
			return new AiPlayerInsane(game);
		default:
			return null;			
		}		
	}	
}