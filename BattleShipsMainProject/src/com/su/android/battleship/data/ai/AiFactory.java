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
	
	private static final short AI_RANDOM_PLAYER = 0;
	private static final short AI_SPPFD_EASY = 1;
	private static final short AI_SPPFD_NORMAL = 2;
	private static final short AI_SPPFD_INSANE = 3;
	
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
		case AI_RANDOM_PLAYER:
			return new AiRandomPlayer(game);
		case AI_SPPFD_EASY:
			return new AiPlayerEasy(game);
		case AI_SPPFD_NORMAL:
			return new AiPlayerNormal(game);
		case AI_SPPFD_INSANE:
			return new AiPlayerInsane(game);
		default:
			return null;			
		}		
	}	
}