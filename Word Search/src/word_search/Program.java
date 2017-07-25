/* This is the Program class that contains the main method that is used to run the Word Search program. It allows the user to choose between
 * loading their own CSV-type Word Search File or creating a new CSV-type Word Search. If loading a Word Search, it mostly uses the methods
 * in the Puzzle class while if creating a Word Search it mostly uses the methods in the BuildPuzzle class. There are also some static methods
 * in this class to help with either loading or creating a Word Search and both selections use the Window class for displaying the Word Search.
 * Coded by Christopher Rosenfelt for CSI 213
 */
package word_search;

// Import the necessary libraries/packages
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Program
{
	// Declaring global variables that will be used to obtain selections from the user
	final static String[] OPTIONS = {"Load", "Create"};
	final static String[] WORD_OPTIONS = {"One", "Two", "Three", "Four"};
	final static String[] YES_OR_NO = {"Yes", "No"};
	
	// Main method of the program, gives the user the option to either load a Word Search
	// or create a new one
	public static void main(String[] args)
	{
		// Implementation of the bonus option to allow the user to either
		// load a CSV-type Word Search of create their own Word Search
		int option = JOptionPane.showOptionDialog(null, "Would you like to load or create a Word Search?", "Please Choose an Option",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, OPTIONS, OPTIONS[0]);
		
		// Loading a Word Search 
		if(option == 0)
		{
			defaultPuzzle();
		}
		// Create a Word Search
		else
		{
			buildPuzzle();
		}
	}// END MAIN
	
	// Method that handles the 'broad picture' operations that come with the option of loading the Word Search
	public static void defaultPuzzle()
	{
		// Open the puzzle and store it as a String
		String puzzleString = loadPuzzle();
		
		//Figure out how many rows and columns are in the puzzle
		int rows = puzzleRows(puzzleString);
		int columns = puzzleColumns(puzzleString);
		
		// Initialize, load and display the puzzle in a new Window
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
		
		// ++numberOfWords because showOptionDialog returns one less than the option selected 
		//so need to add it back before calling the method
		puzzle.initializeWords(++numberOfWords);
		
		// Ask the user for the words and store them in puzzle.java through the use of the addWord 
		// method for later searching
		if(numberOfWords == 1)
		{
			String word = JOptionPane.showInputDialog("Please provide the word that you want to search for: ");
			// Make it lower case to match Word Search
			word = word.toLowerCase();
			puzzle.addWord(word, numberOfWords);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please provide the words that you want to search for: ");
			for (int i = 1; i <= numberOfWords; i++)
			{
				String word = JOptionPane.showInputDialog("Please provide word number " + i + ": ");
				// Make it lower case to match Word Search
				word = word.toLowerCase();
				puzzle.addWord(word, i);
			}
		}
			
		// Solve the puzzle and store the result in a String
		String solved = puzzle.solve();
			
		// Display the solved puzzle in a new Window
		Window solvedWordSearch = new Window();
		solvedWordSearch.addText(solved);
		JOptionPane.showMessageDialog(null, "The words that have been found are highlighted!");
	}//END defaultPuzzle
	
	// Method that handles the 'broad picture' operations that come with the option of building a new Word Search
	public static void buildPuzzle()
	{
		int numberOfWords = JOptionPane.showOptionDialog(null, "We are going to build a 20x20 Word Search!\n\n"
				+ "How many words do you want it to contain?", "Please Choose an Option", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, WORD_OPTIONS, WORD_OPTIONS[0]);
		
		// Build Puzzle Constructor
		BuildPuzzle wordSearch = new BuildPuzzle(++numberOfWords);
		
		// Ask the user for the words that should be in the puzzle and store them in the BuildPuzzle class 
		// through the use of the addWord method for later usage
		if(numberOfWords == 1)
		{
				String word = JOptionPane.showInputDialog("Please provide the word that you want in your Word Search: ");
				// Make it lower case to match Word Search
				word = word.toLowerCase();
				wordSearch.addWord(word, numberOfWords);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please provide the words that you want in your Word Search: ");
			for (int i = 1; i <= numberOfWords; i++)
			{
				String word = JOptionPane.showInputDialog("Please provide word number " + i + ": ");
				// Make it lower case to match Word Search
				word = word.toLowerCase();
				wordSearch.addWord(word, i);
			}
		}
		
		// Create the puzzle and then display in new Window
		wordSearch.create();
		String newPuzzle = wordSearch.toString();
		Window newWindow = new Window();
		newWindow.addText(newPuzzle);
		
		// Ask the user if they want to save their newly created Word Search
		int selection = JOptionPane.showOptionDialog(null, "Your new Word Search is displayed!\n\n"
				+ "Would you like to save it as a CSV File?", "Please Choose an Option", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, YES_OR_NO, YES_OR_NO[0]);
		
		// Save as a CSV File if user clicked on yes		
		if(selection == 0)
		{
			savePuzzle(wordSearch);
		}
		
		// Ask the user if they want the word search solved
		selection = JOptionPane.showOptionDialog(null, "Would you like me to find your words in the Word Search?", "Please Choose an Option",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, YES_OR_NO, YES_OR_NO[0]);
		
		// User selected option to solve the word search
		if(selection == 0)
		{
			String solvedPuzzle = wordSearch.solvedToString();
			Window solvedWindow = new Window();
			solvedWindow.addText(solvedPuzzle);
			JOptionPane.showMessageDialog(null, "The words are now highlighted!");
		}
		
		
	}//END buildPuzzle
	
	// Method that allows the user to select and open a CSV File and returns the selected File as a String
	public static String loadPuzzle()
	{
		// Create String that will ultimately store the File
		String fileString = new String();
		
		// Create a JFileChooser object to open the file in openFile
		JOptionPane.showMessageDialog(null, "Please choose the CSV-type Word Search from the correct directory");
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
	
	// Method that saves a newly created Word Search as a CSV File
	public static void savePuzzle(BuildPuzzle wordSearch)
	{
		// Have the user save the file in their directory
		JOptionPane.showMessageDialog(null,"Please give the File a name and choose where to save it.");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.showSaveDialog(fileChooser);
		File saveFile = fileChooser.getSelectedFile();
		
		// Adjusting the user's saved name with the .csv extension at the end
		String tempName = saveFile.toString();
		tempName += ".csv";
		saveFile = new File(tempName);
		
		// Create a CSV-type string of the created Word Search
		String csvWordSearch = wordSearch.createCSV();
		
		// Use PrinterWriter to use the csv-type String to write into the saved File
		try 
		{
			PrintWriter theWriter = new PrintWriter(saveFile);
			theWriter.print(csvWordSearch);
			theWriter.close();
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Error Writing to file");
		}
	}
	
	// Method that calculates how many rows are in a CSV-type String
	public static int puzzleRows(String puzzleString)
	{
		// Variables needed to count how many rows are in the CSV-type String
		int count = 0;
		int length = puzzleString.length();
		
		// Iterate through every char in the String while updating
		// counter when a newline char is found (represents a new row)
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
		
		// Iterate just through the first row and update count (represents a new column)
		// every time a letter is found (ignoring commas) 
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