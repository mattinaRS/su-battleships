package com.su.data;

import java.util.ArrayList;
import java.util.List;
/**
 * Holder class for a Ship's fields
 * @author vasko
 *
 */
public class ShipFieldsHolder {
	
	private int length;
	private int firstField;
	private Direction direction;
	
	/**
	 * 
	 * @param length
	 * @param firstField
	 * @param direction
	 */
	public ShipFieldsHolder(int length, int firstField, Direction direction) {
		super();
		this.length = length;
		this.firstField = firstField;
		this.direction = direction;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the firstField
	 */
	public int getFirstField() {
		return firstField;
	}

	/**
	 * @param firstField the firstField to set
	 */
	public void setFirstField(int firstField) {
		this.firstField = firstField;
	}

	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	/**
	 * @param sfh - the ShipFieldsHolder ship representation from which a List with all ship fields should be returned
	 * @param boardSideSize - board's side size
	 * @return - returns List with the ship's fields 
	 */
	public static List<Short> getShipFields(ShipFieldsHolder sfh,short boardSideSize){
		List<Short> resultList;
		
		int firstField = sfh.getFirstField();
		int shipLength = sfh.getLength();
		Direction d = sfh.getDirection();		
		
		switch (d) {
		case HORIZONTAL:
			resultList = getShipFieldsFromHorizontalSFH(firstField,shipLength);
			break;
		case VERTICAL:
			resultList = getShipFieldsFromVerticalSFH(firstField,shipLength,boardSideSize);
			break;
		default:
			resultList = null;
			break;
		}
		
		return resultList;
	}
	
	private static List<Short> getShipFieldsFromHorizontalSFH(int firstField,int shipLength){
		List<Short> resultList = new ArrayList<Short>();
		for(int i = 0 ; i < shipLength ; i++){
			resultList.add((short)(firstField + i));
		}		
		return resultList;
	}
	
	private static List<Short> getShipFieldsFromVerticalSFH(int firstField,int shipLength,short boardSideSize){
		List<Short> resultList = new ArrayList<Short>();
		for(int i = 0 ; i < shipLength ; i++){
			resultList.add((short)(firstField + boardSideSize*i));
		}		
		return resultList;
	}
	
	/**
	 * This method is overridden so that effective Set's add operation can be executed
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		
		if(!(obj instanceof ShipFieldsHolder)){
			return false;
		}
		
		ShipFieldsHolder sfh = (ShipFieldsHolder)obj;
		if(this.length == sfh.length && this.firstField == sfh.firstField
				&& this.direction.equals(sfh.direction)){
			return true;
		}
		return false;
	}
	
	
}
