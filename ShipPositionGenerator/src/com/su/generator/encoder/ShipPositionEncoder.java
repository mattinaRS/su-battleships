package com.su.generator.encoder;

import com.su.data.ShipFieldsHolder;

/**
 * Class that encodes and decodes ship positions
 * @author vasko
 *
 */
public interface ShipPositionEncoder {
	
	/**
	 * @param shipSize - the length of a ship 
	 * @return - the number of encoded ship positions on the given board
	 */
	public int encodedPositionsCount(short shipSize);
	
	/**
	 * Method to encode given ship to a position code
	 * @param sfh - ship representation
	 * @return - integer code of the ship
	 */
	public int getPositionCode(ShipFieldsHolder sfh);
	
	
	/**
	 * Method to decode ship from a coded position
	 * @param codedPosition - the encoded position of the ship
	 * @param shipSize - the size of the encoded ship
	 * @return - ship representation from the given encoded ship position
	 */
	public ShipFieldsHolder getShipRepresentation(int codedPosition,short shipSize);
}
