package com.su.android.battleship.data;

import com.su.android.battleship.data.ai.AiFactory;
import com.su.android.battleship.data.ai.AiPlayer;


public class GameAi extends Game{	
	public static final short PLAYER_INDEX = 0;
	public static final short AI_INDEX = 1;
	
	private AiPlayer bot;
	
	/**
	 * The call to this constructor should be made after ShipPositionGenerator is instantiated
	 * because the it is supposed to generate secondPlayerShips configuration,which has to
	 * be passed in this constructor. 
	 * @param playerOnMove - which player has the first move
	 * @param firstPlayerShips - first player's ships configuration
	 * @param secondPlayerShips - second player's (which is ai) ships configuration
	 */
	public GameAi(short playerOnMove,Ship[] firstPlayerShips,Ship[] secondPlayerShips) {		
		super(playerOnMove,firstPlayerShips,secondPlayerShips);
		bot = AiFactory.getInstance().produceAiPlayer(AiFactory.AI_RANDOM_PLAYER,this);		
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
		private short moveField;
		private boolean isItaHit;
		
		public AiMoveResponse(short m,boolean h) {
			this.moveField = m;
			this.isItaHit = h;
		}

		/**
		 * @return the moveField
		 */
		public short getMoveField() {
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
