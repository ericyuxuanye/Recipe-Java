package App;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

// basically stuff that manages the GUI

public class SwingThing implements Runnable {

	// the JFrame
	public JFrame f;

	// results screen
	private RecipeResultsScreen resultsScreen;

	// info screen
	private RecipeInfoScreen recipeInfo;

	// edit recipe screen
	private EditRecipeScreen edit;

	// screens on home page
	private Screen addRecipe;
	private Screen ingredientSearch;
	private Screen nameSearch;

	// the JPanel that shows when f is started
	private JPanel home;

	// stores the search JPanel (can be search by ingredients
	// or search by name)
	private JPanel searchScreen;

	/**
	 * start the GUI.
	 */
	@Override
	public void run() {
		// Create the window
		f = new JFrame("Recipe Finder");
		//minimum size so that it does mess up the look
		f.setMinimumSize(new Dimension(500, 300));
		// preferred size
		f.setPreferredSize(new Dimension(1000, 600));
		// Sets the behavior for when the window is closed
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// home is a JPanel
		home = getHome();
		// the following are Screens
		addRecipe = new AddRecipeScreen(this);
		ingredientSearch = new IngredientSearch(this);
		nameSearch = new NameSearch(this);
		resultsScreen = new RecipeResultsScreen(this);
		recipeInfo = new RecipeInfoScreen(this);
		edit = new EditRecipeScreen(this);
		// home is the panel we show now
		f.setContentPane(home);
		// Arrange the components inside the window
		// By default, the window is not visible. Make it visible.
		f.pack();
		f.setVisible(true);
	}

	/*
	 * returns the home screen JPanel
	 *
	 * @return  the JPanel of the home screen
	 */
	JPanel getHome() {
		// Panel to be returned
		JPanel home = new JPanel();
		// gridlayout
		home.setLayout(new GridLayout(3, 1, 1, 7));

		// buttons in home screen
		JButton addRecipe = new JButton("Add Recipe");
		JButton ingredientSearch = new JButton("Search Recipes by ingredient");
		JButton nameSearch = new JButton("Search Recipes by name");
		Font buttonFont = addRecipe.getFont();
		buttonFont = buttonFont.deriveFont(30f);
		addRecipe.setFont(buttonFont);
		ingredientSearch.setFont(buttonFont);
		nameSearch.setFont(buttonFont);
		// they all use the actionListener in this class
		ingredientSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				f.setContentPane(SwingThing.this.ingredientSearch.panel);
				redraw();
			}
		});

		addRecipe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				f.setContentPane(SwingThing.this.addRecipe.panel);
				redraw();
			}
		});

		nameSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				f.setContentPane(SwingThing.this.nameSearch.panel);
				redraw();
			}
		});

		// add buttons to panel
		home.add(addRecipe);
		home.add(ingredientSearch);
		home.add(nameSearch);
		// return the panel
		return home;
	}

	/**
	 * switches to home screen
	 */
	public void home() {
		f.setContentPane(home);
		redraw();
	}

	/**
	 * redraws frame (when elements are added or deleted)
	 */
	public void redraw() {
		f.revalidate();
		f.repaint();
	}

	/**
	 * Switch to results page that shows the recipe
	 *
	 * @param  recipes  list of recipes to show
	 * @param  caller  the class that called this method
	 */
	public void search(ArrayList<Recipe> recipes, Screen caller) {
		// caller is the object that called it
		searchScreen = caller.panel;
		// sort recipes by relevance
		RecipeHelper.quicksort(recipes);
		// show results
		resultsScreen.showResults(recipes);
		f.setContentPane(resultsScreen.panel);
		redraw();
	}

	/**
	 * Switch to results page.
	 */
	public void backToResults() {
		f.setContentPane(resultsScreen.panel);
		redraw();
	}

	/**
	 * returns to the search page, which may be ingredients or name search.
	 */
	public void toSearch() {
		f.setContentPane(searchScreen);
		redraw();
	}

	/**
	 * Switches to info pane that shows info about given recipe.
	 *
	 * @param   recipe  the recipe object
	 */
	public void showRecipeDetail(Recipe recipe) {
		recipeInfo.showRecipe(recipe);
		f.setContentPane(recipeInfo.panel);
		redraw();
	}

	/**
	 * Switch pane back to recipe info screen
	 */
	public void backToRecipePage() {
		f.setContentPane(recipeInfo.panel);
		redraw();
	}

	/**
	 * Switch to edit JPanel that edits given recipe.
	 */
	public void editRecipe(Recipe recipe) {
		String name = recipe.getName();
		String[] ingredients = recipe.getIngredients();
		String[] amounts = recipe.getAmounts();
		String instructions = recipe.getInstructions();
		// tell edit the recipe we want to edit
		edit.editRecipe(name, ingredients, amounts, instructions);
		f.setContentPane(edit.panel);
		redraw();
	}
}
