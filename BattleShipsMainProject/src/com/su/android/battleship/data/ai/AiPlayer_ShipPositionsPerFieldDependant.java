package com.su.android.battleship.data.ai;

import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.Ship;
import com.su.android.battleship.data.impl.AiGameProcessWrapper;
import com.su.android.battleship.data.transformer.ShipRepresentationTransformer;
import com.su.android.battleship.util.CommonUtil;
import com.su.data.ShipFieldsHolder;
import com.su.generator.encoder.ShipPositionEncoder;
import com.su.generator.encoder.ShipPositionEncoderDefaultImpl;
import com.su.generator.randomizer.ExcludeRowRandomizer;
import com.su.manager.ShipPositionsContainingFieldManager;

public abstract class AiPlayer_ShipPositionsPerFieldDependant extends AiPlayer {
	
	protected short[] boardForAiCalculations;
	
	private ShipPositionsContainingFieldManager shipPositionManager = new ShipPositionsContainingFieldManager(game.BOARD_SIDE);	
	
	public AiPlayer_ShipPositionsPerFieldDependant(GameAi game) {
		this.game = game;
		
		Short[] realBoard = game.getPlayerBoard(GameAi.PLAYER_INDEX);
		boardForAiCalculations = new short[realBoard.length];
		for(int i = 0 ; i < realBoard.length ; i++){
			boardForAiCalculations[i] = AiCalculationBoardStatusManager.WATER;
		}
	}
	/**
	 * Subclasses of this AiPlayer must implement a way to return all fields that are potentially hiding a ship
	 * The more cleaver the AiPlayer is , the shorter List<Short> this method will return.
	 * @return List with all potentially hiding ship fields
	 */
	protected abstract List<Short> getAllPotentialForAttackFields();
	
	/**
	 * AI differ from each other in the way they determine how many ships can pass through a specific field
	 * Obstacles for a ship can be different types of fields - for example attacked_field,destroyed_ships_field
	 * ,cant_be_ship_field.The more cleaver the AiPlayer is , the more detailed list this method will return 
	 * @return
	 */
	protected abstract List<Short> getFieldTypesThatExcludePotentialShipPositions();
	
	
	@Override
	/**
	 * This implementation of generateMove depends on the count of possible ship positions passing through every
	 * potential field.From the fields with the biggest such count , randomly will be selected one , which gives
	 * the best chance to hit a ship.
	 */
	public final short generateMove() {
		Map<Short,Short> bestFieldsToChooseFrom = new HashMap<Short, Short>();
		
		//first obtain all "free for attack" fields
		List<Short> freeForAttackFields = getAllPotentialForAttackFields();//this is polymorphic call 
		
		Map<Short, Short> shipsMap = game.getShipsMapRepresentation();
		Set<Short> shipsLengthSet = shipsMap.keySet();
		
		List<ShipFieldsHolder> tempSfhList;
		
		for (Short field : freeForAttackFields) {
			//this property holds the sum of the possible shipPositions for every ship in the game that has not yet been found
			short possibleShipsCountFromAllGameShips = 0;
			
			for(Short shipLength : shipsMap.keySet()){
				tempSfhList = shipPositionManager.getShipsCrossingField(field, shipLength);
				short possibileShipsCountForGivenShipLength = getCountOfOnlyPotentialShips(tempSfhList);
				short gameShipsCountForShipLength = shipsMap.get(shipLength);
				possibleShipsCountFromAllGameShips += possibileShipsCountForGivenShipLength*gameShipsCountForShipLength;
				
				//updates the besFieldsToChooseFrom map every time with the biggest possibleShipsPosition values for a board field
				//this way, in the end the map will contain the best fields to shoot at 
				if(bestFieldsToChooseFrom.isEmpty()){
					bestFieldsToChooseFrom.put(field, possibleShipsCountFromAllGameShips);
				}else{
					short firstKey = bestFieldsToChooseFrom.keySet().iterator().next(); 
					if(bestFieldsToChooseFrom.get(firstKey) <= possibleShipsCountFromAllGameShips){
						if(bestFieldsToChooseFrom.get(firstKey) < possibleShipsCountFromAllGameShips){
							bestFieldsToChooseFrom.clear();
						}
						bestFieldsToChooseFrom.put(field, possibleShipsCountFromAllGameShips);
					}
				}
			}
		}		
		List<Short> keySetList = new ArrayList<Short>(bestFieldsToChooseFrom.keySet());
		short modul = (short)keySetList.size();
		short randomNumber = CommonUtil.getRandomIndex(modul);
		short resultField = keySetList.get(randomNumber);
		
		return resultField;
	}
	
	
	private short getCountOfOnlyPotentialShips(List<ShipFieldsHolder> allShips){
		short result = (short) allShips.size();
		Ship tempShip;
		short[] tempBoardFields;
		List<Short> fieldStatusesThatDenyShips = getFieldTypesThatExcludePotentialShipPositions();//this is polymorphic call   
		for (ShipFieldsHolder shipFieldsHolder : allShips) {
			tempShip = ShipRepresentationTransformer.getShipFromShipFieldsHolder(shipFieldsHolder);
			tempBoardFields = tempShip.getBoardFields();
			
			short boardFieldIndex;
			short boardFieldStatus;
			for(int i = 0 ; i < tempBoardFields.length ; i++){
				boardFieldIndex = tempBoardFields[i];
				boardFieldStatus = game.getPlayerBoard(GameAi.AI_INDEX)[boardFieldIndex];
				if(fieldStatusesThatDenyShips.contains(boardFieldStatus)){
					//this means that this possibleShipPosition contains a denying fieldStatus - this ship position is impossible
					result--;
				}
			}
		}
		return result;
	}
	
}
