package com.su.android.battleship.util;

import java.util.Calendar;
import java.util.Random;

public class CommonUtil {
	
	private static Random random = new Random(Calendar.getInstance().getTimeInMillis());
	
	/**
	 * 
	 * @param count
	 * @return returns a random number among { 0 to count-1 }
	 */
	public static short getRandomIndex(short count){
		int randomIndex = random.nextInt();
		short resultIndex = (short) (randomIndex % count);
		if(resultIndex < 0){
			resultIndex += count;
		}
		return resultIndex;
	}

}
