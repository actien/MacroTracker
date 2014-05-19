package edu.jhu.cs.ctien4.macrotracker;

import java.util.Calendar;

// Utility functions to unify various computations, such as date format generation

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
	
	public static String genDate(int year, int month, int day){
		return (new StringBuilder().append(year).append("-").append(String.format("%02d", month+1))
				.append("-").append(String.format("%02d",day))).toString();
	}
	
	public static int computeCalories(int proteins, int carbs, int fats){
		return (proteins*PROTEIN_CALORIES) + (carbs*CARBOHYDRATE_CALORIES) + (fats*FAT_CALORIES);
	}
}
