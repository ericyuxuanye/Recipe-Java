package App;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NameSearch extends Screen {
	private JTextField query;
	private JButton search;

	public NameSearch(SwingThing parent) {
		super(parent);
		init();
	}

	@Override
	void init() {
		panel.setLayout(new BorderLayout());
		// the top panel
		JPanel top = new JPanel(new BorderLayout());
		JButton home = new JButton("Home");
		// when clicked, (obviously) go home
		home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.home();
			}
		});
		// home button on top left
		top.add(home, BorderLayout.WEST);
		JLabel title = new JLabel("Search For Recipes By Name", SwingConstants.CENTER);
		Font f = title.getFont();
		title.setFont(f.deriveFont(Font.BOLD));
		top.add(title, BorderLayout.CENTER);
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearScreen();
			}
		});
		top.add(clear, BorderLayout.EAST);
		panel.add(top, BorderLayout.NORTH);

		// the middle panel
		JPanel body = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		// action to start search
		ActionListener goSearch = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				search();
			}
		};
		query = new JTextField(15);
		// BIG font
		query.setFont(query.getFont().deriveFont(40f));
		// pressing enter while in JTextField will search
		query.addActionListener(goSearch);
		body.add(query, gbc);
		search = new JButton("Search");
		search.setEnabled(false);
		search.setPreferredSize(new Dimension(200, 50));
		search.addActionListener(goSearch);
		body.add(search, gbc);
		panel.add(body, BorderLayout.CENTER);
		query.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (query.getDocument().getLength() == 0) {
					search.setEnabled(false);
				}
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (query.getDocument().getLength() > 0 && !search.isEnabled()) {
					search.setEnabled(true);
				}
			}
		});
	}

	// search given the query
	void search() {
		// check if search box is empty
		/*
		if (query.getText().length() == 0) {
			JOptionPane.showMessageDialog(parent.f,
					"Search box empty!", "Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		*/
		// split by keywords
		ArrayList<Recipe> recipes = RecipeHelper.searchFilesFromName(
				query.getText().toLowerCase().split(" ", -1));
		// check if there are any results
		if (recipes.size() == 0) {
			JOptionPane.showMessageDialog(parent.f,
					"No Results!\n" +
					"Hint: Try adding more keywords", "Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// switch to other screen
		parent.search(recipes, this);
	}
}
