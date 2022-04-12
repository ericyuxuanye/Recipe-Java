package App;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * The screen that shows the edit recipe screen
 */
public class EditRecipeScreen extends Screen {

	public EditRecipeScreen(SwingThing parent) {
		super(parent);
		init();
	}

	// shows name of recipe
	private JLabel name;
	// remembers the name, so the reset button can call
	// edit method
	private String nameString;
	// array that stores ingredients, so we can reset
	private String[] ingredientsArray;
	// array that stores amounts, so we can click reset
	private String[] amountsArray;
	// Remembers the instructions the way it was,
	// so reset button can reset
	private String instructionsString;
	// JPanel of ingredients
	private JPanel ingredientsList;
	// stores current ingredients
	// (ArrayList, not array so we can dynamically add and remove ingredients)
	private ArrayList<JTextField> ingredients;
	// stores current amounts
	private ArrayList<JTextField> amounts;
	// JButton to remove ingredients
	private JButton removeIngredient;
	// JPanel that stores the add and remove ingredient buttons
	private JPanel addRemove;

	// GridBagConstraints for the ingredients.
	// Keeps track of where to put new ingredients
	private GridBagConstraints ingredientConstraints;

	// stores the instructions
	private JTextArea instructions;

	// action that calls edit, which edits recipe.
	// It is used in a JButton and all JTextFields
	private ActionListener writeToFile = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			save();
		}
	};

	@Override
	void init() {
		// initialize ingredients and amounts as new ArrayList
		ingredients = new ArrayList<JTextField>();
		amounts = new ArrayList<JTextField>();

		// panel is a BorderLayout
		panel.setLayout(new BorderLayout());
		JPanel top = new JPanel(new BorderLayout());
		top.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
		// top left is on top left
		JPanel topLeft = new JPanel(new FlowLayout());
		JButton home = new JButton("Home");
		// home goes home when pressed
		home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.home();
			}
		});
		// add home button to top left
		topLeft.add(home, BorderLayout.WEST);
		// back button goes back to recipe info screen
		// when pressed
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.backToRecipePage();
			}
		});
		topLeft.add(back);
		top.add(topLeft, BorderLayout.WEST);
		// title
		JLabel title = new JLabel("Edit Recipe", SwingConstants.CENTER);
		Font f = title.getFont();
		// title has bold font
		title.setFont(f.deriveFont(Font.BOLD));
		top.add(title, BorderLayout.CENTER);
		// top right is on east of top
		JPanel topRight = new JPanel();
		// reset button resets screen
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetScreen();
			}
		});
		// add reset to top right
		topRight.add(reset);
		// add top right to top
		top.add(topRight, BorderLayout.EAST);
		// add top to panel
		panel.add(top, BorderLayout.NORTH);

		// body is another JPanel that uses GridBagLayout
		JPanel body = new JPanel(new GridBagLayout());
		// constraints for body
		GridBagConstraints bodyConstraints = new GridBagConstraints();
		// settings for GridBagConstraings
		bodyConstraints.gridx = 0;
		bodyConstraints.gridy = 0;
		bodyConstraints.weightx = 0.1;
		bodyConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
		bodyConstraints.fill = GridBagConstraints.HORIZONTAL;
		// create a scrollbar that appears vertically as needed
		JScrollPane bodyScroll = new JScrollPane(body,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// addName shows the name. Contains two JLabels, one of which
		// changes based on the recipe that was inputted
		JPanel addName = new JPanel(new FlowLayout(FlowLayout.LEADING));
		addName.add(new JLabel("Name: "));
		name = new JLabel();
		addName.add(name);
		body.add(addName, bodyConstraints);
		// ingredientsList is a separate JPanel so the program will
		// know where to add and remove individual ingredients.
		// This panel stores all the ingredient panels
		ingredientsList = new JPanel(new GridBagLayout());
		ingredientConstraints = new GridBagConstraints();
		ingredientConstraints.anchor = GridBagConstraints.NORTHWEST;
		ingredientConstraints.weightx = 0.1;
		ingredientConstraints.fill = GridBagConstraints.FIRST_LINE_START;
		bodyConstraints.gridy = 1;
		body.add(ingredientsList, bodyConstraints);
		panel.add(bodyScroll, BorderLayout.CENTER);
		// addRemove contains a button that adds and a button that removes
		// a recipe
		addRemove = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JButton addIngredient = new JButton("+");
		// add button adds a new JPanel to ingredientsList where
		// user can input another recipe
		addIngredient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRow();
			}
		});
		addRemove.add(addIngredient);
		// This button removes Recipes
		removeIngredient = new JButton("-");
		removeIngredient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeRow();
			}
		});
		bodyConstraints.gridy = 2;
		body.add(addRemove, bodyConstraints);
		// button that is at the very bottom of the screen.
		// When pressed, it submits the form
		JButton editButton = new JButton("Edit Recipe");
		editButton.addActionListener(writeToFile);
		panel.add(editButton, BorderLayout.SOUTH);
		bodyConstraints.gridy = 3;
		JLabel label = new JLabel("Instructions:");
		body.add(label, bodyConstraints);
		bodyConstraints.gridy = 4;
		bodyConstraints.weighty = 1;
		// fill both vertical and horizontal remaining space
		bodyConstraints.fill = GridBagConstraints.BOTH;
		instructions = new JTextArea(10, 0);
		JScrollPane scrollPane = new JScrollPane(instructions,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// wrap text if line is too long
		instructions.setLineWrap(true);
		// when wrapping text, split by word
		instructions.setWrapStyleWord(true);
		body.add(scrollPane, bodyConstraints);
	}

	// returns a jpanel that shows an ingredient and amount pair. These
	// JPanels are to be put into ingredientsList when the user requires it
	private JPanel getIngredientItem(String ingredient, String amount) {
		// JPanel to be returned
		JPanel ingredientItem = new JPanel(new FlowLayout(FlowLayout.LEADING));
		// label for ingredient
		JLabel ingredientLabel = new JLabel("Ingredient: ");
		ingredientItem.add(ingredientLabel);
		// the field where user inputs ingredient
		JTextField input = new JTextField(15);
		// when user presses enter in JTextField,
		// submit, just like the 'Edit Recipe' button
		input.addActionListener(writeToFile);
		input.setText(ingredient);
		// add to ArrayList as well, in order to query this text field when
		// user submits it
		ingredients.add(input);
		ingredientItem.add(input);
		JLabel amountLabel = new JLabel(" Amount: ");
		// add label for amounts
		ingredientItem.add(amountLabel);
		JTextField amountText = new JTextField(15);
		amountText.setText(amount);
		// when user presses enter in JTextField,
		// submit, just like the 'Edit Recipe' button
		amountText.addActionListener(writeToFile);
		// add to ArrayList as well, in order to query this text field
		// when user submits
		amounts.add(amountText);
		// add to JPanel
		ingredientItem.add(amountText);
		return ingredientItem;
	}

	private JPanel getIngredientItem() {
		JPanel ingredientItem = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JLabel ingredientLabel = new JLabel("Ingredient: ");
		ingredientItem.add(ingredientLabel);
		JTextField input = new JTextField(15);
		input.addActionListener(writeToFile);
		ingredientItem.add(input);
		ingredients.add(input);
		JLabel amountLabel = new JLabel(" Amount: ");
		ingredientItem.add(amountLabel);
		JTextField amountText = new JTextField(15);
		amountText.addActionListener(writeToFile);
		amounts.add(amountText);
		ingredientItem.add(amountText);
		return ingredientItem;
	}

	public void editRecipe(String nameString, String[] ingredientsArray, String[] amountsArray, String instructionsString) {
		// clear ArrayLists
		ingredients.clear();
		amounts.clear();
		// store the arguments in case user wants
		// to reset this form
		this.nameString = nameString;
		this.ingredientsArray = ingredientsArray;
		this.amountsArray = amountsArray;
		this.instructionsString = instructionsString;
		// clear ingredientsList
		ingredientsList.removeAll();
		instructions.setText(instructionsString);
		name.setText(nameString);
		// if there is more than one ingredient,
		// the remove button should appear
		if (ingredientsArray.length > 2) {
			addRemove.add(removeIngredient);
		}
		ingredientConstraints.gridx = 0;
		// for each ingredient in recipe, add it to ingredientsList
		for (int i = 0; i < ingredientsArray.length; i++) {
			ingredientConstraints.gridy = i;
			ingredientsList.add(getIngredientItem(
						ingredientsArray[i],
						amountsArray[i]),
					ingredientConstraints);
		}
	}
	// resets the screen by recalling editRecipe
	private void resetScreen() {
		editRecipe(nameString, ingredientsArray, amountsArray, instructionsString);
	}
	// writes to file
	private void save() {
		// some checks on user input if invalid, we warn the user with a pop up
		// check if ingredients are not empty
		for (JTextField text : ingredients) {
			if (text.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(parent.f, "One or more ingredients is empty!",
						"Error", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		// check that amounts are not empty
		for (JTextField text : amounts) {
			if (text.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(parent.f, "One or more ingredient amounts is empty!",
						"Error", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		// check if instructions is not empty
		if (instructions.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(parent.f, "No instructions!", "Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// if the user did not click the OK button (close or cancel), return and don't do anything
		if (JOptionPane.showConfirmDialog(parent.f, "Are you sure you want to overwrite the recipe?\n",
					"Confirm Overwrite Recipe",
					JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
			return;
		}

		ArrayList<String> ingredientsString = new ArrayList<String>();
		// don't strictly need this, but this prevents the arraylist from unnecessarily copying to a
		// new memory location
		ingredientsString.ensureCapacity(ingredients.size());
		// copy the strings into ingredientsString
		for (JTextField ingredient : ingredients) {
			ingredientsString.add(ingredient.getText().trim());
		}
		// ArrayList that stores amounts as String
		ArrayList<String> amountsString = new ArrayList<String>();
		amountsString.ensureCapacity(amounts.size());
		// change to string and copy to amountsString
		for (JTextField amount : amounts) {
			amountsString.add(amount.getText());
		}

		// try writing recipe to file
		try {
			RecipeHelper.addRecipe(name.getText().trim(), ingredientsString, amountsString, instructions.getText());
			JOptionPane.showMessageDialog(parent.f, "Recipe modified!", "Success!", JOptionPane.INFORMATION_MESSAGE);
			parent.home();
			clearScreen();
		} catch (java.io.IOException e) {
			// if we can't write to file, we tell the user
			JOptionPane.showMessageDialog(parent.f, "Unable to write to file\n" +
					"Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	// adds an ingredient row
	private void addRow() {
		// if the size is 1, we need to add the remove button
		// because it isn't already there
		if (ingredients.size() == 1) {
			addRemove.add(removeIngredient);
		}
		ingredientConstraints.gridy++;
		ingredientsList.add(getIngredientItem(), ingredientConstraints);
		// refresh screen
		parent.redraw();
	}
	// removes an ingredient row
	private void removeRow() {
		// remove from JPanel
		ingredientsList.remove(ingredientsList.getComponentCount() - 1);
		// remove from ArrayLists
		ingredients.remove(ingredients.size() - 1);
		amounts.remove(amounts.size() - 1);
		// if the size is 1, we do not want the remove ingredient button to appear
		if (ingredients.size() == 1) {
			addRemove.remove(1);
		}
		ingredientConstraints.gridy--;
		// refresh screen
		parent.redraw();
	}
}
