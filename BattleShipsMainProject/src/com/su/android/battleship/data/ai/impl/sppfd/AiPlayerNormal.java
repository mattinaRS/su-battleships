package com.su.android.battleship.data.ai.impl.sppfd;

import java.util.ArrayList;
import java.util.List;

import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.ai.AiCalculationBoardStatusManager;
import com.su.android.battleship.data.ai.AiPlayer_ShipPositionsPerFieldDependant;

public class AiPlayerNormal extends AiPlayer_ShipPositionsPerFieldDependant {

	public AiPlayerNormal(GameAi game) {
		super(game);		
	}

	@Override
	protected List<Short> getAllPotentialForAttackFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Short> getFieldTypesThatExcludePotentialShipPositions() {
		List<Short> resultList = new ArrayList<Short>();

		//this is normal AI - it know that a ship can not move through empty field
		//but it does not know , that a ship can not run through another ship - not very sensible , but this is a smaller flaw in the algorithm than in the easy AI 
		//as often as for every other field
		resultList.add(AiCalculationBoardStatusManager.EMPTY);
		resultList.add(AiCalculationBoardStatusManager.DESTROYED_SHIP);

		return resultList;
	}

}
