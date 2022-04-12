package App;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class IngredientSearch extends Screen {

	// stores the ingredient fields
	private ArrayList<JTextField> ingredients;
	// button to remove ArrayList
	private JButton subtract;
	// JPanel to store the ingredients
	private JPanel ingredientPanel;
	// JPanel that stores add and remove buttons
	private JPanel buttons;

	// this ActionListener calls search
	private ActionListener searchListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			search();
		}
	};

	/*
	 * Initializes this object
	 *
	 * @param  parent  the class that created this class
	 */
	public IngredientSearch(SwingThing parent) {
		super(parent);
		init();
	}

	@Override
	void init() {
		// using BorderLayout (like always)
		panel.setLayout(new BorderLayout());
		ingredients = new ArrayList<JTextField>();
		// top is at top border of panel
		JPanel top = new JPanel(new BorderLayout());
		// home button
		JButton home = new JButton("Home");
		// home button goes home when pressed
		home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.home();
			}
		});
		// put home on left border of top
		top.add(home, BorderLayout.WEST);
		// title
		JLabel title = new JLabel("Search by Ingredients", SwingConstants.CENTER);
		Font f = title.getFont();
		// Give title a bold font
		title.setFont(f.deriveFont(Font.BOLD));
		top.add(title, BorderLayout.CENTER);
		// clear button
		JButton clear = new JButton("Clear");
		// clear clears screen when pressed
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearScreen();
			}
		});
		// add clear to top JPanel
		top.add(clear, BorderLayout.EAST);
		// add top to top border of panel
		panel.add(top, BorderLayout.NORTH);

		// serach button
		JButton search = new JButton("Search");
		// search when pressed
		search.addActionListener(searchListener);
		panel.add(search, BorderLayout.SOUTH);

		// body will be in the center of panel
		JPanel body = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		// place body in scroll pane, which allows scrolling
		JScrollPane middle = new JScrollPane(body);
		// add the scroll pane to the panel
		panel.add(middle);
		// add stuff to body
		ingredientPanel = new JPanel();
		ingredientPanel.setLayout(new BoxLayout(ingredientPanel, BoxLayout.Y_AXIS));
		ingredientPanel.add(getIngredientItem());
		body.add(ingredientPanel, gbc);
		buttons = new JPanel(new FlowLayout());
		JButton add = new JButton("+");
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				add();
			}
		});
		subtract = new JButton("-");
		subtract.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				subtract();
			}
		});
		// we do not add subtract button to buttons JPanel because
		// there is only one ingredient so far
		buttons.add(add);
		gbc.gridy = 1;
		body.add(buttons, gbc);
		gbc.gridy = 2;
		gbc.weighty = 1;
		// put remaining vertical space on the bottom
		body.add(Box.createVerticalGlue(), gbc);
	}

	// returns a single JPanel row		ingredient: [		]
	JPanel getIngredientItem() {
		JPanel ingredientItem = new JPanel(new FlowLayout());
		// contains a label and a place for user to enter text
		JLabel label = new JLabel("Ingredient: ");
		ingredientItem.add(label);
		JTextField input = new JTextField(15);
		// when user presses enter, it searches
		// (so user does not have to press button)
		input.addActionListener(searchListener);
		ingredientItem.add(input);
		ingredients.add(input);
		return ingredientItem;
	}

	// method to begin searching (called when search JButton pressed)
	void search() {
		// check whether user input is valid
		for (JTextField text : ingredients) {
			if (text.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(parent.f,
						"One or more ingredients is empty!",
						"Error", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		// HashSet to store the ingredients. The benefit of HashSet is that checking whether
		// the HashSet contains something is really fast
		HashSet<String> ingredientsSet = new HashSet<String>();
		for (JTextField text : ingredients) {
			ingredientsSet.add(text.getText().trim().toLowerCase());
		}
		try {
			// recipes will store the results
			ArrayList<Recipe> recipes = RecipeHelper.searchFilesFromIngredients(ingredientsSet);
			if (recipes.size() == 0) {
				// if no recipe is found, give user hints on what to try, then return,
				// and let user try again
				JOptionPane.showMessageDialog(parent.f, "No Results!\n" +
						"Try adding ALL the ingredients that you have " +
						"and be less specific.\n'Sea salt' only matches " +
						"'Sea salt', but 'salt' can match other " +
						"types of salt as well.\n" +
						"Another thing to try is to include both plural and " +
						"non-plural forms of the ingredients.",
						"No Results", JOptionPane.WARNING_MESSAGE);
				return;
			}
			parent.search(recipes, this);
		} catch (Throwable t) {
			// inform user there is a problem with reading from files
			JOptionPane.showMessageDialog(null,
					"File Error!\n" +
					"Please try again.",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	void add() {
		if (ingredients.size() == 1) {
			buttons.add(subtract);
		}
		ingredientPanel.add(getIngredientItem());
		parent.redraw();
	}
	void subtract() {
		// remove last element
		ingredientPanel.remove(
				ingredientPanel.getComponentCount() - 1
				);
		ingredients.remove(ingredients.size() - 1);
		if (ingredients.size() == 1) {
			buttons.remove(1);
		}
		parent.redraw();
	}
}
