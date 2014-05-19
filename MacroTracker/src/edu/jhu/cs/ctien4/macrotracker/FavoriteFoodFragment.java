package edu.jhu.cs.ctien4.macrotracker;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This fragment implements the layout 'favoritefood_fragment'.
 * It allows the user to quickly re-add any foods which have been entered.
 *
 */

public class FavoriteFoodFragment extends Fragment {
	private static ListView foodListView;
	private List<Food> foodList;
	private LoggerDB db;
	
	onDatabaseUpdateListener activityCallback;
	
    /**
     * Interface to allow for the fragment to communicate back to the
     * activity.
     */
	public interface onDatabaseUpdateListener {
		public void onDatabaseUpdate();
	}
	
    /**
     * Attaches the parent activity to this fragment, checking to see
     * that the parent activity implements the interface above.
     */
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
		// Make sure container has implemented callback interface
		try {
			activityCallback = (onDatabaseUpdateListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnDatabaseUpdateListener");
		}
	}
	
    /**
     * Inflate the view outlined in the layout file and also sets the 
     * click listener for the ListView which calls addFood with the index
     * of the view element that the user clicks on.
     */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.favoritefood_fragment, container, false);
		foodListView = (ListView) view.findViewById(R.id.favoriteFoodList);
		updateFoodList();
		
		foodListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				addFood(arg2);
			}
		});
		
		return view;
	}
	

	/**
	 * Add the food at the index returned by onItemClickListener as a new food
	 * to the database with today's date.
	 * @param index
	 */
	public void addFood(int index){
		Food f = foodList.get(index);
		db.addFood(f, Util.getDate());
		
		// Toast the user about adding the food item
		Toast toast = Toast.makeText(getActivity(), "Food added.", Toast.LENGTH_SHORT);
		toast.show();
		
		updateFoodList();
		activityCallback.onDatabaseUpdate();
	}
	
	/**
	 * Updates the foodList with the foods returned by the database.
	 */
	public void updateFoodList() {
		foodList = db.getAllFoods();
		Food[] foodsArray = new Food[foodList.size()];
		
		for (Food food : foodList){
			foodsArray[foodList.indexOf(food)] = food;
		}
		
		foodListView.setAdapter(new ArrayAdapter<Food>(getActivity(), android.R.layout.simple_list_item_1, foodsArray));
	}
	
	public void setLoggerDB(LoggerDB db){
		this.db = db;
	}

}
