package com.su.android.battleship.ui.data;

import java.io.Serializable;

import com.su.android.battleship.data.Ship;

/**
 * Array of Ship objects must be passed between arrangeShips and PlayGame Activities.
 * This communication is implemented via Bundle usage which requires the witness of Serializable object.  
 * @author vasko
 *
 */
public class ActivityShipComunicator implements Serializable {

	/**
	 * ??
	 */
	private static final long serialVersionUID = 1L;

	private Ship[] ships;

	/**
	 * Sets the Ship objects
	 * @param aShips
	 */
	public ActivityShipComunicator(Ship[] aShips) {
		ships = aShips;
	}

	/**
	 * 
	 * @return the array of Ship objects that is to be passed between Activities
	 */
	public Ship[] getShips() {
		return ships;
	}
}
