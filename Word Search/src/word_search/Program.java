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
	// Declaring global options variables that will be used in the main method to obtain a selection
	// from the user
	final static String[] OPTIONS = {"Default Puzzle", "Build Puzzle"};
	final static String[] WORD_OPTIONS = {"One", "Two", "Three", "Four"};
	
	public static void main(String[] args)
	{
		// Implementation of the bonus option to allow the user to either
		// search for words in the default puzzle or create a new puzzle
		int option = JOptionPane.showOptionDialog(null, "Would you like to search for words in the default puzzle or build"
				+ " your own puzzle?", "Please Choose an Option", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, OPTIONS, OPTIONS[0]);
		if(option == 0)
		{
			defaultPuzzle();
		}
		else if(option == 1)
		{
			buildPuzzle();
		}
	}// END MAIN
	
	public static void defaultPuzzle()
	{
		// Open the puzzle and store it as a String
		String puzzleString = loadPuzzle();
		
		//Figure out how many rows and columns are in the puzzle
		int rows = puzzleRows(puzzleString);
		int columns = puzzleColumns(puzzleString);
		
		// Initialize, load and display the puzzle
		Puzzle puzzle = new Puzzle(rows, columns);
		puzzle.load(puzzleString);
		String defaultWordSearch = puzzle.toString();
		Window unsolvedWordSearch = new Window();
		unsolvedWordSearch.addText(defaultWordSearch);
		JOptionPane.showMessageDialog(null, "The Word Search is now displayed!\n\nClick OK to begin searching for words");
		
		// Ask the user for how many words they want to search for
		int numberOfWords = JOptionPane.showOptionDialog(null, "How many words do you want to search for?\n\n"
				, "Please Choose an Option", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, WORD_OPTIONS, WORD_OPTIONS[0]);
		
		puzzle.initializeWords(++numberOfWords);
		
		// Ask the user for the words and store them in puzzle.java through the use of the addWord 
		// method for later searching
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
			
		// Solve the puzzle and store the result in a String
		String solved = puzzle.solve();
			
		// Display the solved puzzle in a Window
		Window solvedWordSearch = new Window();
		solvedWordSearch.addText(solved);
		JOptionPane.showMessageDialog(null, "The words that have been found are highlighted!");
	}//END defaultPuzzle
	
	public static void buildPuzzle()
	{
		int numberOfWords = JOptionPane.showOptionDialog(null, "We are going to build a 20x20 Word Search puzzle!\n\n"
				+ "How many words do you want it to contain?", "Please Choose an Option", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, WORD_OPTIONS, WORD_OPTIONS[0]);
		
		//Build Puzzle Constructor
		BuildPuzzle wordSearch = new BuildPuzzle(++numberOfWords);
		
		// Ask the user for the words that should be in the puzzle and store them in the BuildPuzzle class 
		// through the use of the addWord method for later usage
		if(numberOfWords == 1)
		{
				String word = JOptionPane.showInputDialog("Please provide the word that you want in your Word Search: ");
				word = word.toLowerCase();
				wordSearch.addWord(word, numberOfWords);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please provide the words that you want to search for in the Puzzle: ");
			for (int i = 1; i <= numberOfWords; i++)
			{
				String word = JOptionPane.showInputDialog("Please provide word number " + i + ": ");
				word = word.toLowerCase();
				wordSearch.addWord(word, i);
			}
		}
		
		// Create the puzzle and then display
		wordSearch.create();
		wordSearch.toString();
	}//END buildPuzzle
	
	// Method that allows the user to select and open a CSV File and returns the selected File as a String
	public static String loadPuzzle()
	{
		// Create String that will ultimately store the File
		String fileString = new String();
		
		// Create a JFileChooser object to open the file in openFile
		JOptionPane.showMessageDialog(null, "Please choose the CSV-type Word Search puzzle from the correct directory");
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
