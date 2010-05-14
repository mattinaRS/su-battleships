package com.su.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * This class implements the logic for manipulation with the forbiddenFieldsPositions.
 * 
 * 
 * @author vasil.konstantinov
 *
 */
public class ForbiddenPositionsManager {	
	/*It is important to store the count of times a field is forbidden
	 * so that this class can be reused in the ship positioning implementation*/
	private Map<Short, Short> forbiddenFieldsHolder;
	
	public ForbiddenPositionsManager() {
		this.forbiddenFieldsHolder = new HashMap<Short, Short>();
	}

	/**
	 * Stores a forbidden key in a data structure.In this implementation map is used 
	 * because it is important to remember how many times a field has been forbidden
	 * @param ff - the number of the forbidden field
	 */
	public void addForbiddenField(short ff){
			if(forbiddenFieldsHolder.containsKey(ff)){
				short currentValue = forbiddenFieldsHolder.get(ff);
				forbiddenFieldsHolder.put(ff, (short) (currentValue + 1));
			}else{
				forbiddenFieldsHolder.put(ff, (short)1);
			}
	}
	
	/**
	 * Removes a forbidden field ONE time from the data structure.This means , that if field was 
	 * forbidden more than once , it will still not be totaly removed , only the number of forbids 
	 * will be reduced with one.
	 * @param ff - the number of the forbidden field
	 */
	public void removeForbiddenField(short ff){
		if(forbiddenFieldsHolder.containsKey(ff)){
			short currentValue = forbiddenFieldsHolder.get(ff);
			if(currentValue > 1){
				forbiddenFieldsHolder.put(ff, (short) (currentValue - 1));
			}else{
				forbiddenFieldsHolder.remove(ff);
			}
		}
	}
	/**	 
	 * @return - all forbidden fields added to the data structure
	 */
	public Set<Short> getAllForbiddenFields(){
		return forbiddenFieldsHolder.keySet();
	}
	
	public boolean isForbidden(short field) {
		return forbiddenFieldsHolder.containsKey(field);
	}
}
