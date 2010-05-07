package com.su.android.battleship.data.ai.impl.sppfd;

import java.util.ArrayList;
import java.util.List;

import com.su.android.battleship.data.BoardFieldStatus;
import com.su.android.battleship.data.GameAi;
import com.su.android.battleship.data.Ship;
import com.su.android.battleship.data.ai.AiCalculationBoardStatusManager;
import com.su.android.battleship.data.ai.AiPlayer_ShipPositionsPerFieldDependant;

public class AiPlayerEasy extends AiPlayer_ShipPositionsPerFieldDependant {

	
	public AiPlayerEasy(GameAi game) {
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

		//this is easy AI - it know only that a ship can not move through another ship
		//but it does not know , that when a field is empty  there is no ship.It also does not know anything about
		//the "CANT_BE_SHIP" fields - that is for example - it plays as if 2 ships can touch each other.
		resultList.add(AiCalculationBoardStatusManager.DESTROYED_SHIP);

		return resultList;
	}

	@Override
	public void updateAfterMove(short field,short newFieldStatys) {
//		short statusCode = super.game.getPlayerBoard(GameAi.AI_INDEX)[field];
		short statusCode = newFieldStatys;
		if(BoardFieldStatus.WATER_ATTACKED == statusCode){
			boardForAiCalculations[field] = AiCalculationBoardStatusManager.EMPTY;
		}else if(BoardFieldStatus.isShipAttackedStatus(statusCode)){
			//TODO : handle hitting a shit ,but not destroying it update case
		}else if(BoardFieldStatus.isShipDestroyedStatus(statusCode)){
			short shipIndex = BoardFieldStatus.getShipIndex(statusCode);
			Ship ship = super.game.getPlayerShips(GameAi.AI_INDEX)[shipIndex];
			short[] shipBoardFields = ship.getBoardFields();
			short tempPosition;
			for (int i = 0; i < shipBoardFields.length; i++) {
				tempPosition = shipBoardFields[i];
				boardForAiCalculations[tempPosition] = AiCalculationBoardStatusManager.DESTROYED_SHIP;
			}
		}
		
	}

}
