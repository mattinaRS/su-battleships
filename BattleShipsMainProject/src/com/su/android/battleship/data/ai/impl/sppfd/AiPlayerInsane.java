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

public class AiPlayerInsane extends AiPlayer_ShipPositionsPerFieldDependant {

	public AiPlayerInsane(GameAi game) {
		super(game);
	}

	@Override
	protected List<Short> getAllPotentialForAttackFields() {
		List<Short> resultList = new ArrayList<Short>();
		for (int i = 0; i < boardForAiCalculations.length; i++) {
			if (boardForAiCalculations[i] == AiCalculationBoardStatusManager.WATER) {
				resultList.add((short) i);
			}
		}
		return resultList;
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

	@Override
	public void updateAfterMove(short field) {
		short statusCode = super.game.getPlayerBoard(GameAi.PLAYER_INDEX)[field];
		if (BoardFieldStatus.WATER_ATTACKED == statusCode) {
			updateAfterEmptyShot(field, statusCode);
		} else if (BoardFieldStatus.isShipAttackedStatus(statusCode)) {
			updateAfterAttackedShot(field, statusCode);
		} else if (BoardFieldStatus.isShipDestroyedStatus(statusCode)) {
			updateAfterDestroyedShot(field, statusCode);
		}
	}

	private void updateAfterEmptyShot(short field, short statusCode) {
		boardForAiCalculations[field] = AiCalculationBoardStatusManager.EMPTY;
		if(!fieldsFromNotYetDestroyedShip.isEmpty() && fieldsFromNotYetDestroyedShip.contains(field)){
			fieldsFromNotYetDestroyedShip.remove(field);
		}
		
	}

	private void updateAfterAttackedShot(short field, short statusCode) {
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
			if ((max - min) % boardSide == 0) {// vertical
				if (min / boardSide > 0) {
					fieldsToContinueDestryingWith.add((short) (min - boardSide));
				}
				if (max / boardSide < boardSide - 1) {
					fieldsToContinueDestryingWith.add((short) (max + boardSide));
				}
			} else {// horizontal
				if (min % boardSide != 0) {
					fieldsToContinueDestryingWith.add((short) (min - 1));
				}
				if (max % boardSide != 9) {
					fieldsToContinueDestryingWith.add((short) (max + 1));
				}
			}
		}
	}

	private void updateAfterDestroyedShot(short field, short statusCode) {
		short shipIndex = BoardFieldStatus.getShipIndex(statusCode);
		Ship ship = super.game.getPlayerShips(GameAi.AI_INDEX)[shipIndex];
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

	// TODO : this method should be in Util - perhaps it already exist but Vasko
	// is too tired to think about it
	private List<Short> getCrossNeighboursOfField(short field, short boardSide) {
		List<Short> result = new ArrayList<Short>();
		if (field % boardSide != 0) {
			result.add((short) (field - 1));
		}
		if (field % boardSide != 9) {
			result.add((short) (field + 1));
		}
		if (field / boardSide > 0) {
			result.add((short) (field - boardSide));
		}
		if (field / boardSide < boardSide - 1) {
			result.add((short) (field + boardSide));
		}

		return result;
	}

}
