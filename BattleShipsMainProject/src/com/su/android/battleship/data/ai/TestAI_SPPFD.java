package com.su.android.battleship.data.ai;

import android.util.Log;

import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.Ship;
import com.su.android.battleship.data.ShipPositionGenerator;
/**
 * Test class
 * @author vasko
 *
 */
public class TestAI_SPPFD {	
    /**
     * test method
     */
	public static void test(){
		ShipPositionGenerator generator = new ShipPositionGenerator();
		
		Ship[] generatedShipPosition1 = generator.getRandomShipsPosition();
		Ship[] generatedShipPosition2 = generator.getRandomShipsPosition();

		GameAi game = new GameAi((short) 0, generatedShipPosition1,
				generatedShipPosition2,(short) 3);
		
		AiPlayer_ShipPositionsPerFieldDependant ai = (AiPlayer_ShipPositionsPerFieldDependant) AiFactory.getInstance().produceAiPlayer(3, game);
		short tempNumber;
		boolean isOptimalFirstMove;
		short[] optimalFirstMoves = {44,45,54,55};
		for(int i = 0 ; i < 20 ; i++){
			tempNumber = ai.generateMove();
			isOptimalFirstMove = isInArray(tempNumber, optimalFirstMoves) ;
			Log.d("AI_TEST","generated move is: "+tempNumber+" is in optimal numbers: "+isOptimalFirstMove);
			//NOTE : this test is seek mode only test - not connected with a real game
			ai.updateFieldAfterMove(tempNumber,AiCalculationBoardStatusManager.WATER);
		}
	}
	
	private static boolean isInArray(short number,short[] array){
		for (int i = 0; i < array.length; i++) {
			if(array[i] == number){
				return true;
			}
		}
		return false;
	}
}
