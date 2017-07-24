package word_search;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Program extends JFrame
{
	public static void main(String[] args)
	{
		// Load the puzzle and store it as a String
		String puzzleString = loadPuzzle();
		
		//Figure out how many rows and columns are in the puzzle
		int rows = puzzleRows(puzzleString);
		int columns = puzzleColumns(puzzleString);
		
		// Ask the user for how many words they want to search for - maximum is 4
		int numberOfWords;
		do
		{
			numberOfWords = Integer.parseInt(JOptionPane.showInputDialog("How many words would you like to search for in the puzzle (Maximum is 4): "));
			if(numberOfWords < 0 || numberOfWords > 4)
			{
				JOptionPane.showMessageDialog(null, "Invalid number entered! Please enter an integer from 0 to 4");
			}
			
		} while(numberOfWords < 0 || numberOfWords > 4);
			
		// Initialize and load the puzzle
		Puzzle puzzle = new Puzzle(rows, columns, numberOfWords);
		puzzle.load(puzzleString);
		
		// Ask the user for the words
		if(numberOfWords != 0)
		{
			if(numberOfWords == 1)
			{
				String word = JOptionPane.showInputDialog("Please provide the word that you want to search for in the Puzzle: ");
				word = word.toLowerCase();
				puzzle.addWord(word, numberOfWords);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Please provide the words that you want to search for in the Puzzle: ");
				for (int i = 1; i <= numberOfWords; i++)
				{
					String word = JOptionPane.showInputDialog("Please provide word number " + i + ": ");
					word = word.toLowerCase();
					puzzle.addWord(word, i);
				}
			}
		}
		
		// Solve the puzzle
		String solved = puzzle.solve();
		
		JOptionPane.showMessageDialog(null, solved);
	}
	
	// Method that allows the user to load a puzzle and returns the selected File as a String
	public static String loadPuzzle()
	{
		String fileString = new String();
		
		JFileChooser chooseFile = new JFileChooser();
		chooseFile.showOpenDialog(chooseFile);
		File openFile = chooseFile.getSelectedFile();
		
		try
		{
			Scanner fileScanner = new Scanner(openFile);
			while(fileScanner.hasNextLine())
			{
				fileString += fileScanner.nextLine();
				fileString += "\n";
			}
			fileScanner.close();
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load the file! Exiting program...");
			System.exit(0);
		}
		
		return fileString;
	}
	
	// Method that calculates how many rows are in a CSV-type String
	public static int puzzleRows(String puzzleString)
	{
		int count = 0;
		int length = puzzleString.length();
		
		for(int i = 0; i < length; i++)
		{
			if(puzzleString.charAt(i) == '\n')
			{
				count++;
			}
		}
		
		return count;
	}
	
	// Method that calculates how many columns are in a CSV-type String
	public static int puzzleColumns(String puzzleString)
	{
		int count = 0;
		int index = 0;
		
		while(puzzleString.charAt(index) != '\n')
		{
			if(puzzleString.charAt(index) != ',')
			{
				count++;
			}
			index++;
		}
		
		return count;
	}
}
