package com.su.manager;

import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.su.data.Direction;
import com.su.data.ShipFieldsHolder;

/**
 * Test class for ShipPositionsContainingFieldManager
 * Will test for correctness methods that return vertical and horizontal ships passing through certain board field
 * @author vasko
 *
 */
public class ShipPositionsContainingFieldManagerTest {

	private static final short BOARD_SIDE_4 = 4;
	
	/**
	 * Test for getHorizontalShipsCrossingField method
	 */
	@Test
	public void testGetHorizontalShipsCrossingField() {	
		
		Set<ShipFieldsHolder> setToTest;
		Set<ShipFieldsHolder> actualSet;
		boolean areEqual;
		
		setToTest = getSetHorizontalShipsToTest((short)0, (short)3);
		actualSet = getActualHorizontalSet_pos0_shipLength3();	
		areEqual = setToTest.equals(actualSet);
		assertTrue(areEqual);
				
		
		setToTest = getSetHorizontalShipsToTest((short)6, (short)3);
		actualSet = getActualHorizontalSet_pos6_shipLength3();	
		areEqual = setToTest.equals(actualSet);
		assertTrue(areEqual);
				
		
		setToTest = getSetHorizontalShipsToTest((short)11, (short)3);
		actualSet = getActualHorizontalSet_pos11_shipLength3();	
		areEqual = setToTest.equals(actualSet);
		assertTrue(areEqual);
				
	}
	
	private Set<ShipFieldsHolder> getSetHorizontalShipsToTest(short field,short shipLength){
		ShipPositionsContainingFieldManager manager = new ShipPositionsContainingFieldManager(BOARD_SIDE_4);
		List<ShipFieldsHolder> list  = manager.getHorizontalShipsCrossingField(field, shipLength);
		
		Set<ShipFieldsHolder> resultSet = new HashSet<ShipFieldsHolder>(list);
		return resultSet;		
	}
	
	
	private Set<ShipFieldsHolder> getActualHorizontalSet_pos0_shipLength3(){
		Set<ShipFieldsHolder> resultSet = new HashSet<ShipFieldsHolder>();
		resultSet.add(new ShipFieldsHolder(3, 0, Direction.HORIZONTAL));
		return resultSet;
	}	
	
	private Set<ShipFieldsHolder> getActualHorizontalSet_pos6_shipLength3(){
		Set<ShipFieldsHolder> resultSet = new HashSet<ShipFieldsHolder>();
		resultSet.add(new ShipFieldsHolder(3, 4, Direction.HORIZONTAL));
		resultSet.add(new ShipFieldsHolder(3, 5, Direction.HORIZONTAL));
		return resultSet;
	}
		
	
	private Set<ShipFieldsHolder> getActualHorizontalSet_pos11_shipLength3(){
		Set<ShipFieldsHolder> resultSet = new HashSet<ShipFieldsHolder>();
		resultSet.add(new ShipFieldsHolder(3, 9, Direction.HORIZONTAL));
		return resultSet;
	}

	/**
	 * Test for GetVerticalShipsCrossingField
	 */
	@Test
	public void testGetVerticalShipsCrossingField() {
		Set<ShipFieldsHolder> setToTest;
		Set<ShipFieldsHolder> actualSet;
		boolean areEqual;
		
		setToTest = getSetVerticalShipsToTest((short)3, (short)3);
		actualSet = getActualVerticalSet_pos3_shipLength3();	
		areEqual = setToTest.equals(actualSet);
		assertTrue(areEqual);
				
		
		setToTest = getSetVerticalShipsToTest((short)4, (short)3);
		actualSet = getActualVerticalSet_pos4_shipLength3();	
		areEqual = setToTest.equals(actualSet);
		assertTrue(areEqual);
				
		
		setToTest = getSetVerticalShipsToTest((short)14, (short)3);
		actualSet = getActualVerticalSet_pos14_shipLength3();	
		areEqual = setToTest.equals(actualSet);
		assertTrue(areEqual);
	}
	
	private Set<ShipFieldsHolder> getSetVerticalShipsToTest(short field,short shipLength){
		ShipPositionsContainingFieldManager manager = new ShipPositionsContainingFieldManager(BOARD_SIDE_4);
		List<ShipFieldsHolder> list  = manager.getVerticalShipsCrossingField(field, shipLength);
		
		Set<ShipFieldsHolder> resultSet = new HashSet<ShipFieldsHolder>(list);
		return resultSet;		
	}
	
	private Set<ShipFieldsHolder> getActualVerticalSet_pos3_shipLength3(){
		Set<ShipFieldsHolder> resultSet = new HashSet<ShipFieldsHolder>();
		resultSet.add(new ShipFieldsHolder(3, 3, Direction.VERTICAL));
		return resultSet;
	}	
	
	private Set<ShipFieldsHolder> getActualVerticalSet_pos4_shipLength3(){
		Set<ShipFieldsHolder> resultSet = new HashSet<ShipFieldsHolder>();
		resultSet.add(new ShipFieldsHolder(3, 0, Direction.VERTICAL));
		resultSet.add(new ShipFieldsHolder(3, 4, Direction.VERTICAL));
		return resultSet;
	}
		
	
	private Set<ShipFieldsHolder> getActualVerticalSet_pos14_shipLength3(){
		Set<ShipFieldsHolder> resultSet = new HashSet<ShipFieldsHolder>();
		resultSet.add(new ShipFieldsHolder(3, 6, Direction.VERTICAL));
		return resultSet;
	}

}
