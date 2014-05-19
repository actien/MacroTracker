package edu.jhu.cs.ctien4.macrotracker;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StatusFragment extends Fragment {
	private static TextView calorieField;
	private static ListView foodList;
	private LoggerDB db;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.status_fragment, container, false);
		
		calorieField = (TextView) view.findViewById(R.id.calorieField);
		foodList = (ListView) view.findViewById(R.id.statusFoodList);
		updateCalorieField();
		updateFoodList();
		return view;
	}

    /** 
     * Update the calorieField with the latest calorie values from the database.
     */
	public void updateCalorieField(){
		int calories = db.getCaloriesGivenDate(Util.getDate());
		calorieField.setText(String.valueOf(calories));
		
		Log.d("debug", "In StatusFragment. Computed calories: " + String.valueOf(calories));
	}
	
    /** 
     * Update the listView foodList with the latest foods from the database.
     */
	public void updateFoodList(){
		List<Food> foods = db.getFoodsGivenDate(Util.getDate());
		List<String> foodsString = new ArrayList<String>();
		
		for (Food food : foods) {
			foodsString.add(food.toString());
		}
		
		foodList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, foodsString));
	}

    /**
     * Set the LoggerDB database that will be used to grab the food information.
     */
	public void setLoggerDb(LoggerDB db){
		this.db = db;
	}
	
	
}
