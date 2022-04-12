package App;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class RecipeInfoScreen extends Screen {

	// gives information about recipe
	private JLabel body;
	// recipe we are showing information about
	private Recipe recipe;
	/**
	 * constructs a RecipeInfoScreen object
	 *
	 * @param  parent  the caller class
	 */
	public RecipeInfoScreen(SwingThing parent) {
		super(parent);
		// using borderlayout
		panel.setLayout(new BorderLayout());
		// top is on the top
		JPanel top = new JPanel(new BorderLayout());
		// topLeft is on left border (west) of top
		JPanel topLeft = new JPanel(new FlowLayout());
		// home button
		JButton home = new JButton("Home");
		// go home when home button clicked
		home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.home();
			}
		});
		// add home button to topLeft
		topLeft.add(home);
		// back button
		JButton back = new JButton("Back");
		// go back to results page if back button pressed
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.backToResults();
			}
		});
		// add back to topLeft
		topLeft.add(back);
		JLabel title = new JLabel("Recipe Info", SwingConstants.CENTER);
		Font f = title.getFont();
		// make title font bold
		title.setFont(f.deriveFont(Font.BOLD));
		top.add(topLeft, BorderLayout.WEST);
		top.add(title, BorderLayout.CENTER);
		JPanel topRight = new JPanel(new FlowLayout());
		JButton edit = new JButton("Edit");
		// button that when pressed, goes to edit page
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.editRecipe(recipe);
			}
		});
		JButton delete = new JButton("Delete");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteRecipe();
			}
		});
		topRight.add(edit);
		topRight.add(delete);
		top.add(topRight, BorderLayout.EAST);
		// body is a JLabel
		body = new JLabel();
		body.setVerticalAlignment(JLabel.TOP);
		body.setVerticalTextPosition(JLabel.TOP);
		JScrollPane bodyScroll = new JScrollPane(body);
		panel.add(top, BorderLayout.NORTH);
		panel.add(bodyScroll, BorderLayout.CENTER);
	}

	public void showRecipe(Recipe r) {
		recipe = r;
		try {
			// set text to recipe
			body.setText(r.getInfo());
		} catch (java.io.IOException e) {
			// if there is an io exception, tell the user about it
			JOptionPane.showMessageDialog(null,
					"Unable to Fetch Recipe!\n" +
					"Please try again.", "Error",
					JOptionPane.ERROR_MESSAGE);
			// go back to results (so user can try again)
			parent.backToResults();
		}
	}
	void deleteRecipe() {
		// ask user for confirmation, and proceed only if user clicked 'ok'
		if (JOptionPane.showConfirmDialog(null, "Are you sure that you want to delete recipe?",
					"Confirm Delete",
					JOptionPane.OK_CANCEL_OPTION) == 0) {
			// delete recipe
			recipe.delete();
			// tell user recipe has been successfully deleted
			JOptionPane.showMessageDialog(null, "Recipe deleted",
					"info", JOptionPane.INFORMATION_MESSAGE);
			// return to home screen
			parent.home();
		}
	}
}
