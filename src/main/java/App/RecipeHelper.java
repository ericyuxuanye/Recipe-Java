package App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * contains algorithms, file readers, and stuff to help recipe finding and searching and sorting
 */

public class RecipeHelper {
	/**
	 * the directory that the program writes to.<br>
	 * WARNING: This program writes to YOUR user directory (Because where else would I write to?)<br>
	 * DELETE the directory ~/.recipes/ as well when you delete this program
	 */
	public final static String recipesPath = System.getProperty("user.home") +
		File.separator + ".recipes/";
	/**
	 * The directory as a file
	 */
	public final static File recipeDir = new File(recipesPath);

	/**
	 * helper method to add a recipe
	 */
	public static void addRecipe(String name, ArrayList<String> ingredients,
			ArrayList<String> amounts, String procedure) throws IOException {
		PrintWriter out = new PrintWriter(recipesPath + name + ".txt");
		// write to file number of ingredients
		out.println(ingredients.size());
		// write ingredient and amount, alternating
		for (int i = 0; i < ingredients.size(); i++) {
			// write ingredient
			out.println(ingredients.get(i));
			// write amount
			out.println(amounts.get(i));
		}
		// write procedure
		out.println(procedure);
		// close to prevent resource leak (and to flush)
		out.close();
	}

	// method to swap two integers in array
	private static void swap(ArrayList<Recipe> recipes, int a, int b) {
		Recipe temp = recipes.get(a);
		recipes.set(a, recipes.get(b));
		recipes.set(b, temp);
	}

	/**
	 * sort the recipes (most matches first) using getCount()
	 *
	 * @param  recipes  the recipes that need to be sorted
	 */
	public static void quicksort(ArrayList<Recipe> recipes) {
		// call the method that does the sorting,
		// with start as 0 and stop as last index
		quicksortHelper(recipes, 0, recipes.size() - 1);
	}

	// does the real sorting
	private static void quicksortHelper(ArrayList<Recipe> recipes, int start, int stop) {
		// if start < stop so array size is at least 2
		if (start < stop) {
			// partition array, and pi is pivot element
			int pi = partition(recipes, start, stop);
			// recursive calls with smaller subarrays without pivot
			// (since pivot is already in correct spot
			quicksortHelper(recipes, start, pi - 1);
			quicksortHelper(recipes, pi + 1, stop);
		}
	}
	// helps partition for quicksort
	// (puts elements larger on left and smaller on right of pivot)
	private static int partition(ArrayList<Recipe> recipes, int start, int stop) {
		// pivot would be the last element
		Recipe pivot = recipes.get(stop);
		// slow iterator
		int i = start - 1;
		// j is fast iterator
		for (int j = start; j < stop; j++) {
			// if element at j is greater than element at pivot
			if (recipes.get(j).getCount() > pivot.getCount()) {
				// increment i
				i++;
				// swap elements at i and j
				swap(recipes, i, j);
			}
		}
		// swap pivot with element at i+1
		swap(recipes, i + 1, stop);
		// return pivot index
		return i + 1;
	}

	public static boolean checkFileExists(String name) {
		File file = new File(recipesPath + name + ".txt");
		if (file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * checks if a recipe name can be stored as a file (mostly for Windows because linux and Mac doesn't care except for '/' and NUL)
	 */
	public static boolean isValidName(String name) {
		for (int i = 0; i < name.length(); i++) {
			switch (name.charAt(i)) {
				/* FALLTHROUGH */
				case '\0': // IDK how someone would type this character (maybe copy paste?), but just in case
				case '?':
				case '<':
				case '>':
				case '"':
				case '/':
				case '\\':
				case ':':
				case '|':
				case '*':
					return false;
			}
		}
		return true;
	}

	/**
	 * method to search files from the ingredients
	 *
	 * @param  ingredients  the ingredients the user has
	 * @return the Recipes that match
	 */
	public static ArrayList<Recipe> searchFilesFromIngredients(HashSet<String> ingredients)
			throws IOException, FileNotFoundException {
		// specify the directory we search
		final File directory = new File(recipesPath);
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		// loop through files in directory
loop:
		for (File recipe : directory.listFiles()) {
			BufferedReader reader = new BufferedReader(new FileReader(recipe));
			int numIngredients = Integer.parseInt(reader.readLine());
			for (int i = 0; i < numIngredients; i++) {
				// read line and that is our ingredient
				String ingredient = reader.readLine().toLowerCase();
				// skip next line because this tells us the amount and we don't need that
				reader.readLine();
				// try to match whole ingredient
				if (ingredients.contains(ingredient)) {
					// if matched, continue to next ingredient
					continue;
				}
				// if not matched, see if the last word matchs so if user has salt,
				// and recipe has sea salt, it will still match
				String last = ingredient.substring(ingredient.lastIndexOf(' ') + 1);
				if (!ingredients.contains(last)) {
					// the user does not have this ingredient. go to next recipe
					// (continue outer loop)
					continue loop;
				}
				// we matched the last word, which still counts
			}
			// add the recipe because it matches, with numIngredients as the relevance
			Recipe match = new Recipe(recipe.getName(), numIngredients);
			// add to ArrayList
			recipes.add(match);
			// close reader to prevent leaks
			reader.close();
		}
		// return the ArrayList
		return recipes;
	}
	// checks if string b exists in string a
	private static boolean contains(String a, String b) {
		int lenA = a.length();
		int lenB = b.length();
		if (lenB > lenA) {
			return false;
		}
		if (lenB == 0) {
			return true;
		}
		char firstB = b.charAt(0);
		for (int i = 0; i < lenA; i++) {
			if (a.charAt(i) != firstB) {
				while (++i < lenA && a.charAt(i) != firstB);
			}
			if (i < lenA) {
				int j = i + 1;
				int end = j + lenB - 1;
				for (int k = 1; j < end && a.charAt(j) == b.charAt(k); j++, k++);
				if (j == end) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * method to return search recipes from keywords
	 *
	 * @param  keywords  the keywords to search
	 * @return an ArrayList of Recipes
	 */
	public static ArrayList<Recipe> searchFilesFromName(String[] keywords) {
		final File directory = new File(recipesPath);
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		Trie kTrie = new Trie(keywords);
		// loop through files in directory
		for (File recipe : directory.listFiles()) {
			String name = recipe.getName().toLowerCase();
			name = name.substring(0, name.length() - 4);
			int matchedKeywords = 0;
			// if the filename contains any keywords, add it
			/*
			for (String keyword : keywords) {
				if (contains(name, keyword)) {
					matchedKeywords++;
				}
			}
			*/
			for (String keyword : name.split("\\s+")) {
				if (kTrie.contains(keyword)) {
					matchedKeywords++;
				}
			}
			if (matchedKeywords > 0) {
				// any recipe with more than one keyword that matches
				// gets put in ArrayList
				Recipe match = new Recipe(recipe.getName(), matchedKeywords);
				recipes.add(match);
			}
		}
		return recipes;
	}
}
