package com.su.android.battleship.data.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.su.android.battleship.data.BoardFieldStatus;
import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.Ship;
import com.su.android.battleship.data.transformer.ShipRepresentationTransformer;
import com.su.android.battleship.util.CommonUtil;
import com.su.data.ShipFieldsHolder;
import com.su.manager.ShipPositionsContainingFieldManager;

public abstract class AiPlayer_ShipPositionsPerFieldDependant extends AiPlayer {

	protected short[] boardForAiCalculations;
	protected SortedSet<Short> fieldsFromNotYetDestroyedShip = new TreeSet<Short>();
	protected SortedSet<Short> fieldsToContinueDestryingWith = new TreeSet<Short>();

	private ShipPositionsContainingFieldManager shipPositionManager = new ShipPositionsContainingFieldManager(
			game.BOARD_SIDE);

	public AiPlayer_ShipPositionsPerFieldDependant(GameAi game) {
		this.game = game;

		Short[] realBoard = game.getPlayerBoard(GameAi.PLAYER_INDEX);
		boardForAiCalculations = new short[realBoard.length];
		for (int i = 0; i < realBoard.length; i++) {
			boardForAiCalculations[i] = AiCalculationBoardStatusManager.WATER;
		}

	}
	
	


	// fieldPotentialForAttack will be decreased after the update
	/**
	 * Subclasses of this AiPlayer must implement a way to return all fields
	 * that are potentially hiding a ship The more cleaver the AiPlayer is , the
	 * shorter List<Short> this method will return.
	 * 
	 * @return List with all potentially hiding ship fields
	 */
	protected final List<Short> getAllPotentialForAttackFields(){
		List<Short> resultList = new ArrayList<Short>();
		for (int i = 0; i < boardForAiCalculations.length; i++) {
			if (boardForAiCalculations[i] == AiCalculationBoardStatusManager.WATER) {
				resultList.add((short) i);
			}
		}
		return resultList;
	}

	/**
	 * AI differ from each other in the way they determine how many ships can
	 * pass through a specific field Obstacles for a ship can be different types
	 * of fields - for example attacked_field,destroyed_ships_field
	 * ,cant_be_ship_field.The more cleaver the AiPlayer is , the more detailed
	 * list this method will return
	 * 
	 * @return
	 */
	protected abstract List<Short> getFieldTypesThatExcludePotentialShipPositions();

	/**
	 * Different AI hold different state for the fields from which the pick to shoot
	 * in the destroy mode.Implementation of this method provides the difference in their logic
	 * @return
	 */
	protected abstract short chooseFromPossibleShipFields();
	
	
	/**
	 * Implements how the ai state will be updated after an "attacked ship" shot
	 * @param field
	 * @param statusCode
	 */
	protected abstract void updateAfterAttackedShot(short field,short statusCode);
	
	
	/**
	 * Implements how the ai state will be updated after a "destroyed ship" shot
	 * @param field
	 * @param statusCode
	 */
	protected abstract void updateAfterDestroyedShot(short field,short statusCode);
	
	
	@Override
	/**
	 * This implementation of generateMove depends on the count of possible ship positions passing through every
	 * potential field.From the fields with the biggest such count , randomly will be selected one , which gives
	 * the best chance to hit a ship.
	 */
	public final short generateMove() {
		short result;
		//second condition is very important for EasyAi algorythm - it gives the ai the opportunity to return to seek mode before it destroys the current ship
		if (fieldsFromNotYetDestroyedShip.isEmpty() || fieldsToContinueDestryingWith.isEmpty()) {
			result = seek();
		} else {
			result = destroy();
		}
		return result;
	}

	/**
	 * This method MUST be called after a generated move is chosen for a ai
	 * actual move - right after updating game This way the
	 * boardForAiCalculations will be always synchronized with the actual game
	 * situations and ai will generate next moves correctly
	 * 
	 * @param field - the chosen game move.
	 * @param newFieldSatus - the newStatus of the attacked field which is returned from the game update
	 */
	public void updateFieldAfterMove(short field,short newFieldStatus) {
		updateAfterMove(field,newFieldStatus);
	}
	
	@Override
	public void updateAfterMove(short field, short newFieldStatusCode) {
		if (BoardFieldStatus.WATER_ATTACKED == newFieldStatusCode) {
			updateAfterEmptyShot(field, newFieldStatusCode);
		} else if (BoardFieldStatus.isShipAttackedStatus(newFieldStatusCode)) {
			updateAfterAttackedShot(field, newFieldStatusCode);
		} else if (BoardFieldStatus.isShipDestroyedStatus(newFieldStatusCode)) {
			updateAfterDestroyedShot(field, newFieldStatusCode);
		}
	}
	
	

	private short seek() {
		Map<Short, Short> bestFieldsToChooseFrom = new HashMap<Short, Short>();

		// first obtain all "free for attack" fields
		List<Short> freeForAttackFields = getAllPotentialForAttackFields();// this
		// is
		// polymorphic
		// call

		Map<Short, Short> shipsMap = game.getShipsMapRepresentation();
		Set<Short> shipsLengthSet = shipsMap.keySet();

		List<ShipFieldsHolder> tempSfhList;

		for (Short field : freeForAttackFields) {
			// this property holds the sum of the possible shipPositions for
			// every ship in the game that has not yet been found
			short possibleShipsCountFromAllGameShips = 0;
			for (Short shipLength : shipsMap.keySet()) {
				tempSfhList = shipPositionManager.getShipsCrossingField(field,shipLength);
				short possibileShipsCountForGivenShipLength = getCountOfOnlyPotentialShips(tempSfhList);

				// System.out.println("For field: " + field
				// + " passing ships with length: " + shipLength
				// + " are: " + possibileShipsCountForGivenShipLength);

				short gameShipsCountForShipLength = shipsMap.get(shipLength);
				possibleShipsCountFromAllGameShips += possibileShipsCountForGivenShipLength
						* gameShipsCountForShipLength;

			}
			// System.out.println("For field: " + field
			// + " all passing ships are OVERALL: " +
			// possibleShipsCountFromAllGameShips);

			// updates the besFieldsToChooseFrom map every time with the biggest
			// possibleShipsPosition values for a board field.
			// This way, in the end the map will contain the best fields to
			// shoot at
			if (bestFieldsToChooseFrom.isEmpty()) {
				bestFieldsToChooseFrom.put(field,possibleShipsCountFromAllGameShips);
			} else {
				short firstKey = bestFieldsToChooseFrom.keySet().iterator().next();				
				if (bestFieldsToChooseFrom.get(firstKey) <= possibleShipsCountFromAllGameShips) {
					if (bestFieldsToChooseFrom.get(firstKey) < possibleShipsCountFromAllGameShips) {
						bestFieldsToChooseFrom.clear();
					}
					bestFieldsToChooseFrom.put(field,possibleShipsCountFromAllGameShips);
				}
			}
		}
		List<Short> keySetList = new ArrayList<Short>(bestFieldsToChooseFrom
				.keySet());
		short modul = (short) keySetList.size();
		short randomNumber = CommonUtil.getRandomIndex(modul);
		short resultField = keySetList.get(randomNumber);

		return resultField;
	}

	private short destroy() {
		short result = -1;
		if (fieldsToContinueDestryingWith.isEmpty()) {
			//impossible case
		} else {
			if (fieldsFromNotYetDestroyedShip.size() == 1) {// ship's direction is not determined yet
				short theOnlyDestroyedField = fieldsFromNotYetDestroyedShip.iterator().next();
				short max_count = Short.MIN_VALUE;
				for(Short field : fieldsToContinueDestryingWith){					
					short current_max = getCountOfShipsThroughTwoFields(theOnlyDestroyedField,field);
					//TODO : implement random logic if there are more than one equal max_counts
					if(current_max > max_count){
						max_count = current_max;
						result = field;
					}
				}
			} else {// ship's direction can be determined
				if (fieldsToContinueDestryingWith.size() == 1) {
					result = fieldsToContinueDestryingWith.iterator().next();
				} else {// there are 2 (min and max) possible fields to continue with
					result = chooseFromPossibleShipFields();
				}
			}
		}
		return result;
	}
	
	protected short getCountOfShipsThroughTwoFields(short f1,short f2){
		short resultCount = 0;
		
		Map<Short, Short> shipsMap = game.getShipsMapRepresentation();
		Set<Short> shipsLengthSet = shipsMap.keySet();
		
		List<ShipFieldsHolder> tempSFH;
		for (Short shipLength : shipsMap.keySet()) {
			tempSFH = getShipsThroughTwoFields(f1,f2, shipLength);
			resultCount += getCountOfOnlyPotentialShips(tempSFH)*shipsMap.get(shipLength);
		}
		
		return resultCount;
	}	

	/**
	 * Common method for AI algorithms implementation
	 * @param field1
	 * @param field2
	 * @param shipLength
	 * @return the ships through 2 fields
	 */
	protected List<ShipFieldsHolder> getShipsThroughTwoFields(short field1,short field2, short shipLength) {
		List<ShipFieldsHolder> resultSHFList = new ArrayList<ShipFieldsHolder>();

		List<ShipFieldsHolder> tempSfhList;

		List<Short> tempFieldsList;

		tempSfhList = shipPositionManager.getShipsCrossingField(field1,
				shipLength);

		for (ShipFieldsHolder sfh : tempSfhList) {
			tempFieldsList = ShipFieldsHolder.getShipFields(sfh,
					game.BOARD_SIDE);
			if (tempFieldsList.contains(field2)) {
				resultSHFList.add(sfh);
			}
		}
		return resultSHFList;
	}

	/**
	 * Common method for AI algorithms implementation
	 * @param allShips
	 * @return the count of only potential ships - that is - the impossible ships are removed from the result number
	 */
	protected short getCountOfOnlyPotentialShips(List<ShipFieldsHolder> allShips) {
		short result = (short) allShips.size();
		Ship tempShip;
		short[] tempBoardFields;
		List<Short> fieldStatusesThatDenyShips = getFieldTypesThatExcludePotentialShipPositions();

		for (ShipFieldsHolder shipFieldsHolder : allShips) {
			tempShip = ShipRepresentationTransformer
					.getShipFromShipFieldsHolder(shipFieldsHolder);
			tempBoardFields = tempShip.getBoardFields();

			short boardFieldIndex;
			short boardFieldStatus;
			for (int i = 0; i < tempBoardFields.length; i++) {
				boardFieldIndex = tempBoardFields[i];
				// boardFieldStatus =
				// game.getPlayerBoard(GameAi.AI_INDEX)[boardFieldIndex];
				boardFieldStatus = boardForAiCalculations[boardFieldIndex];
				if (fieldStatusesThatDenyShips.contains(boardFieldStatus)) {
					// this means that this possibleShipPosition contains a
					// denying fieldStatus - this ship position is impossible
					result--;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param fieldStatus
	 * @return whether or not a field is in the AI's list of types that exclude potential ship positions
	 */
	public boolean isaFieldStatusForbidden(short fieldStatus){
		boolean result = getFieldTypesThatExcludePotentialShipPositions().contains(fieldStatus);
		return result;
	}
	
	/**
	 * 
	 * @param field
	 * @param boardSide
	 * @return The neighbour fields of the specified board field
	 */
	public List<Short> getCrossNeighboursOfField(short field, short boardSide) {
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
	
	/**
	 * This method is common for all the ai bots so it is implemented in parent abstract class
	 * @param field
	 * @param statusCode
	 */
	protected void updateAfterEmptyShot(short field, short statusCode) {
		boardForAiCalculations[field] = AiCalculationBoardStatusManager.EMPTY;
		if(fieldsToContinueDestryingWith.contains(field)){
			fieldsToContinueDestryingWith.remove(field);
		}		
	}
}
