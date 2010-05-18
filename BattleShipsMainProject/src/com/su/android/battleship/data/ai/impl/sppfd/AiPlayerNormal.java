package com.su.android.battleship.data.ai.impl.sppfd;

import java.util.ArrayList;
import java.util.List;

import com.su.android.battleship.data.BoardFieldStatus;
import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.Ship;
import com.su.android.battleship.data.ai.AiCalculationBoardStatusManager;
import com.su.android.battleship.data.ai.AiPlayer_ShipPositionsPerFieldDependant;

/**
 * Normal implementation of the AiPlayer
 * @author vasko
 *
 */
public class AiPlayerNormal extends AiPlayer_ShipPositionsPerFieldDependant {

	/**
	 * 
	 * @param game
	 */
	public AiPlayerNormal(GameAi game) {
		super(game);
	}

	

	@Override
	protected List<Short> getFieldTypesThatExcludePotentialShipPositions() {
		List<Short> resultList = new ArrayList<Short>();

		// this is normal AI - it know that a ship can not move through empty
		// field
		// but it does not know , that a ship can not run through another ship -
		// not very sensible , but this is a smaller flaw in the algorithm than
		// in the easy AI
		// as often as for every other field
		resultList.add(AiCalculationBoardStatusManager.DESTROYED_SHIP);
		resultList.add(AiCalculationBoardStatusManager.EMPTY);

		return resultList;
	}

	@Override
	protected short chooseFromPossibleShipFields() {
		short result;
		//NOTE : implementation can be made so that normal to choose randomly from fieldsToContinueDestryingWith array
		//		int size = fieldsToContinueDestryingWith.size();		

		result = fieldsToContinueDestryingWith.iterator().next();
		fieldsToContinueDestryingWith.remove(result);
		return result;
	}

	/**
	 * Normal AI adds all the possible ship fields in the fieldSet - so it behaves better that Easy AI - it 
	 * always finished the destroyed process.However it is not optimized as in the Insane AI variant to reduce
	 * the possible fields with knowledge from the made shots
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
		for (Short s : l) {
			if (boardForAiCalculations[s] == AiCalculationBoardStatusManager.WATER) {
				fieldsToContinueDestryingWith.add(s);
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
		
		//this AI should clean fieldSets , because it always finishes its destroy state with a destroyed ship
		fieldsFromNotYetDestroyedShip.clear();
		fieldsToContinueDestryingWith.clear();
	}

}
