package com.su.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.su.data.BoardField;
import com.su.data.Direction;
import com.su.data.ShipFieldsHolder;
import com.su.generator.rule.FieldForbiddingRule;
/**
 * This class offers method to obtain all possible Ship positions , that contain a given field.
 * In the game context this will be used to obtain all the forbidden ships positions from a field
 * that is - when a board field is forbidden , no ship can be placed ot it.
 * See also {@link FieldForbiddingRule} and {@link ShipFieldsHolder}
 * @author vasil.konstantinov
 *
 */
public class ShipPositionsContainingFieldManager implements Serializable {
	
	private static final long serialVersionUID = 4263241582867844397L;
	
	private short boardSideSize;
	
	/**
	 * 
	 * @param boardSideSize
	 */
	public ShipPositionsContainingFieldManager(short boardSideSize) {
		this.boardSideSize = boardSideSize;
	}
	
	/**
	 * Here for a ship we understand a possible ship position on the board.
	 * @param fieldPosition - the field ,for which all the intersecting ships must be found
	 * @param shipLength - ships' length
	 * @return - all the ships that contain the given field. 
	 */
	public List<ShipFieldsHolder> getShipsCrossingField(int fieldPosition,int shipLength){
		List<ShipFieldsHolder> actualShips = new ArrayList<ShipFieldsHolder>();
		
		List<ShipFieldsHolder> actualHorizontalShips = getHorizontalShipsCrossingField(fieldPosition, shipLength);
		actualShips.addAll(actualHorizontalShips);
		
		List<ShipFieldsHolder> actualVerticalShips = getVerticalShipsCrossingField(fieldPosition, shipLength);
		actualShips.addAll(actualVerticalShips);
		
		return actualShips;
	}
	
	/**
	 * Here for a ship we understand a possible ship position on the board.
	 * @param fieldPosition - the field ,for which all the intersecting ships must be found
	 * @param shipLength - ships' length
	 * @return - all the vertical ships that contain the given field. 
	 */
	public List<ShipFieldsHolder> getVerticalShipsCrossingField(int fieldPosition,int shipLength){
		List<ShipFieldsHolder> actualShips = new ArrayList<ShipFieldsHolder>();
		ShipFieldsHolder tempSFH;
		BoardField tempBF;
		int boardPosition;
		
		BoardField bf = BoardField.getBoardFieldFromIndex(fieldPosition, boardSideSize);
		
		//get Horizontal ships
		int lastPossibleVerticalStartIndex = bf.y;
		int firstPossibleVerticalStartIndex = 
			firstPossibleStartField(lastPossibleVerticalStartIndex, shipLength);				
		
		for(int i = firstPossibleVerticalStartIndex ; i <= lastPossibleVerticalStartIndex ; i++){
			if(isShipEntirelyOnTheBoard(i, shipLength)){
				tempBF = new BoardField(bf.x,i);
				boardPosition = BoardField.getPositionFromBoardField(tempBF, boardSideSize);
				tempSFH = new ShipFieldsHolder(shipLength,boardPosition,Direction.VERTICAL);
				actualShips.add(tempSFH);
			}
		}
		
		return actualShips;
	}
	
	
	
	
	/**
	 * Here for a ship we understand a possible ship position on the board.
	 * @param fieldPosition - the field ,for which all the intersecting ships must be found
	 * @param shipLength - ships' length
	 * @return - all the horizontal ships that contain the given field. 
	 */
	public List<ShipFieldsHolder> getHorizontalShipsCrossingField(int fieldPosition,int shipLength){
		List<ShipFieldsHolder> actualShips = new ArrayList<ShipFieldsHolder>();
		
		ShipFieldsHolder tempSFH;
		BoardField tempBF;
		int boardPosition;
		
		BoardField bf = BoardField.getBoardFieldFromIndex(fieldPosition, boardSideSize);
		
		//get Vertical ships		
		int lastPossibleHorizontalStartIndex = bf.x;
		int firstPossibleHorizontalStartIndex = 
			firstPossibleStartField(lastPossibleHorizontalStartIndex, shipLength);
		
		for(int j = firstPossibleHorizontalStartIndex ; j <= lastPossibleHorizontalStartIndex; j++){
			if(isShipEntirelyOnTheBoard(j, shipLength)){
				tempBF = new BoardField(j,bf.y);
				boardPosition = BoardField.getPositionFromBoardField(tempBF, boardSideSize);
				tempSFH = new ShipFieldsHolder(shipLength,boardPosition,Direction.HORIZONTAL);
				actualShips.add(tempSFH);
			}
		}
		
		return actualShips;
	}	
	
	private int firstPossibleStartField(int coordinateValue,int shipLength){
		int possibleStartIndex =  Math.max(0, coordinateValue - (shipLength - 1));
		return possibleStartIndex;
	}
	
	private boolean isShipEntirelyOnTheBoard(int coordinate,int shipLength){
		return boardSideSize >= (coordinate + shipLength);
	}
	
	/**
	 * test method
	 * @param args
	 */
	public static void main(String[] args){
		ShipPositionsContainingFieldManager test = new ShipPositionsContainingFieldManager((short)4);
		List<ShipFieldsHolder> list;
		
		list = test.getShipsCrossingField(0, 3);
		printTest(list,0,3,4);
		
		list = test.getShipsCrossingField(8, 3);
		printTest(list,8,3,4);
		
		list = test.getShipsCrossingField(14, 4);
		printTest(list,14,4,4);
	}
	
	private static void printTest(List<ShipFieldsHolder> list,int throughField,int shipSize,int boardSize){
		ShipFieldsHolder temp;
		for(int i = 0 ; i < list.size() ; i++){
			temp = list.get(i);
			System.out.println("BoardSideSize:"+boardSize+" Ship through field:"+throughField+" size:"+shipSize+" is - startField:"+temp.getFirstField()+" direction:"+temp.getDirection());
		}
		System.out.println();
	}
}
