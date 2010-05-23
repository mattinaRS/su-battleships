package com.su.android.battleship.data;

import java.util.List;

import com.su.data.ShipFieldsHolder;
import com.su.generator.AllShipPositionsForbiddenException;
import com.su.generator.RandomShipGenerator;

/**
 * Wrapper class that uses RandomShipGenerator instance to generate randomly positioned ships
 * on the Game's board.
 * @author vasko
 *
 */
public class ShipPositionGenerator {
	
	private RandomShipGenerator generator;
	
	/**
	 * Public constructor
	 */
	public ShipPositionGenerator() {
		generator = new RandomShipGenerator(Game.BOARD_SIDE);
	}
	
	/**
	 * 
	 * @return hard-coded ship positions
	 */
	public Ship[] getHardcodedShipPosition(){
		Ship[] resultShips = new Ship[Game.SHIPS_COUNT]; //5
		
		Ship s1 = new Ship((short) 2,new short[]{1,2});
		Ship s2 = new Ship((short) 2,new short[]{4,5});
		Ship s3 = new Ship((short) 3,new short[]{7,8,9});
		Ship s4 = new Ship((short) 4,new short[]{20,21,22,23});
		Ship s5 = new Ship((short) 5,new short[]{25,26,27,28,29});
		
		resultShips[0] = s1;
		resultShips[1] = s2;
		resultShips[2] = s3;
		resultShips[3] = s4;
		resultShips[4] = s5;
		
		return resultShips;
	}
	
	/**
	 * 
	 * @return - randomly generated ship positions
	 */
	public Ship[] getRandomShipsPosition(){		
		short currentShipSize;
		ShipFieldsHolder tempSFH;
		List<Short> tempList;
		short[] tempArray;
		Ship[] resultShips = new Ship[Game.SHIPS_COUNT];
		
		for(int i = 0 ; i < Game.SHIPS_SIZES.length ; i++){
			currentShipSize = Game.SHIPS_SIZES[i];
			try {
				tempSFH = generator.generateRandomShipPosition(currentShipSize);
				tempList = ShipFieldsHolder.getShipFields(tempSFH, Game.BOARD_SIDE);
				tempArray = new short[tempList.size()];
				for(int j = 0 ; j < tempList.size() ; j++){
					tempArray[j] = tempList.get(j);
				}
				resultShips[i] = new Ship(currentShipSize,tempArray);
				
				//DO NOT forget to update generator with the currently randomized ship, or else ships may intersect
				generator.addShipUpdateState(tempSFH);
			} catch (AllShipPositionsForbiddenException e) {
				// TODO : design and implement Exception handling mechanism when all ship positions are forbidden and another ship position is yet to be generated
				e.printStackTrace();
			}
		}
		
		return resultShips;
	}
	
}
