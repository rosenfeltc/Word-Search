package word_search;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Program 
{
	public static void main(String[] args)
	{
		// Load the puzzle and store it as a String
		String puzzleString = loadPuzzle();
		
		//Figure out how many rows and columns are in the puzzle
		int rows = puzzleRows(puzzleString);
		int columns = puzzleColumns(puzzleString);
		
		// Initialize and load the puzzle
		Puzzle puzzle = new Puzzle(rows, columns);
		puzzle.load(puzzleString);
		//JOptionPane.showMessageDialog(null, puzzle.display());
		
		// Ask the user for a String that they want searched in the puzzle
		String searchWord = JOptionPane.showInputDialog("Please provide a String that you want to search for in the Puzzle: ");
		searchWord = searchWord.toLowerCase();
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
