package com.su.generator.encoder;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import com.su.data.Direction;
import com.su.data.ShipFieldsHolder;

/**
 * Test class for ShipPositionEncoderDefaultImpl
 * @author vasko
 *
 */
public class ShipPositionEncoderDefaultImplTest {
	private static final short BOARD_SIZE_4 = 4;
	private static final short BOARD_SIZE_10 = 10;
	
	private static Map<Short, ShipFieldsHolder> testMap_BoardSide_4_ShipLength_3 = new HashMap<Short, ShipFieldsHolder>();
	
	static{
		testMap_BoardSide_4_ShipLength_3.put((short)0, new ShipFieldsHolder(3, 0, Direction.HORIZONTAL));
		testMap_BoardSide_4_ShipLength_3.put((short)1, new ShipFieldsHolder(3, 1, Direction.HORIZONTAL));
		testMap_BoardSide_4_ShipLength_3.put((short)2, new ShipFieldsHolder(3, 4, Direction.HORIZONTAL));
		testMap_BoardSide_4_ShipLength_3.put((short)3, new ShipFieldsHolder(3, 5, Direction.HORIZONTAL));
		testMap_BoardSide_4_ShipLength_3.put((short)4, new ShipFieldsHolder(3, 8, Direction.HORIZONTAL));
		testMap_BoardSide_4_ShipLength_3.put((short)5, new ShipFieldsHolder(3, 9, Direction.HORIZONTAL));
		testMap_BoardSide_4_ShipLength_3.put((short)6, new ShipFieldsHolder(3, 12, Direction.HORIZONTAL));
		testMap_BoardSide_4_ShipLength_3.put((short)7, new ShipFieldsHolder(3, 13, Direction.HORIZONTAL));
		
		testMap_BoardSide_4_ShipLength_3.put((short)8, new ShipFieldsHolder(3, 0, Direction.VERTICAL));
		testMap_BoardSide_4_ShipLength_3.put((short)9, new ShipFieldsHolder(3, 4, Direction.VERTICAL));
		testMap_BoardSide_4_ShipLength_3.put((short)10, new ShipFieldsHolder(3, 1, Direction.VERTICAL));
		testMap_BoardSide_4_ShipLength_3.put((short)11, new ShipFieldsHolder(3, 5, Direction.VERTICAL));
		testMap_BoardSide_4_ShipLength_3.put((short)12, new ShipFieldsHolder(3, 2, Direction.VERTICAL));
		testMap_BoardSide_4_ShipLength_3.put((short)13, new ShipFieldsHolder(3, 6, Direction.VERTICAL));
		testMap_BoardSide_4_ShipLength_3.put((short)14, new ShipFieldsHolder(3, 3, Direction.VERTICAL));
		testMap_BoardSide_4_ShipLength_3.put((short)15, new ShipFieldsHolder(3, 7, Direction.VERTICAL));		
	}

	/**
	 * Test method for encodedPositionsCount - on a board with boardSize 10.
	 */
	@Test
	public void testEncodedPositionsCount() {
		ShipPositionEncoder encoder = new ShipPositionEncoderDefaultImpl(BOARD_SIZE_10);
				
		int posCount_3 = 160;
		int posCount_3_FromEncoder = encoder.encodedPositionsCount((short)3);
		assertEquals(posCount_3, posCount_3_FromEncoder);
		
		int posCount_5 = 120;
		int posCount_5_FromEncoder = encoder.encodedPositionsCount((short)5);
		assertEquals(posCount_5, posCount_5_FromEncoder);
		
		int posCount_8 = 60;
		int posCount_8_FromEncoder = encoder.encodedPositionsCount((short)8);
		assertEquals(posCount_8, posCount_8_FromEncoder);
		
		int posCount_10 = 20;
		int posCount_10_FromEncoder = encoder.encodedPositionsCount((short)10);
		assertEquals(posCount_10, posCount_10_FromEncoder);
	}

	/**
	 * Test method for getShipRepresentation and getPositionCode
	 */
	@Test
	public void testGetShipRepresentation_GetPositionCode() {
		ShipPositionEncoder encoder = new ShipPositionEncoderDefaultImpl(BOARD_SIZE_4);
		Iterator<Short> iterator = testMap_BoardSide_4_ShipLength_3.keySet().iterator();
		short shipSize = 3;
		short currentKey;
		ShipFieldsHolder sfhToTest;
		ShipFieldsHolder sfhActual;
				
		while(iterator.hasNext()){
			currentKey = iterator.next(); 
			sfhToTest = encoder.getShipRepresentation(currentKey, shipSize);
			sfhActual = testMap_BoardSide_4_ShipLength_3.get(currentKey);
			assertEquals(sfhToTest, sfhActual); // method equals in SHF is predefined
			
			short codeToTest = (short) encoder.getPositionCode(sfhActual);
			assertEquals(currentKey, codeToTest);
		}
		

	}

}
