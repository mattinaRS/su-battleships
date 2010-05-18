package com.su.generator.randomizer;
/**
 * This class wraps exception caused when all numbers are to be excluded from the row, thus
 * there are no numbers to randomly choose from 
 * @author vasil.konstantinov
 *
 */
public class ExcludeRowRandomizerAllExcludedException extends Exception {
	
	/**
	 * Vasko has no idea what this field is used for
	 */
	private static final long serialVersionUID = 1L;

	public ExcludeRowRandomizerAllExcludedException(String msg) {
		super("msg");
	}
	
}
