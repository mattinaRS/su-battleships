package com.su.generator;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;


import com.su.data.Direction;
import com.su.data.ShipFieldsHolder;

/**
 * Test random ship generator
 * @author Vasko
 *
 */
public class RandomShipGeneratorTest {

	private static final short BOARD_SIZE_4 = 4;
//	private static final short BOARD_SIZE_10 = 10;
	
	/**
	 * Test for generateRandomShipPosition method
	 * This method will check whether or not a generated position is "available" ship position
	 * This is done with the approach : 
	 * All the free ship positions are calculated from the RandomShipGenerator.
	 * After that , from them , another module - Randomizer - picks randomly - this is this modul's test for correctness
	 * The thing that should be tested in this module is whether or not this module calculates all the
	 * possible ship positions correctly - this is done here with hard-coded ship positions check on a board 4x4.
	 */
	@Test	
	public void testGetAllEncodedForbiddenShips() {
		RandomShipGenerator generator = new RandomShipGenerator(BOARD_SIZE_4);
		ShipFieldsHolder firstShip = new ShipFieldsHolder(3, 6, Direction.VERTICAL);
		generator.addShipUpdateState(firstShip);
		
		Set<Integer> forbiddenForTesting = generator.getAllEncodedForbiddenShips((short)3);
		Set<Integer> actualForbiddenForTesting = getTestSetReal_1();
		
	    boolean areEqual = forbiddenForTesting.equals(actualForbiddenForTesting);
	    assertTrue(areEqual);
		
	    
	    Set<Integer> forbiddenForTesting_2 = generator.getAllEncodedForbiddenShips((short)2);
		Set<Integer> actualForbiddenForTesting_2 = getTestSetReal_2();
		
	    boolean areEqual_2 = forbiddenForTesting_2.equals(actualForbiddenForTesting_2);
	    assertTrue(areEqual_2);
	    
	}
	
	private Set<Integer> getTestSetReal_1(){
		Integer[] array = new Integer[]{0,1,2,3,4,5,6,7,10,11,12,13,14,15};
		List<Integer> list = Arrays.asList(array);
		Set<Integer> set = new HashSet<Integer>(list);
		return set;
	}
	
	private Set<Integer> getTestSetReal_2(){
		Integer[] array = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,15,16,17,18,19,20,21,22,23};
		List<Integer> list = Arrays.asList(array);
		Set<Integer> set = new HashSet<Integer>(list);
		return set;
	}

	/**
	 * When generator's state is updated , from the updating ship are obtained it's fields
	 * and for each of them are determined all the ship-forbidding fields produced from them 
	 * Here this behavior is tested for correctness
	 */
	@Test
	public void testDetermineForbiddenFields() {
		RandomShipGenerator generator = new RandomShipGenerator(BOARD_SIZE_4);
				
		ShipFieldsHolder firstShip = new ShipFieldsHolder(3, 6, Direction.VERTICAL);
		List<Integer> forbiddenFieldsForTesting = generator.determineForbiddenFields(firstShip);		
		Set<Integer> forbiddenFieldsForTesting_Set = new HashSet<Integer>(forbiddenFieldsForTesting);
		
		Set<Integer> actualForbiddenFields = getActualForbiddenFields_1(); 
		
		boolean areEqual = forbiddenFieldsForTesting_Set.equals(actualForbiddenFields);
		assertTrue(areEqual);
	}
	
	private Set<Integer> getActualForbiddenFields_1(){
		Integer[] array = new Integer[]{1,2,3,5,6,7,9,10,11,13,14,15};
		List<Integer> list = Arrays.asList(array);
		Set<Integer> result = new HashSet<Integer>(list);
		return result;
	}
	
}
