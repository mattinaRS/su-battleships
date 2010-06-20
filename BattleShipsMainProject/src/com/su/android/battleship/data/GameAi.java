package com.su.android.battleship.data;

import com.su.android.battleship.data.ai.AiFactory;
import com.su.android.battleship.data.ai.AiPlayer;

/**
 * @author vasko
 * This class extends the Game functionality and also provides business methods to play the game with an Ai opponent
 */
public class GameAi extends Game {
	
	private static final long serialVersionUID = 5640927081793011987L;

	/**index of the PLAYER used in the storing of boards and ships in the GameAi*/
	public static final short PLAYER_INDEX = 0;
	
	/**index of the AI used in the storing of boards and ships in the GameAi*/
	public static final short AI_INDEX = 1;

	private short gameDifficulty;

	private AiPlayer bot;

	/**
	 * @return the game difficulty
	 */
	public short getGameDifficulty() {
		return gameDifficulty;
	}

	/**
	 * @param gameDifficulty
	 */
	public void setGameDifficulty(short gameDifficulty) {
		this.gameDifficulty = gameDifficulty;
	}

	/**
	 * The call to this constructor should be made after ShipPositionGenerator
	 * is instantiated because the it is supposed to generate secondPlayerShips
	 * configuration,which has to be passed in this constructor.
	 * 
	 * @param playerOnMove
	 *            - which player has the first move
	 * @param firstPlayerShips
	 *            - first player's ships configuration
	 * @param secondPlayerShips
	 *            - second player's (which is ai) ships configuration
	 * @param gameDifficulty 
	 * 			  - the GameAi is responsible for creating its bot so the game difficulty must be known
	 * so that the bot can be created accordingly
	 *
	 */
	public GameAi(short playerOnMove, Ship[] firstPlayerShips,
			Ship[] secondPlayerShips, short gameDifficulty) {
		super(playerOnMove, firstPlayerShips, secondPlayerShips);
		this.gameDifficulty = gameDifficulty;
		bot = AiFactory.getInstance().produceAiPlayer(gameDifficulty, this);
	}	

	/**
	 * @return the field position chosen by the ai as the next move.The board's
	 *         field is already updated after this method returns and the new
	 *         status of the attacked field can be obtained via the fields
	 *         position that is returned from this method
	 */
	public short makeMoveForAi() {
		short field = bot.generateMove();
		short newFieldStatus = super.executeMove(AI_INDEX, field);
		// executeMove updates the game state.The bot's update method must be
		// called
		bot.updateAfterMove(field, newFieldStatus);

		return field;
	}

//	@Deprecated
//	public class AiMoveResponse {
//		private short moveField;
//		private boolean isItaHit;
//
//		public AiMoveResponse(short m, boolean h) {
//			this.moveField = m;
//			this.isItaHit = h;
//		}
//
//		/**
//		 * @return the moveField
//		 */
//		public short getMoveField() {
//			return moveField;
//		}
//
//		/**
//		 * @return the isItaHit
//		 */
//		public boolean isItaHit() {
//			return isItaHit;
//		}
//	}

}
