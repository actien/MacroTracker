package edu.jhu.cs.ctien4.macrotracker;

public class Food {
	private String name;
	private int proteins;
	private int carbs;
	private int fats;
	
	public Food(String name, int proteins, int carbs, int fats){
		this.setProteins(proteins);
		this.setCarbs(carbs);
		this.setFats(fats);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProteins() {
		return proteins;
	}

	public void setProteins(int proteins) {
		this.proteins = proteins;
	}

	public int getCarbs() {
		return carbs;
	}

	public void setCarbs(int carbs) {
		this.carbs = carbs;
	}

	public int getFats() {
		return fats;
	}

	public void setFats(int fats) {
		this.fats = fats;
	}
	
	public String toString(){
		return name + ": " + "P = " + proteins + " C = " + carbs + " F = " + fats;
	}

}
