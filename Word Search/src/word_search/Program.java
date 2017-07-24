/* This is the Program class that contains the main method that is used to run the Word Search program. It allows the user to select
 * the CSV file which is then loaded to a String in order to calculate the number of rows and columns in the CSV file and it passes it
 * to the puzzle class to obtain the solution as a String.
 * Coded by Christopher Rosenfelt for CSI 213
 */
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
		// Open the puzzle and store it as a String
		String puzzleString = loadPuzzle();
		
		//Figure out how many rows and columns are in the puzzle
		int rows = puzzleRows(puzzleString);
		int columns = puzzleColumns(puzzleString);
		
		// Ask the user for how many words they want to search for (maximum is 4)
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
		
		// Ask the user for the words and store them in puzzle.java through the use of the addWord 
		// method for later searching
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
		}//END IF STATEMENT
		
		// Solve the puzzle and store the result in a String
		String solved = puzzle.solve();
		
		// Display the solved puzzle in a Dialog box
		JOptionPane.showMessageDialog(null, solved);
		
		// Display the solved puzzle in a Window
		Window wordSearch = new Window(rows,columns);
		wordSearch.addText(solved);
	}//END MAIN
	
	// Method that allows the user to select and open a CSV File and returns the selected File as a String
	public static String loadPuzzle()
	{
		// Create String that will ultimately store the File
		String fileString = new String();
		
		// Create a JFileChooser object to open the file in openFile
		JFileChooser chooseFile = new JFileChooser();
		chooseFile.showOpenDialog(chooseFile);
		File openFile = chooseFile.getSelectedFile();
		
		// Read the File with Scanner and store the contents of the File as a String
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
	}// END loadPuzzle
	
	// Method that calculates how many rows are in a CSV-type String
	public static int puzzleRows(String puzzleString)
	{
		// Variables needed to count how many rows are in the CSV-type String
		int count = 0;
		int length = puzzleString.length();
		
		// Iterate through every Char in the String while updating
		// counter when a newline Char is found
		for(int i = 0; i < length; i++)
		{
			if(puzzleString.charAt(i) == '\n')
			{
				count++;
			}
		}
		
		return count;
	}// END puzzleRows
	
	// Method that calculates how many columns are in a CSV-type String
	public static int puzzleColumns(String puzzleString)
	{
		// Variables needed to count how many columns are in the CSV-type String
		int count = 0;
		int index = 0;
		
		// Iterate just through the first row and updating count every
		// time a letter is found (ignoring commas)
		while(puzzleString.charAt(index) != '\n')
		{
			if(puzzleString.charAt(index) != ',')
			{
				count++;
			}
			index++;
		}
		
		return count;
	}// End puzzleColumns
}// END Program class
