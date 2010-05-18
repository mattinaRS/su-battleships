package com.su.generator;
/**
 * Exception that is thrown when a ship position can not be generated due to current ship's placement
 * @author vasko
 *
 */
public class AllShipPositionsForbiddenException extends Exception {

	/**
	 * Vasko has no idea what is this field used for
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public AllShipPositionsForbiddenException(String msg) {
		super(msg);		
	}
}
