package com.su.android.battleship.data.ai;

import java.util.Calendar;
import java.util.Random;

import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.ai.impl.AiRandomPlayer;

import android.util.Log;

public class AiFactory {
	
	public static final short AI_RANDOM_PLAYER = 0;
	
	private static AiFactory instance = new AiFactory();
	
	public static AiFactory getInstance(){
		return instance;
	}
	
	//TODO : change aiType from int to enum
	public AiPlayer produceAiPlayer(int type,GameAi game){
		switch (type) {
		case 0:
			return new AiRandomPlayer(game);			
		default:
			return null;			
		}		
	}	
}