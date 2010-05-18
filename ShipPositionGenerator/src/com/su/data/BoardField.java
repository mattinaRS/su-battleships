package com.su.data;

/**
 * this class is implemented with the default understanding that the fields index order
 * is the natural order of a table's fileds - from left to right , and from first row to the last.
 * The same default logic is used in {@link ShipFieldsHolder}
 * @author vasil.konstantinov
 *
 */
public class BoardField {
	
	/**
	 * x coordinate
	 */
	public int x;
	
	/**
	 * y coordinate
	 */
	public int y;

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public BoardField(int x, int y) {
		this.x = x;
		this.y = y;		
	}
	
	/**
	 * 
	 * @param index
	 * @param boardSideSize
	 * @return  BoardField From Index
	 */
	public static BoardField getBoardFieldFromIndex(int index,int boardSideSize) {
		int x = index % boardSideSize;
		int y = index / boardSideSize;

		return new BoardField(x, y);
	}

	/**
	 * 
	 * @param bf
	 * @param boardSideSize
	 * @return Position From BoardField
	 */
	public static int getPositionFromBoardField(BoardField bf,int boardSideSize) {
		int position = bf.y * boardSideSize + bf.x;
		return position;
	}
}