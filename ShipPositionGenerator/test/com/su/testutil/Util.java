package com.su.testutil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Common Util class for all the Test classes
 * @author vasko
 *
 */
public class Util {

	/**
	 * 
	 * @param <T> - generic type of returned Set
	 * @param array - the array that is to be transformed to Set
	 * @return - Set containing array's elements
	 */
	public static <T> Set<T> getSetFromArray(T[] array){
		List<T> list = Arrays.asList(array);
		Set<T> result = new HashSet<T>(list);
		return result;
	}
}
