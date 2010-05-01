package com.su.generator.randomizer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class wraps the logic for choosing random number from a index row
 * (numbers from 0 to a certain limit) with the additional option of excluded
 * positions in the row.Excluded positions are such indexes that are not
 * considered possible to be selected as a random result from the index row -
 * the random number will be chosen from all the others.
 * 
 * The business logic is accessible through static method which is parameterized
 * by a rowElement upperLimit and a set of excluded numbers
 * 
 * @author vasko
 * 
 */
public class ExcludeRowRandomizer {

	private  Random random;

	public ExcludeRowRandomizer() {
		random = new Random(Calendar.getInstance()
				.getTimeInMillis());
	}
	/**
	 * @param rowCount
	 *            - the upper exclusive limit of the index row - if rowCount is
	 *            5, the row is {0,1,2,3,4}
	 * @param excludedNumbers
	 *            - set of numbers that are not to be chosen from - if {2,3} are
	 *            excluded ,this means that a random number should be chosen
	 *            from the rest of the numbers - {0,1,4}.
	 * @return - random number from the row numbers that are not excluded from
	 *         the choosing
	 * @throws ExcludeRowRandomizerAllExcludedException 
	 */
	public int getRandomIndexFromExclusiveRow(int rowCount,
			Set<Integer> excludedNumbers) throws ExcludeRowRandomizerAllExcludedException {
		// TODO : handle assert criteria - rowCount must be bigger than all
		// numbers in the excludedNumbers set and should be bigger than their
		// count
		int modul = rowCount - excludedNumbers.size();		
		
		if(modul <= 0){
			throw new ExcludeRowRandomizerAllExcludedException("All numbers are excluded from the row - no random choosing available");
		}
		
		int randomIndex = random.nextInt(modul);
		int result = getNthNotExcludedNumber(randomIndex, excludedNumbers);

		return result;
	}

	private int getNthNotExcludedNumber(int nth,Set<Integer> excludedNumbers) {
		// nth + excludedNumber.size() should be smaller then the whole size ,
		// however this method does not have to assert that
		int result = -1;
		while (nth >= 0) {
			result++;
			if (excludedNumbers.contains(result)) {
				continue;
			} else {
				nth--;
			}
		}
		return result;
	}

	public static void main(String[] args) throws ExcludeRowRandomizerAllExcludedException {
		ExcludeRowRandomizer test = new ExcludeRowRandomizer();
		
		SortedSet<Integer> set = new TreeSet<Integer>();
		set.add(0);
		set.add(2);
		set.add(1);
		set.add(4);
		
		test.getRandomIndexFromExclusiveRow(7, set);

		int temp;
		for (int i = 0; i < 20; i++) {
			temp = test.getRandomIndexFromExclusiveRow(7, set);
			System.out
					.println("random from {0,1,2,3,4,5,6} excluding {0,1,2,4}: "
							+ temp);
			
		}
	}

}
