// RegNo. 2312089
// Adapted from code by Michael Fairbank

package Assignment;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
	// this class just creates the window that will house the game
	public Component comp;

	public Window(Component comp, String title) {
		super(title);
		this.comp = comp;
		getContentPane().add(BorderLayout.CENTER, comp);
		pack();
		this.setVisible(true);
		this.setResizable(false); // so that the screen width/height can't be changed
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		repaint();
	}
}
