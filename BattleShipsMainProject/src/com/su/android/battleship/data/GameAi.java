package com.su.android.battleship.data;

import com.su.android.battleship.data.ai.AiFactory;
import com.su.android.battleship.data.ai.iface.AiPlayer;


public class GameAi extends Game{	
	private static final short PLAYER_INDEX = 0;
	private static final short AI_INDEX = 1;
	
	private AiPlayer bot;
	
	/**
	 * The call to this constructor should be made after AiPlayer bot is instantiated
	 * because the bot is supposed to generate secondPlayerShips configuration,which has to
	 * be passed in this constructor. 
	 * @param playerOnMove - which player has the first move
	 * @param firstPlayerShips - first player's ships configuration
	 * @param secondPlayerShips - second player's (which is ai) ships configuration
	 * @param bot - the bot that will play as second player and that was used to generate and pass second player's ship configuration
	 */
	public GameAi(short playerOnMove,Ship[] firstPlayerShips,Ship[] secondPlayerShips,AiPlayer bot) {		
		super(playerOnMove,firstPlayerShips,secondPlayerShips);
//		bot = AiFactory.getInstance().produceAiPlayer(0,this);
		this.bot = bot;
	}

	/**
	 * TODO : Should be deprecated and BoardFieldStatus short number should be returned instead
	 * @return AiMoveResponse that holds the moveField and a flag whether there was a ship ti hit
	 */
	public AiMoveResponse makeMoveForAi(){
		short field = bot.generateMove();
		
		short newFieldStatus = super.executeMove(AI_INDEX, field);
		boolean isItaHit = BoardFieldStatus.isShipAttackedStatus(newFieldStatus);
		if(isItaHit){
			int shipIndex = BoardFieldStatus.getShipIndex(newFieldStatus);
			Ship shipToUpdate = getPlayerShips(PLAYER_INDEX)[shipIndex];
			shipToUpdate.updateShipState(field);
		}
		
		return new AiMoveResponse(field,isItaHit);
		
	}	
	
	public class AiMoveResponse{
		private int moveField;
		private boolean isItaHit;
		
		public AiMoveResponse(int m,boolean h) {
			this.moveField = m;
			this.isItaHit = h;
		}

		/**
		 * @return the moveField
		 */
		public int getMoveField() {
			return moveField;
		}

		/**
		 * @return the isItaHit
		 */
		public boolean isItaHit() {
			return isItaHit;
		}
	}

}
