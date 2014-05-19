package edu.jhu.cs.ctien4.macrotracker;


/**
 * This class is the global data class for food items.
 * It contains four fields:
 * name - String, the name of the food
 * proteins - int, the grams of protein the food contains
 * carbs - int, the grams of carbohydrates the food contains
 * fats - int, the grams of fats the food contains
 */
public class Food {
	private String name;
	private int proteins;
	private int carbs;
	private int fats;

    /** 
     * The only constructor. All fields must be set when the object is
     * created.
     */
	public Food(String name, int proteins, int carbs, int fats){
		this.setProteins(proteins);
		this.setCarbs(carbs);
		this.setFats(fats);
		this.setName(name);
	}

    /**
     * Gets the name associated with this food object.
     * @return String name
     */
	public String getName() {
		return name;
	}

    /**
     * Sets the name associated with this food object.
     */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * Gets the grams of protein associated with this food object.
     * @return int proteins
     */
	public int getProteins() {
		return proteins;
	}

    /**
     * Sets the grams of proteins associated with this food object.
     */
	public void setProteins(int proteins) {
		this.proteins = proteins;
	}

    /**
     * Gets the grms of carbohydrates associated with this food object.
     * @return int carbs 
     */
	public int getCarbs() {
		return carbs;
	}

    /**
     * Sets the grams of carbohydrates associated with this food object.
     */
	public void setCarbs(int carbs) {
		this.carbs = carbs;
	}

    /**
     * Gets the grabs of fats associated with this food object.
     * @return int fats
     */
	public int getFats() {
		return fats;
	}

    /**
     * Sets the grams of fats associated with this food object.
     */
	public void setFats(int fats) {
		this.fats = fats;
	}

    /** 
     * Returns the string representation of the Food object.
     */
	public String toString(){
		return name + ": " + "P = " + proteins + " C = " + carbs + " F = " + fats;
	}

}
