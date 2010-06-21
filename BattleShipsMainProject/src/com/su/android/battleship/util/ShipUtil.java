package com.su.android.battleship.util;

import java.util.ArrayList;
import java.util.List;

import com.su.android.battleship.data.Ship;
import com.su.generator.rule.FieldForbiddingRule;

/**
 * A class with ship related utilities
 * @author Vasko and Tony
 *
 */
public class ShipUtil {
	/**
	 * converts an array of ships into a an array of their fields
	 * @param ships
	 * @return Description
	 */
	public static short[] getShipsFields(Ship[] ships) {
		List<Short> resultList = new ArrayList<Short>();
		Ship tempShip;
		for (short i = 0; i < ships.length; i++) {
			tempShip = ships[i];
			for (short j = 0; j < tempShip.getLength(); j++) {
				resultList.add(tempShip.getBoardFields()[j]);
			}
		}
		short[] result = new short[resultList.size()];
		for (short k = 0; k < result.length; k++) {
			result[k] = resultList.get(k);
		}
		return result;
	}
	
	/**
	 * Applying a forbidding rule get the fields that a ship forbids 
	 * @param ship
	 * @param rule
	 * @return Description
	 */
	public static List<Short> getForbiddenFieldsFromShip(Ship ship, FieldForbiddingRule rule) {
		List<Short> result = new ArrayList<Short>();
		short[] shipFields = ship.getBoardFields();
		
		for(short i : shipFields) {
			List<Integer> forbiddenFields = rule.getForbiddenFields(i); 
			for(int ff : forbiddenFields) {
				result.add((short) ff);
			}
		}

		return result;
	}
}
