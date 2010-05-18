package com.su.android.battleship.data.ai.impl.sppfd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.su.android.battleship.data.BoardFieldStatus;
import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.Ship;
import com.su.android.battleship.data.ai.AiCalculationBoardStatusManager;
import com.su.android.battleship.data.ai.AiPlayer_ShipPositionsPerFieldDependant;
import com.su.generator.rule.FieldForbiddingRule;
import com.su.generator.rule.FieldForbiddingRuleSquareImpl;
/**
 * Insane implementation of the AiPlayer
 * @author vasko
 *
 */
public class AiPlayerInsane extends AiPlayer_ShipPositionsPerFieldDependant {

	/**
	 * 
	 * @param game
	 */
	public AiPlayerInsane(GameAi game) {
		super(game);
	}

	

	@Override
	protected List<Short> getFieldTypesThatExcludePotentialShipPositions() {
		List<Short> resultList = new ArrayList<Short>();

		// this AI knows everything and plays perfectly
		resultList.add(AiCalculationBoardStatusManager.DESTROYED_SHIP);
		resultList.add(AiCalculationBoardStatusManager.EMPTY);
		resultList.add(AiCalculationBoardStatusManager.CANT_BE_SHIP);

		return resultList;
	}

	

	protected void updateAfterAttackedShot(short field, short statusCode) {
		short boardSide = GameAi.BOARD_SIDE;
		// mark that field is attacked
		boardForAiCalculations[field] = AiCalculationBoardStatusManager.SHIP;

		// remember that this is SortedSet - no need to rearrange min/max
		fieldsFromNotYetDestroyedShip.add(field);

		// if this is the first attacked field for this ship - simple update
		if (fieldsFromNotYetDestroyedShip.size() == 1) {// was empty before
			List<Short> l = getCrossNeighboursOfField(field, boardSide);
			for (Short s : l) {
				if (boardForAiCalculations[s] == AiCalculationBoardStatusManager.WATER) {
					fieldsToContinueDestryingWith.add(s);
				}
			}
		} else {			
			fieldsToContinueDestryingWith.clear();

			Iterator<Short> iterator = fieldsFromNotYetDestroyedShip.iterator();
			short min = iterator.next();
			short max = iterator.next();			
			while (iterator.hasNext()) {
				max = iterator.next();
			}
			
			short tempFieldPositon;
			short tempBoardStatus;
			if ((max - min) % boardSide == 0) {// vertical
				if (min / boardSide > 0) {
					tempFieldPositon = (short) (min - boardSide);
					tempBoardStatus = boardForAiCalculations[tempFieldPositon];
					if(!isaFieldStatusForbidden(tempBoardStatus)){
						fieldsToContinueDestryingWith.add(tempFieldPositon);
					}
				}
				if (max / boardSide < boardSide - 1) {
					tempFieldPositon = (short) (max + boardSide);
					tempBoardStatus = boardForAiCalculations[tempFieldPositon];
					if(!isaFieldStatusForbidden(tempBoardStatus)){
						fieldsToContinueDestryingWith.add(tempFieldPositon);
					}					
				}
			} else {// horizontal				
				if (min % boardSide != 0) {
					tempFieldPositon = (short) (min - 1);
					tempBoardStatus = boardForAiCalculations[tempFieldPositon];
					if(!isaFieldStatusForbidden(tempBoardStatus)){
						fieldsToContinueDestryingWith.add(tempFieldPositon);
					}					
				}
				if (max % boardSide != (boardSide-1)) {
					tempFieldPositon = (short) (max + 1);
					tempBoardStatus = boardForAiCalculations[tempFieldPositon];
					if(!isaFieldStatusForbidden(tempBoardStatus)){
						fieldsToContinueDestryingWith.add(tempFieldPositon);
					}					
				}
			}
		}
	}
	
	

	protected void updateAfterDestroyedShot(short field, short statusCode) {
		short shipIndex = BoardFieldStatus.getShipIndex(statusCode);
		Ship ship = super.game.getPlayerShips(GameAi.PLAYER_INDEX)[shipIndex];//get PLAYER's ship!!
		short[] shipBoardFields = ship.getBoardFields();
		short tempPosition;
		// update from attacked to destroyed
		for (int i = 0; i < shipBoardFields.length; i++) {
			tempPosition = shipBoardFields[i];
			boardForAiCalculations[tempPosition] = AiCalculationBoardStatusManager.DESTROYED_SHIP;
		}
		// update forbidden positions to CANT_BE_SHIP
		for (int j = 0; j < shipBoardFields.length; j++) {
			tempPosition = shipBoardFields[j];
			// TODO : get rule from configuration
			FieldForbiddingRule rule = new FieldForbiddingRuleSquareImpl(
					GameAi.BOARD_SIDE);
			List<Integer> list = rule.getForbiddenFields(tempPosition);
			for (Integer _int : list) {
				short fieldStatus = boardForAiCalculations[_int];
				if (fieldStatus != AiCalculationBoardStatusManager.DESTROYED_SHIP
						&& fieldStatus != AiCalculationBoardStatusManager.CANT_BE_SHIP) {
					boardForAiCalculations[_int] = AiCalculationBoardStatusManager.CANT_BE_SHIP;
				}
			}
		}
		// empty the destroy_calculations supporting field Lists

		// TODO : Vasko thinks it is not generically OK to clear - better remove
		// old fields
		fieldsFromNotYetDestroyedShip.clear();
		fieldsToContinueDestryingWith.clear();
	}

	@Override
	protected short chooseFromPossibleShipFields() {
		short result = chooseFromTheTwoPendingFields();
		return result;
	}

	private short chooseFromTheTwoPendingFields() {
		//TODO : implement some sort of UnitTest that fieldsToContinueDestroyingWith has exactly 2 elements in this specific case
		Iterator<Short> _iterator = fieldsToContinueDestryingWith.iterator();
		short min_pending = _iterator.next();
		short max_pending = _iterator.next();
		

		Iterator<Short> iterator = fieldsFromNotYetDestroyedShip.iterator();
		short min_destroyed = iterator.next();
		short max_destroyed = iterator.next();
		
		while (iterator.hasNext()) {
			max_destroyed = iterator.next();
		}
		//TODO : implement some sort of UnitTest that pending and destroyed fields are all in the same direction
		short count_of_ships_through_min_pending = getCountOfShipsThroughTwoFields(min_pending,max_destroyed);
		short count_of_ships_through_max_pending = getCountOfShipsThroughTwoFields(max_pending,min_destroyed);		

		if (count_of_ships_through_max_pending == count_of_ships_through_min_pending) {
			int random = (int) (Math.random() * 2);
			return random == 0 ? min_pending : max_pending;
		} else {
			return count_of_ships_through_max_pending > count_of_ships_through_min_pending ? max_pending : min_pending;
		}
	}
	

}
