package App;

import javax.swing.SwingUtilities;
//import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterIJTheme;

/**
 * Starts the GUI
 * (this is the main class that is loaded when jar file is run)
 */

public class App
{
	public static void main( String[] args )
	{
		/*
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Unable to load look and feel");
		}
		*/
		if (!FlatMaterialLighterIJTheme.install()) {
			System.err.println("Unable to install Look and Feel");
		}
		SwingThing se = new SwingThing();
		// Schedules the application to be run at the correct time in the event queue.
		SwingUtilities.invokeLater(se);
		// create the directory if it doesn't exist
		RecipeHelper.recipeDir.mkdir();
	}
}
