package com.su.generator.rule;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.su.testutil.Util;

/**
 * Test class for FieldForbiddingRuleSquareImpl
 * @author vasko
 *
 */
public class FieldForbiddingRuleSquareImplTest {

	private static final short BOARD_SIDE_4 = 4;
	
	/**
	 * test method for getForbiddenFields
	 */
	@Test
	public void testGetForbiddenFields() {
		FieldForbiddingRule rule = new FieldForbiddingRuleSquareImpl(BOARD_SIDE_4);
		
		
		Set<Integer> fieldsToCheck;
		Set<Integer> actualFields;
		boolean areEqual;
		
		fieldsToCheck = new HashSet<Integer>(rule.getForbiddenFields(3));
		actualFields = getTestFields_3();
		areEqual = fieldsToCheck.equals(actualFields);
		assertTrue(areEqual);
		
		fieldsToCheck = new HashSet<Integer>(rule.getForbiddenFields(5));
		actualFields = getTestFields_5();
		areEqual = fieldsToCheck.equals(actualFields);
		assertTrue(areEqual);
		
		fieldsToCheck = new HashSet<Integer>(rule.getForbiddenFields(8));
		actualFields = getTestFields_8();
		areEqual = fieldsToCheck.equals(actualFields);
		assertTrue(areEqual);
		
		fieldsToCheck = new HashSet<Integer>(rule.getForbiddenFields(11));
		actualFields = getTestFields_11();
		areEqual = fieldsToCheck.equals(actualFields);
		assertTrue(areEqual);
		
		fieldsToCheck = new HashSet<Integer>(rule.getForbiddenFields(15));
		actualFields = getTestFields_15();
		areEqual = fieldsToCheck.equals(actualFields);
		assertTrue(areEqual);
		
		
	}
	//forbidden fields for field 3
	private Set<Integer> getTestFields_3(){
		Integer[] array = new Integer[]{2,3,6,7};
		Set<Integer> resultSet = Util.getSetFromArray(array); 
		return resultSet;
	}
	
	//forbidden fields for field 5
	private Set<Integer> getTestFields_5(){
		Integer[] array = new Integer[]{0,1,2,4,5,6,8,9,10};
		Set<Integer> resultSet = Util.getSetFromArray(array); 
		return resultSet;
	}

	//forbidden fields for field 8
	private Set<Integer> getTestFields_8(){
		Integer[] array = new Integer[]{4,5,8,9,12,13};
		Set<Integer> resultSet = Util.getSetFromArray(array); 
		return resultSet;
	}

	//forbidden fields for field 11
	private Set<Integer> getTestFields_11(){
		Integer[] array = new Integer[]{6,7,10,11,14,15};
		Set<Integer> resultSet = Util.getSetFromArray(array); 
		return resultSet;
	}
	
	//forbidden fields for field 15
	private Set<Integer> getTestFields_15(){
		Integer[] array = new Integer[]{10,11,14,15};
		Set<Integer> resultSet = Util.getSetFromArray(array); 
		return resultSet;
	}
}
