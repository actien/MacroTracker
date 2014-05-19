package edu.jhu.cs.ctien4.macrotracker;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LoggerDB extends SQLiteOpenHelper {
	// DB Version
	private final static int DB_VERSION = 1;
	// DB name
	private final static String DB_NAME = "LoggerDB";

	// Table name
	private final static String TABLE_FOOD = "foodlog";
	
	// Column names
	private final static String KEY_ID = "id";
	private final static String KEY_TIME = "time";
	private final static String KEY_PROTEINS = "proteins";
	private final static String KEY_CARBS = "carbs";
	private final static String KEY_FATS = "fats";
	private final static String KEY_NAME = "name";
	
//	private final static String[] COLUMNS = {KEY_NAME, KEY_PROTEINS, KEY_CARBS, KEY_FATS}; 
	
	public LoggerDB(Context context){
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("debug", "creating database in onCreate");
		
		// SQL statement to create log
		String CREATE_FOOD_LOG = "CREATE TABLE foodlog ( " +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"time TEXT, " +
				"name TEXT, " +
				"proteins INTEGER, " +
				"carbs INTEGER, " +
				"fats INTEGER )";
		
		// create table
		db.execSQL(CREATE_FOOD_LOG);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table
		db.execSQL("DROP TABLE IF EXISTS foodlog");
		
		// Create new one
		this.onCreate(db);

	}
	
	/**
	 * Adds a food object along with the date
	 * @param f
	 * @param date
	 */
	
	public void addFood(Food f, String date){
		Log.d("debug", "In addFood");
		
		// Get ref to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		
		// Create content values to the key column/value
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, f.getName());
		values.put(KEY_PROTEINS, f.getProteins());
		values.put(KEY_CARBS, f.getCarbs());
		values.put(KEY_FATS, f.getFats());
		values.put(KEY_TIME, date);
		
		Log.d("debug", "Adding: " + f.toString());
		
		// Insert
		db.insert(TABLE_FOOD, null, values);
		
		// Close
		db.close();
	}
	
	public void removeFood(Food f, String date){
//		String query = String.format("DELETE FROM %s WHERE %s='%s' and %s='%s' and "
//				+ "%s = '%s' and %s = '%s'", TABLE_FOOD, 
//				KEY_NAME, f.getName(), 
//				KEY_PROTEINS, String.valueOf(f.getProteins()), 
//				KEY_CARBS, String.valueOf(f.getCarbs()),
//				KEY_FATS, String.valueOf(f.getFats())); 
//		
//		Log.d("debug", "Query is: " + query);
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FOOD,
				KEY_NAME+ " = ? and " +
				KEY_PROTEINS + " = ? and " +
				KEY_CARBS + " = ? and " +
				KEY_FATS + " = ? and " +
				KEY_TIME + " = ? ",
				new String[]{f.getName(), 
				String.valueOf(f.getProteins()), 
				String.valueOf(f.getCarbs()), 
				String.valueOf(f.getFats()), date});
		
		db.close();
		
	}
	
	// This method updates an entry in the log
	// It expects to be passed the id; the view is responsible
	// for figuring this out
	public void updateFood(Food f, int id){
		// Get reference to writeableDB
		SQLiteDatabase db = this.getWritableDatabase();
		
		// Extract new content values from Food object
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, f.getName());
		values.put(KEY_PROTEINS, f.getProteins());
		values.put(KEY_CARBS, f.getCarbs());
		values.put(KEY_FATS, f.getFats());
		
		Log.d("debug", "Updating: " + f.toString());
		
		// Update
		db.update(TABLE_FOOD, values, KEY_ID+" = ?", new String[] { String.valueOf(id) });
	}
	
	// This method computes the calories for a given date 
	public int getCaloriesGivenDate(String date){
		//build query
		String query = "SELECT * FROM " + TABLE_FOOD + " WHERE time = '" + date + "'";
		
		// Send query
		// Get ref to ReadableDB
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		int totalCalories = 0;
		if (cursor.moveToFirst()) {
			do {
				int p = cursor.getInt(cursor.getColumnIndex(KEY_PROTEINS));
				int c = cursor.getInt(cursor.getColumnIndex(KEY_CARBS));
				int f = cursor.getInt(cursor.getColumnIndex(KEY_FATS));
				//String time = cursor.getString(cursor.getColumnIndex(KEY_TIME));
				//Log.d("debug", time);
				totalCalories += Util.computeCalories(p, c, f);
			} while (cursor.moveToNext());
		}
		
		return totalCalories;
	}

	/**
	 * This function runs a query which groups all entries by date, summing
	 * the macros. Each row is then returned in a graphPoint. This function
	 * should only be called by GraphFragment.
	 */
	public List<GraphPoint> getMacrosGroupedDate(){
		List<GraphPoint> points = new ArrayList<GraphPoint>();
		
		//build query
		String query = String.format("SELECT %s, sum(%s) as %s, sum(%s) as %s, sum(%s) as %s FROM "
				+ "%s GROUP BY %s", KEY_TIME, KEY_PROTEINS, KEY_PROTEINS, KEY_CARBS,
				KEY_CARBS, KEY_FATS, KEY_FATS, TABLE_FOOD, KEY_TIME);
		
		Log.d("debug", "Query is: " + query);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		// x-coordinate to make graph
		int x = 0;
		
		if (cursor.moveToFirst()) {
			do {
				int p = cursor.getInt(cursor.getColumnIndex(KEY_PROTEINS));
				int c = cursor.getInt(cursor.getColumnIndex(KEY_CARBS));
				int f = cursor.getInt(cursor.getColumnIndex(KEY_FATS));
				String time = cursor.getString(cursor.getColumnIndex(KEY_TIME));
				
				int calories = Util.computeCalories(p, c, f);
				
				GraphPoint point = new GraphPoint(x, calories, time);
				
				points.add(point);
				x = x + 1;
			} while (cursor.moveToNext());
		}
		
		return points;
	}
	
	public List<Food> getFoodsGivenDate(String date){
		// Make list
		List<Food> foodList = new ArrayList<Food>();
		
		Log.d("debug", "In getTodaysFoods()");
		
		//build query
		String query = "SELECT * FROM " + TABLE_FOOD + " WHERE time = '" + date + "'";
				
		// Send query
		// Get ref to ReadableDB
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
			
		
		if (cursor.moveToFirst()) {
			do {
				int p = cursor.getInt(cursor.getColumnIndex(KEY_PROTEINS));
				int c = cursor.getInt(cursor.getColumnIndex(KEY_CARBS));
				int f = cursor.getInt(cursor.getColumnIndex(KEY_FATS));
				String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
							
				Food curFood = new Food(name, p, c, f);
				
				Log.d("debug", "Processed food: " + curFood.toString());
				
				foodList.add(curFood);
			} while (cursor.moveToNext());
		}
			
		return foodList;
	}
	
	public List<Food> getAllFoods(){
		List<Food> foodList = new ArrayList<Food>();
		
		// build query
		String query = String.format("SELECT DISTINCT %s, %s, %s, %s FROM %s ORDER BY %s",
				KEY_NAME,
				KEY_PROTEINS,
				KEY_CARBS,
				KEY_FATS,
				TABLE_FOOD,
				KEY_NAME);
		
		// Send query
				// Get ref to ReadableDB
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
					
				
		if (cursor.moveToFirst()) {
			do {
				int p = cursor.getInt(cursor.getColumnIndex(KEY_PROTEINS));
				int c = cursor.getInt(cursor.getColumnIndex(KEY_CARBS));
				int f = cursor.getInt(cursor.getColumnIndex(KEY_FATS));
				String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
									
				Food curFood = new Food(name, p, c, f);
					
				Log.d("debug", "Processed food: " + curFood.toString());
						
				foodList.add(curFood);
			} while (cursor.moveToNext());
		}
					
		return foodList;
	}
	
}
