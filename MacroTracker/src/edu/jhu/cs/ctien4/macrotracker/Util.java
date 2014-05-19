package edu.jhu.cs.ctien4.macrotracker;

import java.util.Calendar;

/**
 * This class implements utility functions used by fragments across the app to ensure 
 * all results are consistent across the app.
 */
public class Util {
	public final static int PROTEIN_CALORIES = 4;
	public final static int CARBOHYDRATE_CALORIES = 4;
	public final static int FAT_CALORIES = 9;
	
	/**
	 * Generates today's date in a String format
	 * The date is padded so that it works with SQLite
	 * @return String
	 */
	public static String getDate(){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DATE);
		
		return (new StringBuilder().append(year).append("-").append(String.format("%02d", month+1))
				.append("-").append(String.format("%02d",day))).toString();
	}

    /**
     * Generates a String version of any given date that is formatted
     * to be consistent with the format in getDate(), and allows the database
     * to correctly rank date value.
     */
	public static String genDate(int year, int month, int day){
		return (new StringBuilder().append(year).append("-").append(String.format("%02d", month+1))
				.append("-").append(String.format("%02d",day))).toString();
	}

    /**
     * Computes the caloric content of a food given the macronutrient information.
     * @param proteins
     *     The grams of proteins
     * @param carbs
     *     The grams of carbs
     * @param fats
     *     The grams of fats
     */
	public static int computeCalories(int proteins, int carbs, int fats){
		return (proteins*PROTEIN_CALORIES) + (carbs*CARBOHYDRATE_CALORIES) + (fats*FAT_CALORIES);
	}
}
