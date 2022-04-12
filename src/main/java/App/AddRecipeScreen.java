package App;

import java.awt.BorderLayout;
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

// returns the JPanel that represents
// the add recipe screen. This class will make it easier
// to manage the multiple screens
public class AddRecipeScreen extends Screen {

	// holds the name JTextField
	private JTextField name;
	// JPanel that holds ingredients. We need access to this
	// so we can dynamically add or remove ingredients
	private JPanel ingredientsList;
	// constraints for the ingredients panel
	private GridBagConstraints ingredientConstraints;

	// ingredients list, so we can use this to write to file
	private ArrayList<JTextField> ingredients;
	// amounts list, so we can use this to write to file
	private ArrayList<JTextField> amounts;
	// button to remove ingredient
	private JButton removeIngredient;
	// JPanel that contains the removeIngredient button
	private JPanel addRemove;

	// holds instructions JTextArea
	private JTextArea instructions;

	// action that calls add, which adds recipe.
	// It is used in a JButton and all JTextFields
	private ActionListener writeToFile = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			save();
		}
	};

	public AddRecipeScreen(SwingThing parent) {
		super(parent);
		init();
	}

	@Override
	void init() {
		// initialize ingredients and amounts
		ingredients = new ArrayList<JTextField>();
		amounts = new ArrayList<JTextField>();
		// use BorderLayout for panel
		panel.setLayout(new BorderLayout());
		JPanel top = new JPanel(new BorderLayout());
		// home button
		JButton home = new JButton("Home");
		// home goes home whien pressed
		home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.home();
			}
		});
		top.add(home, BorderLayout.WEST);
		// title, which is center-aligned
		JLabel title = new JLabel("Add Recipe", SwingConstants.CENTER);
		// bold title
		Font f = title.getFont();
		title.setFont(f.deriveFont(Font.BOLD));
		// add title to top JPanel
		top.add(title, BorderLayout.CENTER);
		JButton clear = new JButton("Clear");
		// clear button clears screen when pressed
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearScreen();
			}
		});
		top.add(clear, BorderLayout.EAST);
		panel.add(top, BorderLayout.NORTH);

		// body uses GridBagLayout
		JPanel body = new JPanel(new GridBagLayout());
		// used in body
		GridBagConstraints bodyConstraints = new GridBagConstraints();
		bodyConstraints.gridx = 0;
		bodyConstraints.gridy = 0;
		bodyConstraints.weightx = 0.1;
		bodyConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
		bodyConstraints.fill = GridBagConstraints.HORIZONTAL;
		// scrollbar with only vertical scrolling as needed
		JScrollPane bodyScroll = new JScrollPane(body,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JPanel addName = new JPanel(new FlowLayout(FlowLayout.LEADING));
		addName.add(new JLabel("Name: "));
		name = new JTextField(15);
		name.addActionListener(writeToFile);
		addName.add(name);
		body.add(addName, bodyConstraints);
		ingredientsList = new JPanel();
		//ingredientsList.setLayout(new BoxLayout(ingredientsList, BoxLayout.Y_AXIS));
		ingredientsList.setLayout(new GridBagLayout());
		ingredientConstraints = new GridBagConstraints();
		ingredientConstraints.gridx = 0;
		ingredientConstraints.gridy = 0;
		ingredientConstraints.anchor = GridBagConstraints.NORTHWEST;
		ingredientConstraints.weightx = 0.1;
		ingredientConstraints.fill = GridBagConstraints.FIRST_LINE_START;
		ingredientsList.add(getIngredientItem(), ingredientConstraints);
		bodyConstraints.gridy = 1;
		body.add(ingredientsList, bodyConstraints);
		panel.add(bodyScroll, BorderLayout.CENTER);
		addRemove = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JButton addIngredient = new JButton("+");
		// + adds ingredients
		addIngredient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRow();
			}
		});
		addRemove.add(addIngredient);
		removeIngredient = new JButton("-");
		// - removes ingredients
		removeIngredient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeRow();
			}
		});
		bodyConstraints.gridy = 2;
		body.add(addRemove, bodyConstraints);
		JButton add = new JButton("Add Recipe");
		// this button adds the recipe (by calling add method)
		add.addActionListener(writeToFile);
		panel.add(add, BorderLayout.PAGE_END);
		bodyConstraints.gridy = 3;
		// instructions
		JLabel label = new JLabel("Instructions:");
		body.add(label, bodyConstraints);
		bodyConstraints.gridy = 4;
		bodyConstraints.weighty = 1;
		bodyConstraints.fill = GridBagConstraints.BOTH;
		// user types instructions in JTextArea
		instructions = new JTextArea(10, 0);
		JScrollPane scrollpane = new JScrollPane(instructions,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// lineWrap so it goes to new line if the line overflows
		instructions.setLineWrap(true);
		// wrap by whole words
		instructions.setWrapStyleWord(true);
		// add to body
		body.add(scrollpane, bodyConstraints);
	}

	// generates an ingredient row that contains ingredient and amount
	private JPanel getIngredientItem() {
		JPanel ingredientItem = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JLabel ingredientLabel = new JLabel("Ingredient: ");
		ingredientItem.add(ingredientLabel);
		JTextField input = new JTextField(15);
		input.addActionListener(writeToFile);
		ingredients.add(input);
		ingredientItem.add(input);
		JLabel amountLabel = new JLabel(" Amount: ");
		ingredientItem.add(amountLabel);
		JTextField amount = new JTextField(15);
		// when user presses enter, it automatically searches
		amount.addActionListener(writeToFile);
		amounts.add(amount);
		ingredientItem.add(amount);
		return ingredientItem;
	}

	private void save() {
		// some checks on user input
		// check if the name is empty, if it is, show a popup, return and do nothing more
		if (name.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(parent.f, "Empty name!", "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		// check if the name contains invalid characters
		if (!RecipeHelper.isValidName(name.getText().trim())) {
			JOptionPane.showMessageDialog(parent.f, "Invalid Recipe Name!\n"
					+ "Recipe name cannot contain special characters such as:\n"
					+ "'?', '<', '>', '/', or '*'", "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		// check if any ingredients are empty by looping through ingredientss
		for (JTextField text : ingredients) {
			if (text.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(parent.f, "One or more ingredients is empty!",
						"Error", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		// check if any amounts are empty by looping through amounts
		for (JTextField text : amounts) {
			if (text.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(parent.f, "One or more ingredient amounts is empty!",
						"Error", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		// check if instructions is empty
		if (instructions.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(parent.f, "No instructions!", "Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// check if the recipe already exists. if it does, ask user whether they want to overwrite.
		// If user does not click 'ok', exit from this method.
		if (RecipeHelper.checkFileExists(name.getText().trim())) {
			if (JOptionPane.showConfirmDialog(parent.f, "A recipe with the same name already exists. Overwrite?",
						"Recipe already exists!",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE) != JOptionPane.OK_OPTION) {
				return;
			}
		}

		// convert ArrayList of JTextArea to ArrayList of Strings
		ArrayList<String> ingredientsString = new ArrayList<String>();
		// allocates enough space to store all the elements, so that the
		// ArrayList will not have to be resized
		ingredientsString.ensureCapacity(ingredients.size());
		// copy the strings from the JTextAreas in ingredients to ingredientsString
		for (JTextField ingredient : ingredients) {
			ingredientsString.add(ingredient.getText().trim());
		}
		// convert ArrayList of JTextArea to ArrayList of Strings
		ArrayList<String> amountsString = new ArrayList<String>();
		amountsString.ensureCapacity(ingredients.size());
		// copy the strings from JTextAreas in amounts to amountsString
		for (JTextField amount : amounts) {
			amountsString.add(amount.getText());
		}
		try {
			// write recipe to file
			RecipeHelper.addRecipe(name.getText().trim(), ingredientsString, amountsString, instructions.getText());
			JOptionPane.showMessageDialog(parent.f, "Recipe added!", "Success!", JOptionPane.INFORMATION_MESSAGE);
			parent.home();
			clearScreen();
		} catch (java.io.IOException e) {
			// if there is an error for IO, tell the user about it
			JOptionPane.showMessageDialog(parent.f, "Unable to write to file\n" +
					"Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void addRow() {
		// if the size is 1, we need to add the remove button
		// because it isn't already there
		if (ingredients.size() == 1) {
			addRemove.add(removeIngredient);
		}
		// increment gridy (because it's on the next line)
		ingredientConstraints.gridy++;
		ingredientsList.add(getIngredientItem(), ingredientConstraints);
		parent.redraw();
	}
	private void removeRow() {
		// remove last element from ingredientsList
		ingredientsList.remove(ingredientsList.getComponentCount() - 1);
		// remove last element from ArrayLists
		ingredients.remove(ingredients.size() - 1);
		amounts.remove(amounts.size() - 1);
		// if the size is 1, we do not want the remove ingredient button to appear
		if (ingredients.size() == 1) {
			// remove button is at index 1
			addRemove.remove(1);
		}
		// subtract y because we removed an element
		ingredientConstraints.gridy--;
		parent.redraw();
	}
}
