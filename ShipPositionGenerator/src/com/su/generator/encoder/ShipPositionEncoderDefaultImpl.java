package com.su.generator.encoder;

import com.su.data.BoardField;
import com.su.data.Direction;
import com.su.data.ShipFieldsHolder;
/**
 * This is default implementation of position encoding
 * First position is for a horizontal ship , with first field at (0,0)
 * Second is for a horizontal ship starting at (0,1) and so on
 * 
 * After horizontal positions are through , the vertical positions naturally take 
 * place in the encoding order
 * 
 * The common bijective formula is something like
 * 
 *  position = 0*halfCount + y*(boardSize - shipSize  + 1) + x for HORIZONTAL Direction or
 *  position = 1*halfCount + x*(boardSize - shipSize + 1) + y for VERTICAL Direction
 *  where  x and y are 
 *  the (x,y) coordinates of the ship's first field
 *  
 *  The encoding starts from 0 !!
 * @author vasil.konstantinov
 *
 */
public class ShipPositionEncoderDefaultImpl implements ShipPositionEncoder {

	private short boardSideSize;
	
	/**
	 * 
	 * @param boardSideSize
	 */
	public ShipPositionEncoderDefaultImpl(short boardSideSize) {
		this.boardSideSize = boardSideSize;
	}
	
	@Override
	public int encodedPositionsCount(short shipSize) {
		int horizontalShipsCount = (boardSideSize - shipSize + 1) * boardSideSize;
		int verticalShipsCount = horizontalShipsCount; //board is square
		int result = horizontalShipsCount + verticalShipsCount;
		return result;
	}

	//TODO : create tests for this method
	@Override
	public int getPositionCode(ShipFieldsHolder sfh) {
		int fieldPosition = sfh.getFirstField();
		BoardField bf = BoardField.getBoardFieldFromIndex(fieldPosition, boardSideSize);
		
		int x = bf.x;
		int y = bf.y;		
		int directionSign = sfh.getDirection().equals(Direction.HORIZONTAL) ? 0 : 1;
		
		short shipSize = (short) sfh.getLength();
		int posCodesCount = encodedPositionsCount(shipSize);
		
		int positionCode;
		if(sfh.getDirection().equals(Direction.HORIZONTAL)){
			positionCode = directionSign*posCodesCount/2 + 
			(boardSideSize - shipSize + 1)*y + x;		
			
		}else{
			positionCode = directionSign*posCodesCount/2 + 
			(boardSideSize - shipSize + 1)*x + y;
		}
		
		
		return positionCode;
	}

	//TODO : create tests for this method
	@Override
	public ShipFieldsHolder getShipRepresentation(int codedPosition,short shipSize) {
		ShipFieldsHolder result;
		
		int positionCodesCount = encodedPositionsCount(shipSize);
		int halfCodes = positionCodesCount/2;
			
		int modul = boardSideSize - shipSize + 1;
		if(codedPosition < halfCodes){//means Direction is Horizontal			
			int x = codedPosition % modul; 
			int y = codedPosition / modul;			
			BoardField bf = new BoardField(x,y);
			int position = BoardField.getPositionFromBoardField(bf,boardSideSize);
			result = new ShipFieldsHolder(shipSize,position,Direction.HORIZONTAL);
		}else{//means Direction is Vertical			
			codedPosition -= halfCodes;
			int x = codedPosition / modul;
			int y = codedPosition % modul;
			
			BoardField bf = new BoardField(x,y);
			int position = BoardField.getPositionFromBoardField(bf,boardSideSize);
			result = new ShipFieldsHolder(shipSize,position,Direction.VERTICAL);
		}		
		return result;
	}

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args){
		ShipPositionEncoderDefaultImpl testEncoder = new ShipPositionEncoderDefaultImpl((short)3);
		
		ShipFieldsHolder encodedShip;
		ShipFieldsHolder decodedShip;
		int code;
		
		encodedShip = new ShipFieldsHolder(2,0,Direction.HORIZONTAL);
		code = testEncoder.getPositionCode(encodedShip);		
		printTest1(encodedShip, code);
		decodedShip = testEncoder.getShipRepresentation(code, (short)2);
		printTest2(encodedShip, decodedShip);
		
		
		encodedShip = new ShipFieldsHolder(2,4,Direction.HORIZONTAL);
		code = testEncoder.getPositionCode(encodedShip);		
		printTest1(encodedShip, code);
		decodedShip = testEncoder.getShipRepresentation(code, (short)2);
		printTest2(encodedShip, decodedShip);
		
		encodedShip = new ShipFieldsHolder(2,2,Direction.VERTICAL);
		code = testEncoder.getPositionCode(encodedShip);		
		printTest1(encodedShip, code);
		decodedShip = testEncoder.getShipRepresentation(code, (short)2);
		printTest2(encodedShip, decodedShip);
		
		encodedShip = new ShipFieldsHolder(2,3,Direction.VERTICAL);
		code = testEncoder.getPositionCode(encodedShip);		
		printTest1(encodedShip, code);
		decodedShip = testEncoder.getShipRepresentation(code, (short)2);
		printTest2(encodedShip, decodedShip);
		
	}
	
	private static void printTest1(ShipFieldsHolder sfh,int code){
		System.out.println("The code for ship (len:"+sfh.getLength()+
						   " pos:"+sfh.getFirstField()+
						   " direct:"+sfh.getDirection()+
						   " is - "+code);
	}
	
	private static void printTest2(ShipFieldsHolder sfh1,ShipFieldsHolder sfh2){
		System.out.println("decoding the code gives back the same ship: "+sfh1.equals(sfh2));
	}
	
}
