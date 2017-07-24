package word_search;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridLayout;

public class Window extends JFrame
{
	public Window(int rows, int columns)
	{
		setTitle("Word Search");
		setSize(1000, 1000);
		setLayout(new GridLayout(rows, columns));
		setDefaultCloseOperation(EXIT_ON_CLOSE);	
	}
	
	public void addText(String puzzle)
	{
		JLabel theLabel = new JLabel(puzzle, JLabel.CENTER);
		add(theLabel);
		setVisible(true);
	}
}// END Window Class
