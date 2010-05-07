package com.su.android.battleship.ui.data;

import java.io.Serializable;

import com.su.android.battleship.data.Ship;

public class ActivityShipComunicator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Ship[] ships;

	public ActivityShipComunicator(Ship[] aShips) {
		ships = aShips;
	}

	public Ship[] getShips() {
		return ships;
	}
}
