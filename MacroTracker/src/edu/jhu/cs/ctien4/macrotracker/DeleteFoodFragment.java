package edu.jhu.cs.ctien4.macrotracker;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * This class is in charge of the logic behind the layout 'deletefood_fragment'.
 * 
 * @author Andy
 *
 */
public class DeleteFoodFragment extends Fragment {
	private static TextView dateInput;
	private static ListView foodListView;
	private List<Food> foodList;
	private LoggerDB db;

	// Interface for this fragment to communicate to activity.
	onDatabaseUpdateListener activityCallback;

	public interface onDatabaseUpdateListener {
		public void onDatabaseUpdate();
	}

    /**
     * When the activity loads this fragment, it uses this function to attach
     * itself to the fragment. The activity must implement the callback interface
     * above, so that the activityCallback works correctly. An exception will
     * be thrown if the parent activity does not implement the interface.
     */
	public void onAttach(Activity activity) {
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
     * Override the onCreateView in the superclass to inflate the layout.
     * Listeners are also set for the ListView and DateInput objects. The ListView
     * listener allows us to extract the index of the item the user clicked on,
     * which is then used to perform the delete operation on the object.
     */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.deletefood_fragment, container,
				false);

		dateInput = (TextView) view.findViewById(R.id.deleteDateField);
		dateInput.setText(Util.getDate());

        // Add listener for the date picker
		dateInput.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(v);
			}
		});

        // Detect when date picker has returned, so we update the ListView
		dateInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				updateFoodList();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		foodListView = (ListView) view.findViewById(R.id.deleteFoodList);

        // Add listeners to detect the index of the element that has been clicked.
		foodListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d("debug", "INT: " + String.valueOf(arg2));
				removeFood(arg2);
			}
		});
		updateFoodList();

		return view;
	}

	/**
	 * Show the date picker dialog.
	 * 
	 * @param v
	 */
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	/**
	 * Given the index of the food item, remove this food item from the
	 * database.
	 * 
	 * @param index
	 *            the index passed back from onItemClickListener
	 */
	public void removeFood(int index) {
		Log.d("debug", "removing food at index " + String.valueOf(index));
		Food f = foodList.get(index);
		db.removeFood(f, dateInput.getText().toString());
		
		// Toast the user about removing the food item
		Toast toast = Toast.makeText(getActivity(), "Food removed.", Toast.LENGTH_SHORT);
		toast.show();
		
		
		updateFoodList();
		activityCallback.onDatabaseUpdate();
	}

	/**
	 * Updates the food list view with the foods saved on the same date as the
	 * dateView text.
	 */
	public void updateFoodList() {
		String date = dateInput.getText().toString();
		foodList = db.getFoodsGivenDate(date);
		Food[] foodsArray = new Food[foodList.size()];

		for (Food food : foodList) {
			foodsArray[foodList.indexOf(food)] = food;
		}

		foodListView.setAdapter(new ArrayAdapter<Food>(getActivity(),
				android.R.layout.simple_list_item_1, foodsArray));

	}

    /**
     * Sets the reference for this fragment to the database that is passed in.
     * This reference needs to be set before database functions are called.
     *
     */
	public void setLoggerDB(LoggerDB db) {
		this.db = db;
	}

	/**
	 * Internal date picker class, which updates the dateInput.
	 * 
	 * @author Andy
	 * 
	 */
	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use current date as default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DATE);

			// Create new instance of DatePickerDialog
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			dateInput.setText(Util.genDate(year, monthOfYear, dayOfMonth));
		}
	}

}
