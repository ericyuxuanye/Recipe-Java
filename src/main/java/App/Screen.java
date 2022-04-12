package App;

import javax.swing.JPanel;

/**
 * All screens should inherit this
 */
public class Screen {
	/**
	 * the class that called this
	 */
	protected SwingThing parent;

	/**
	 * the panel that SwingThing can access
	 */
	public JPanel panel;

	public Screen(SwingThing parent) {
		this.panel = new JPanel();
		this.parent = parent;
	}

	/**
	 * the init method is useful when we
	 * want to clear the screen.
	 */
	void init() {
	}

	/**
	 * redraws the panel (actually not that useful
	 * since the JFrame should redraw as well)
	 */
	void redraw() {
		panel.revalidate();
	}

	/**
	 * resets the panel
	 */
	void clearScreen() {
		panel.removeAll();
		init();
		parent.redraw();
	}
}
