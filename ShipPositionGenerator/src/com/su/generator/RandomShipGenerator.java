package com.su.generator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.su.data.ShipFieldsHolder;
import com.su.generator.encoder.ShipPositionEncoder;
import com.su.generator.encoder.ShipPositionEncoderDefaultImpl;
import com.su.generator.randomizer.ExcludeRowRandomizer;
import com.su.generator.randomizer.ExcludeRowRandomizerAllExcludedException;
import com.su.generator.rule.FieldForbiddingRule;
import com.su.generator.rule.FieldForbiddingRuleSquareImpl;
import com.su.manager.ForbiddenPositionsManager;
import com.su.manager.ShipPositionsContainingFieldManager;

/**
 * This class implements logic for generating random ship position That is - it
 * holds a list of the forbidden fields via the ForbiddenPositionsManager. From
 * those fields , using the ForbiddenShipsManager is receives all the
 * 'impossible' ships - those that can not be added to the board (because of the
 * game rules). Then, those ships are encoded with the shipPositionEncoder and
 * they can be used to obtain a random encoded ship using the
 * ExcludeRowRandomizer.When the random ship code is produced , it is decoded
 * and the ship is returned to the business logic of the game , and the
 * ForbiddenPositionsManager is updated.
 * 
 * @author vasil.konstantinov
 * 
 */
public class RandomShipGenerator {

	private short boardSideSize;

	private ExcludeRowRandomizer randomizer;
	private ForbiddenPositionsManager positionManager;
	private ShipPositionEncoder encoder;
	private ShipPositionsContainingFieldManager shipsManager;
	private FieldForbiddingRule rule;

	/**
	 * 
	 * @param boardSideSize
	 *            - the size of the shipBoard , from which a random ship will be
	 *            generated
	 */
	public RandomShipGenerator(short boardSideSize) {
		this.boardSideSize = boardSideSize;

		this.randomizer = new ExcludeRowRandomizer();
		this.encoder = new ShipPositionEncoderDefaultImpl(
				(short) (boardSideSize));
		this.positionManager = new ForbiddenPositionsManager();
		this.shipsManager = new ShipPositionsContainingFieldManager(boardSideSize);
		
//		this.rule = new FieldForbiddingRuleCrossImpl(boardSideSize);
		this.rule = new FieldForbiddingRuleSquareImpl(boardSideSize);
	}

	/**
	 * This method generates random ship position from all possible ship
	 * positions Remember to update forbiddenFields' state via the
	 * <strong>updateForbiddenFieldsState</strong> method if the generated
	 * random ship should be used for further calculations - that is its
	 * forbidden fields are to be considered in an eventual next random position
	 * generation
	 * 
	 * @param shipLength
	 *            - the length of the ship ,for which a position should be
	 *            generated
	 * @return - the randomly chosen shipPosition
	 * @throws AllShipPositionsForbiddenException
	 */
	public ShipFieldsHolder generateRandomShipPosition(short shipLength)
			throws AllShipPositionsForbiddenException {
		ShipFieldsHolder resultShip = null;
		// first get all forbidden fields
		Set<Short> currentForbiddenFields = positionManager
				.getAllForbiddenFields();
		Iterator<Short> iterator = currentForbiddenFields.iterator();

		// from every forbidden field determine all forbidden ship positions
		Set<ShipFieldsHolder> allForbiddenShips = new HashSet<ShipFieldsHolder>();
		List<ShipFieldsHolder> tempForbiddenShips;
		short tempField;
		while (iterator.hasNext()) {
			tempField = iterator.next();
			tempForbiddenShips = shipsManager.getShipsCrossingField(tempField,
					shipLength);
			allForbiddenShips.addAll(tempForbiddenShips);
		}

		// encode all forbiddenShips
		SortedSet<Integer> encodedForbiddenShips = encodeAllForbiddenShips(allForbiddenShips);

		// generate random position
		int encodedPositionsCount = encoder.encodedPositionsCount(shipLength);
		try {
			int randomShipPosition = randomizer.getRandomIndexFromExclusiveRow(
					encodedPositionsCount, encodedForbiddenShips);

			// transform the position back to a ShipFieldsHolder object

			resultShip = encoder.getShipRepresentation(randomShipPosition,
					shipLength);
			return resultShip;
		} catch (ExcludeRowRandomizerAllExcludedException e) {
			throw new AllShipPositionsForbiddenException(
					"Can not generate ship position - all ship positions are forbidden");
		}
	}

	/**
	 * Use this method a generated random ship should be used for further
	 * calculations - that is its forbidden fields are to be considered in an
	 * eventual next random position generation
	 * 
	 * @param ship
	 *            - ship with which the forbiddenFields state should be updated
	 */
	public void addShipUpdateState(ShipFieldsHolder ship) {

		List<Short> shipFields = ShipFieldsHolder.getShipFields(ship,
				boardSideSize);
		Iterator<Short> iterator = shipFields.iterator();
		short temp;
		List<Integer> forbiddenFieldsFromShipField;
		while (iterator.hasNext()) {
			temp = iterator.next();
			forbiddenFieldsFromShipField = rule.getForbiddenFields(temp);
			for (Integer i : forbiddenFieldsFromShipField) {
				int _i = i;
				short s = (short) _i;
				positionManager.addForbiddenField(s);
			}
		}
	}
	
	private SortedSet<Integer> encodeAllForbiddenShips(
			Set<ShipFieldsHolder> allShips) {
		SortedSet<Integer> resultSet = new TreeSet<Integer>();
		ShipFieldsHolder tempSFH;

		Iterator<ShipFieldsHolder> iterator = allShips.iterator();
		while (iterator.hasNext()) {
			tempSFH = iterator.next();
			int tempCode = encoder.getPositionCode(tempSFH);
			resultSet.add(tempCode);
		}
		return resultSet;
	}

	public static void main(String[] args) {
//		testOnBoard4();
//		testOnBoard10();
	}
	
	@SuppressWarnings("unused") //this is test method and is sometimes commented
	private static void testOnBoard4(){
		short boardSideSize = 4;
		ShipFieldsHolder temp;

		RandomShipGenerator test = new RandomShipGenerator(boardSideSize);
		try {
			temp = test.generateRandomShipPosition((short) 3);
			printTest(temp, boardSideSize);

			test.addShipUpdateState(temp);
			temp = test.generateRandomShipPosition((short) 2);
			printTest(temp, boardSideSize);

			test.addShipUpdateState(temp);
			temp = test.generateRandomShipPosition((short) 2);
			printTest(temp, boardSideSize);
		} catch (AllShipPositionsForbiddenException e) {
			System.err.println(e.getMessage());
		}
	}
	
	@SuppressWarnings("unused") //this is test method and is sometimes commented
	private static void testOnBoard10(){
		short boardSideSize = 10;
		ShipFieldsHolder temp;

		RandomShipGenerator test = new RandomShipGenerator(boardSideSize);
		try {
			temp = test.generateRandomShipPosition((short) 5);
			printTest(temp, boardSideSize);

			test.addShipUpdateState(temp);
			temp = test.generateRandomShipPosition((short) 4);
			printTest(temp, boardSideSize);

			test.addShipUpdateState(temp);
			temp = test.generateRandomShipPosition((short) 3);
			printTest(temp, boardSideSize);
			
			test.addShipUpdateState(temp);
			temp = test.generateRandomShipPosition((short) 3);
			printTest(temp, boardSideSize);
			
			test.addShipUpdateState(temp);
			temp = test.generateRandomShipPosition((short) 3);
			printTest(temp, boardSideSize);
		} catch (AllShipPositionsForbiddenException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void printTest(ShipFieldsHolder sfh, short boardSideSize) {
		List<Short> list = ShipFieldsHolder.getShipFields(sfh, boardSideSize);
		System.out.println("Generated ship's fields are:");
		for (Short s : list) {
			System.out.print(" " + s);
		}
		System.out.println();
	}
}
