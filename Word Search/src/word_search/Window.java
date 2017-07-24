package word_search;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Window extends JFrame
{
	public Window()
	{
		setTitle("Word Search");
		setSize(800, 1000);
		setDefaultCloseOperation(EXIT_ON_CLOSE);	
	}
	
	public void addText(String puzzle)
	{
		JLabel theLabel = new JLabel(puzzle, JLabel.CENTER);
		//theLabel.setPreferredSize(new Dimension(800, 800));
		add(theLabel);
		setVisible(true);
	}
}// END Window Class
