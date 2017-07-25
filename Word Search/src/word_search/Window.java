/* This is the Window class that extends JFrame in order to create a popup Window with a Word Search displayed on it.
 * Coded by Christopher Rosenfelt for CSI 213
 */
package word_search;

// Import the necessary libraries/packages
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Window extends JFrame
{
	// Window Constructor
	public Window()
	{
		setTitle("Word Search");
		setSize(800, 1000);
		setDefaultCloseOperation(EXIT_ON_CLOSE);	
	}
	
	// Method that receives a String-type Word Search and adds it to the Window
	// through the usage of JLabel
	public void addText(String puzzle)
	{
		JLabel theLabel = new JLabel(puzzle, JLabel.CENTER);
		add(theLabel);
		setVisible(true);
	}
}// END Window Class
