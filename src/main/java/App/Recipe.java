package App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class represents a recipe that is added to a list of matches.
 * To prevent redundant info lookup, the getInfo method must be called
 * before methods getIngredients, getAmounts, and getInstructions can
 * be called. (The app shows the recipes before allowing editing anyway)
 */

public class Recipe {
	// private variables for safe encapsulation (And for the fact
	// that real contents of the recipe is still in a file so
	// if the method getInfo is not called, the recipe contents will not
	// have to be looked at. I guess this is one advantage of encapsulation)

	// stores the name of the recipe
	private String name;
	// points to the file containing this recipe
	private File fileLocation;
	// number of matches (for sorting by relevance)
	private int matches;
	// stores information about recipe (is empty before getInfo() call)
	private StringBuilder info;
	// stores ingredients (is empty before getInfo() call)
	private String[] ingredients;
	// stores amounts (is empty before getInfo() call)
	private String[] amounts;
	// stores instructions for recipe (is empty before getInfo() call)
	private StringBuilder instructions;

	/**
	 * creates a Recipe object
	 *
	 * @param  filename  the filename of the recipe (name + .txt)
	 * @param  matches  number of matches in search (at least 1)
	 */
	public Recipe(String filename, int matches) {
		this.name = filename.substring(0, filename.length() - 4);
		this.matches = matches;
		this.fileLocation = new File(RecipeHelper.recipesPath + filename);
		this.info = new StringBuilder();
	}

	/**
	 * returns name of recipe
	 *
	 * @return  name of recipe
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns number of matches
	 *
	 * @return  number of matches
	 */
	public int getCount() {
		return matches;
	}

	/**
	 * returns information about the recipe<br>
	 *
	 * note: To use methods getIngredients, getAmounts, and getInstructions, 
	 * this method must be called first
	 *
	 * @return  A string that contains ingredients, amounts, and instructions
	 */
	public String getInfo() throws IOException, FileNotFoundException {
		// if info has already been looked at, then we can just
		// return it but if it has not, we will read the file
		if (info.length() == 0) {
			BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
			info.append("<html><h1>" + name + "</h1>");
			int numIngredients = Integer.parseInt(reader.readLine());
			info.append("<h2>Ingredients</h2><br>");
			info.append("<ul>");
			// Since the user can only edit the recipe when he has clicked here,
			// we initialize ingredients and amounts here
			this.ingredients = new String[numIngredients];
			this.amounts = new String[numIngredients];
			// each iteration of the loop, read two lines and add to ingredients,
			// amounts, and info
			for (int i = 0; i < numIngredients; i++) {
				String ingredient = reader.readLine();
				String amount = reader.readLine();
				this.ingredients[i] = ingredient;
				this.amounts[i] = amount;
				info.append("<li>" + amount + " " + ingredient + "</li>");
			}
			info.append("</ul><br><p>");
			info.append("<h2>Instructions</h2><br><br>");
			String line;
			// just like ingredients and amounts, we only (might) need it now (if ever)
			instructions = new StringBuilder();
			while((line = reader.readLine()) != null) {
				instructions.append(line + '\n');
				info.append(line + "<br>");
			}
			instructions.setLength(instructions.length() - 1);
			info.append("</p></html>");
			reader.close();
		}
		return info.toString();
	}

	/**
	 * deletes the recipe
	 */
	public void delete() {
		fileLocation.delete();
	}

	// really so that I can print it for debugging
	@Override
	public String toString() {
		return name;
	}

	/**
	 * returns the ingredients. <br>
	 * This method must be called after getInfo is called. Otherwise, 
	 * it returns null
	 *
	 * @return  An Array of Strings that are the ingredients
	 */
	public String[] getIngredients() {
		return ingredients;
	}

	/**
	 * returns the amounts. <br>
	 * This method must be called after getInfo is called. Otherwise, 
	 * it returns null
	 *
	 * @return  An Array of Strings that are the amounts
	 */
	public String[] getAmounts() {
		return amounts;
	}

	/**
	 * returns the instructions. <br>
	 * This method must be called after getInfo is called. Otherwise, 
	 * it throws a NullPointerException due to instructions being null
	 *
	 * @return  a String containing the instructions
	 */
	public String getInstructions() {
		// because we want to return a string, not StringBuilder
		return instructions.toString();
	}
}
