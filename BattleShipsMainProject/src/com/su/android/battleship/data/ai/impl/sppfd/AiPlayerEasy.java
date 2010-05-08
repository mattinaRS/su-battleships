package com.su.android.battleship.data.ai.impl.sppfd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.su.android.battleship.data.BoardFieldStatus;
import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.Ship;
import com.su.android.battleship.data.ai.AiCalculationBoardStatusManager;
import com.su.android.battleship.data.ai.AiPlayer_ShipPositionsPerFieldDependant;

public class AiPlayerEasy extends AiPlayer_ShipPositionsPerFieldDependant {

	public AiPlayerEasy(GameAi game) {
		super(game);
	}

	

	@Override
	protected List<Short> getFieldTypesThatExcludePotentialShipPositions() {
		List<Short> resultList = new ArrayList<Short>();

		// this is easy AI - it know only that a ship can not move through
		// another ship
		// but it does not know , that when a field is empty there is no ship.It
		// also does not know anything about
		// the "CANT_BE_SHIP" fields - that is for example - it plays as if 2
		// ships can touch each other.
		resultList.add(AiCalculationBoardStatusManager.DESTROYED_SHIP);

		return resultList;
	}

	
	
	
	@Override
	/**
	 * The Easy AI will choose randomly from the stacked possible fields , with nothing learned from the 
	 * made attempts - if ship's direction can be determined from 2 hits , the EasyAI will not know it
	 */
	protected short chooseFromPossibleShipFields() {
		short result;				
		
		result = fieldsToContinueDestryingWith.iterator().next();
		fieldsToContinueDestryingWith.remove(result);
		return result;
	}

	
	
	
	/**
	 * The implementation is as follows:
	 * From the selected field are obtained the neighbor fields
	 * From them a random number is added in  in the search set.This way , the EasyAI can 
	 * go in a seek mode before destroying a ship - which is desired dummy behavior.
	 * @param field
	 * @param statusCode
	 */
	protected void updateAfterAttackedShot(short field, short statusCode) {
		short boardSide = GameAi.BOARD_SIDE;
		// mark that field is attacked
		boardForAiCalculations[field] = AiCalculationBoardStatusManager.SHIP;

		
		fieldsFromNotYetDestroyedShip.add(field);
		fieldsToContinueDestryingWith.remove(field);
		
		List<Short> l = getCrossNeighboursOfField(field, boardSide);
		short s;
		for(int i = 0 ; i < l.size() ; i++){
			if(i%2 == 0){
				s=l.get(i);
				if (boardForAiCalculations[s] == AiCalculationBoardStatusManager.WATER) {
					fieldsToContinueDestryingWith.add(s);
				}
			}
		}
	}

	
	protected void updateAfterDestroyedShot(short field,short newFieldStatusCode){
		short shipIndex = BoardFieldStatus.getShipIndex(newFieldStatusCode);
		Ship ship = super.game.getPlayerShips(GameAi.PLAYER_INDEX)[shipIndex];
		short[] shipBoardFields = ship.getBoardFields();
		short tempPosition;
		for (int i = 0; i < shipBoardFields.length; i++) {
			tempPosition = shipBoardFields[i];
			boardForAiCalculations[tempPosition] = AiCalculationBoardStatusManager.DESTROYED_SHIP;
		}
		fieldsFromNotYetDestroyedShip.clear();
		fieldsToContinueDestryingWith.clear();
	}
	

}
