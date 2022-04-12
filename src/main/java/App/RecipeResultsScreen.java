package App;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class RecipeResultsScreen extends Screen {

	// displays the text
	private JPanel body;
	// HashMap so we can quickly look up the recipe given the string
	private HashMap<String, Recipe> hm;
	// when the button is pressed, show the recipe
	// that is found based on button name
	// (using hashmap lookup)
	// This is a class constant so it doesn't have to be created
	// like a million times
	private final ActionListener showResults = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// get name of recipe
			String name = e.getActionCommand();
			// looks up the recipe from string in hashmap
			parent.showRecipeDetail(hm.get(name));
		}
	};

	/**
	 * Create a RecipeResultsScreen object that manages a JPanel
	 *
	 * @param  parent  the object that called this class
	 */
	public RecipeResultsScreen(SwingThing parent) {
		super(parent);
		init();
	}

	@Override
	void init() {
		// use BorderLayout
		panel.setLayout(new BorderLayout());
		// hashmap to give recipe given name
		hm = new HashMap<String, Recipe>();
		// top panel
		JPanel top = new JPanel(new BorderLayout());
		// left side of top panel
		JPanel topLeft = new JPanel(new FlowLayout());
		// home button
		JButton home = new JButton("Home");
		// add home button to topLeft
		topLeft.add(home);
		// go home when home button clicked
		home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.home();
			}
		});
		// back button
		JButton back = new JButton("Back");
		// go back when back button clicked
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.toSearch();
			}
		});
		// add back button to topLeft
		topLeft.add(back);
		// add topLeft to top (on left border)
		top.add(topLeft, BorderLayout.WEST);
		// title that says "Results", center aligned
		JLabel title = new JLabel("Results", SwingConstants.CENTER);
		// font of title
		Font f = title.getFont();
		// set font to bold
		title.setFont(f.deriveFont(Font.BOLD));
		// put title in center
		top.add(title, BorderLayout.CENTER);
		// add top panel to main panel
		panel.add(top, BorderLayout.NORTH);
		// body is a new JPanel that will be in middle
		body = new JPanel(new GridBagLayout());
		// Scroll pane that houses body
		JScrollPane bodyScroll = new JScrollPane(body);
		// add bodyScroll to JPanel
		panel.add(bodyScroll);
	}

	/**
	 * shows the recipes on JFrame as buttons
	 *
	 * @param  recipes  the list of recipes
	 */
	public void showResults(ArrayList<Recipe> recipes) {
		body.removeAll();
		hm.clear();
		// gridBagConstraints object
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 1;
		// button fills all availble horizontal and vertical space
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 0.1;
		gbc.ipady = 1;
		for (int i = 0; i < recipes.size(); i++) {
			// the name of the recipe
			Recipe recipe = recipes.get(i);
			String name = recipe.getName();
			JButton recipeLink = new JButton(name);
			// add to hashmap
			hm.put(name, recipe);
			recipeLink.addActionListener(showResults);
			// update current current gridy to i
			gbc.gridy = i;
			// add button to body
			body.add(recipeLink, gbc);
		}
		// weighty is now 1, so we can take all remaining
		// vertical space
		gbc.weighty = 1;
		// increment gridy
		gbc.gridy++;
		// create a vertical glue that takes up all
		// remaining vertical space
		body.add(Box.createVerticalGlue(), gbc);
		// tell SwingThing to redraw JFrame
		parent.redraw();
	}
}
