package com.su.generator.rule;

import java.util.ArrayList;
import java.util.List;

import com.su.data.BoardField;
import com.su.data.Orientation;
/**
 * Square implementation of the FieldForbiddinRule interface
 * @author vasko
 *
 */
public class FieldForbiddingRuleSquareImpl implements FieldForbiddingRule {

	private int boardSideSize;

	/**
	 * 
	 * @param boardSideSize
	 */
	public FieldForbiddingRuleSquareImpl(int boardSideSize) {
		this.boardSideSize = boardSideSize;
	}

	//TODO : create tests for this method
	@Override
	public List<Integer> getForbiddenFields(int field) {
		List<Integer> resultList = new ArrayList<Integer>();
		//the given ship fields is sure to be forbidden
		resultList.add(field);
		
		int tempNeighbourPosition;
		BoardField bf = BoardField.getBoardFieldFromIndex(field, boardSideSize);		
		if (hasBoardNeighbourField(bf, Orientation.UP)) {
			tempNeighbourPosition = BoardField.getPositionFromBoardField(new BoardField(bf.x ,bf.y - 1),boardSideSize);
			resultList.add(tempNeighbourPosition);
		}
		if (hasBoardNeighbourField(bf, Orientation.RIGHT)) {
			tempNeighbourPosition = BoardField.getPositionFromBoardField(new BoardField(bf.x + 1,bf.y),boardSideSize);
			resultList.add(tempNeighbourPosition);
		}
		if (hasBoardNeighbourField(bf, Orientation.DOWN)) {
			tempNeighbourPosition = BoardField.getPositionFromBoardField(new BoardField(bf.x ,bf.y + 1),boardSideSize);
			resultList.add(tempNeighbourPosition);
		}
		if (hasBoardNeighbourField(bf, Orientation.LEFT)) {
			tempNeighbourPosition = BoardField.getPositionFromBoardField(new BoardField(bf.x - 1,bf.y),boardSideSize);
			resultList.add(tempNeighbourPosition);
		}			
		if (hasBoardNeighbourField(bf, Orientation.UP_RIGHT)) {
			tempNeighbourPosition = BoardField.getPositionFromBoardField(new BoardField(bf.x + 1 ,bf.y - 1),boardSideSize);
			resultList.add(tempNeighbourPosition);
		}
		if (hasBoardNeighbourField(bf, Orientation.DOWN_RIGTH)) {
			tempNeighbourPosition = BoardField.getPositionFromBoardField(new BoardField(bf.x + 1,bf.y + 1),boardSideSize);
			resultList.add(tempNeighbourPosition);
		}
		if (hasBoardNeighbourField(bf, Orientation.DOWN_LEFT)) {
			tempNeighbourPosition = BoardField.getPositionFromBoardField(new BoardField(bf.x - 1,bf.y + 1),boardSideSize);
			resultList.add(tempNeighbourPosition);
		}
		if (hasBoardNeighbourField(bf, Orientation.UP_LEFT)) {
			tempNeighbourPosition = BoardField.getPositionFromBoardField(new BoardField(bf.x - 1,bf.y - 1),boardSideSize);
			resultList.add(tempNeighbourPosition);
		}		
		
		return resultList;
	}

	private boolean hasBoardNeighbourField(BoardField bf,
			Orientation orientation) {
		boolean result;

		switch (orientation) {
		case UP:
			result = bf.y - 1 >= 0;
			break;
		case RIGHT:
			result = bf.x + 1 < boardSideSize;
			break;
		case DOWN:
			result = bf.y + 1 < boardSideSize;
			break;
		case LEFT:
			result = bf.x - 1 >= 0;
			break;
		case UP_RIGHT:
			result = (bf.y - 1 >= 0) && (bf.x + 1 < boardSideSize);
			break;
		case DOWN_RIGTH:
			result = (bf.y + 1 < boardSideSize) && (bf.x + 1 < boardSideSize);
			break;
		case DOWN_LEFT:
			result = (bf.y + 1 < boardSideSize) && (bf.x - 1 >= 0);
			break;
		case UP_LEFT:
			result = (bf.y - 1 >= 0) && (bf.x - 1 >= 0);
			break;
		default:
			// impossible case
			result = false;
			break;
		}
		return result;
	}	

	

	
	/*
	public static void main(String[] args){
		FieldForbiddingRuleCrossImpl test = new FieldForbiddingRuleCrossImpl(10);
		
		List<Integer> testResult = test.getForbiddenFields(0);
		printResult(testResult, 0);
		
		List<Integer> testResult_4 = test.getForbiddenFields(4);
		printResult(testResult_4, 4);
		
		testResult = test.getForbiddenFields(16);
		printResult(testResult, 10);
		
		testResult = test.getForbiddenFields(99);
		printResult(testResult, 99);
	}

	private static void printResult(List<Integer> list,int pos){
		System.out.println("Result for pos "+pos+" is: ");
		for(int i = 0 ; i < list.size() ; i++){
			System.out.print(" "+list.get(i));
		}
		System.out.println();
	}*/
}
