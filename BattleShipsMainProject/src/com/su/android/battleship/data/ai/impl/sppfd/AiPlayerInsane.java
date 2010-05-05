package com.su.android.battleship.data.ai.impl.sppfd;

import java.util.ArrayList;
import java.util.List;

import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.ai.AiCalculationBoardStatusManager;
import com.su.android.battleship.data.ai.AiPlayer_ShipPositionsPerFieldDependant;

public class AiPlayerInsane extends AiPlayer_ShipPositionsPerFieldDependant {

	
	public AiPlayerInsane(GameAi game) {
		super(game);
	}
	
	@Override
	protected List<Short> getAllPotentialForAttackFields() {
		List<Short> resultList = new ArrayList<Short>();
		for(int i = 0 ; i < boardForAiCalculations.length ; i++){
			if(boardForAiCalculations[i] == AiCalculationBoardStatusManager.WATER){
				resultList.add((short) i);
			}
		}		
		return resultList;
	}

	@Override
	protected List<Short> getFieldTypesThatExcludePotentialShipPositions() {
		List<Short> resultList = new ArrayList<Short>();
		
		//this AI knows everything and plays perfectly
		resultList.add(AiCalculationBoardStatusManager.DESTROYED_SHIP);
		resultList.add(AiCalculationBoardStatusManager.EMPTY);		
		resultList.add(AiCalculationBoardStatusManager.CANT_BE_SHIP);
		
		return resultList;
	}

}
