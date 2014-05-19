package edu.jhu.cs.ctien4.macrotracker;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class implements the logic for the layout in addfood_fragment.
 * It also keeps a reference to the database helper object which will
 * be used to add items to the database.
 * 
 * @author Andy Tien
 *
 */

public class AddFoodFragment extends Fragment {
	private LoggerDB db;
	private boolean dbSet = false;

	private static EditText name_input;
	private static EditText protein_input;
	private static EditText carb_input;
	private static EditText fat_input;
	private static TextView cal_input;
	private static TextView date_input;

	private static int proteins;
	private static int carbs;
	private static int fats;
	private static int calories;

	onDatabaseUpdateListener activityCallback;

	/**
	 * Interface to allow the fragment to communicate back to parent activity
	 * 
	 * @author Andy
	 * 
	 */
	public interface onDatabaseUpdateListener {
		public void onDatabaseUpdate();
	}

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
     * This class overrides the onCreateView and uses the inflater
     * to inflate the addfood_fragment layout. It also adds listeners
     * to the date_input object and the submit button, to process
     * the data correctly.
     *
     */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.addfood_fragment, container,
				false);

		// Grab references to the text files
		name_input = (EditText) view.findViewById(R.id.nameText);
		protein_input = (EditText) view.findViewById(R.id.proteinText);
		carb_input = (EditText) view.findViewById(R.id.carbText);
		fat_input = (EditText) view.findViewById(R.id.fatText);
		cal_input = (TextView) view.findViewById(R.id.caloriesText);
		date_input = (TextView) view.findViewById(R.id.dateText);

		// Initialize Inputs to default values
		clearInputs();

		date_input.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(v);
			}
		});

		final Button submit_button = (Button) view
				.findViewById(R.id.submitMacrosButton);
		submit_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				submitMacros();
			}
		});

		return view;
	}

    /**
     * This class is called in the onClickListener for date_input above. 
     * It creates a new dialog fragment, which the user can use to 
     * select the correct date.
     *
     */
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	/**
	 * Submit the macro values that are entered in the EditText fields to the
	 * database.
	 */
	public void submitMacros() {
		// Grab String values from text fields.
		String name = name_input.getText().toString();
		String proteinsText = protein_input.getText().toString();
		String carbsText = carb_input.getText().toString();
		String fatsText = fat_input.getText().toString();
		String dateText = date_input.getText().toString();

		// Parse empty strings as integers, or if they are empty,
		// parse as zero
		if (proteinsText.equals("")) {
			proteins = 0;
		} else {
			proteins = Integer.parseInt(proteinsText);
		}

		if (carbsText.equals("")) {
			carbs = 0;
		} else {
			carbs = Integer.parseInt(carbsText);
		}

		if (fatsText.equals("")) {
			fats = 0;
		} else {
			fats = Integer.parseInt(fatsText);
		}

		// Compute calories using calorie constants
		calories = Util.computeCalories(proteins, carbs, fats);
		cal_input.setText(String.valueOf(calories));

		// Create food item
		Food f = new Food(name, proteins, carbs, fats);

		// Logs this food item if the database has been set
		if (dbSet) {
			db.addFood(f, dateText);
			activityCallback.onDatabaseUpdate();
		}

		// Toast the user about adding the food item
		Toast toast = Toast.makeText(getActivity(), "Food added.", Toast.LENGTH_SHORT);
		toast.show();
		
		// Clears the fields
		clearInputs();
	}

	/**
	 * Clears all editTexts and sets the date to the current date.
	 */
	public void clearInputs() {
		protein_input.setText("");
		carb_input.setText("");
		fat_input.setText("");
		name_input.setText("");
		date_input.setText(Util.getDate());
	}

	/**
	 * This method is used to set the database reference through which the
	 * fragment will call its methods.
	 * 
	 * @param db
	 *            the database to set
	 */
	public void setLoggerDb(LoggerDB db) {
		this.db = db;
		dbSet = true;
	}

	/**
	 * Internal DatePicker class to allow for the date picker to be generated.
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
			date_input.setText(Util.genDate(year, monthOfYear, dayOfMonth));
		}
	}
}
