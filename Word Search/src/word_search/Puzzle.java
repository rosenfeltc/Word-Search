/* This is the Puzzle class that stores all the variables and performs all of the methods related to the Word Search puzzle:
 * from loading the Puzzle into an array of Chars, storing the words provided by the user, to searching those words in the puzzle and
 * highlighting them in the final solution returned as a String to the program class.
 * Coded by Christopher Rosenfelt for CSI 213
 */
package word_search;

public class Puzzle 
{
	// Private fields that will be useful to the different methods in this class
	private char[][] puzzle;
	private int[][] solvedPuzzle;
	private String[] words;
	private int numberOfWords;
	private int rows;
	private int columns;
	private int[][] locator;
	private final String[] colors;
	
	// Puzzle constructor that takes an integer for the number of rows,
	// an integer for the number of columns
	public Puzzle(int r, int c)
	{
		rows = r;
		columns = c;
		numberOfWords = 0;
		puzzle = new char[rows][columns];
		solvedPuzzle = new int[rows][columns];
		
		// Add the colors that will be used for highlighting the found words
		colors = new String[5];
		colors[0] = "<TD BGCOLOR =#0000FF><FONT SIZE=6><CENTER>"; // blue
		colors[1] = "<TD BGCOLOR =#FF0000><FONT SIZE=6><CENTER>"; // red
		colors[2] = "<TD BGCOLOR =#00FF00><FONT SIZE=6><CENTER>"; // green
		colors[3] = "<TD BGCOLOR =#FFA500><FONT SIZE=6><CENTER>"; // orange
		colors[4] = "<TD BGCOLOR =#FFFF00><FONT SIZE=6><CENTER>"; // yellow
	}// END Constructor
	
	// Method that loads the puzzle into a 2D char array from a string
	public void load(String puzzleString)
	{
		// Declare and initialize the variables that will be needed to iterate
		// through the String while storing it into a 2D char array
		int index = 0;
		int i = 0;
		int j = 0;
		int length = puzzleString.length();
		
		// Iterate through every char in the String, storing all the 'letters' into the 2D char puzzle
		// while appropriately adjusting the previously declared variables when encountering a comma
		// or a newline escape char
		while(index < length)
		{
			// Ignore commas so as to not place them in the 2D char array
			if(puzzleString.charAt(index) == ',')
			{
				j++;
				index++;
			}
			// Do not place the newline char into the 2D array, instead update i and j
			// as if we are moving into a new row
			else if(puzzleString.charAt(index) == '\n')
			{
				i++;
				j = 0;
				index++;
			}
			// Place the current char into the 2D char array
			else
			{
				puzzle[i][j] = puzzleString.charAt(index);
				index++;
			}
		}
	}// END load
	
	// Method that initializes the fields that are important to find the words that will be inputted by the user
	public void initializeWords(int number)
	{
		numberOfWords = number;
		// number + plus 1 is the size because later on we will be 
		// storing words in their appropriate index so 0 index will be null
		words = new String[number + 1];
		
	}
	
	// Method that essentially stores the words provided by the user in
	// the main method of Program.java into a String array
	public void addWord(String word, int index)
	{
		words[index] = word;
	}// END addWord
	
	// Method that will ultimately solve the word search by returning a string
	// containing the word search with the found words highlighted. Uses other
	// methods from this class in order to accomplish it.
	public String solve()
	{
		// Iterate through all the provided words from the user and
		// find them in the puzzle
		for(int i = 1; i <= numberOfWords; i++)
		{
			// Use the search Method to find location of the word
			// if found and return true if found or false if not
			if(search(words[i]))
			{	
				// If word is found, add it to solvedPuzzle
				addToPuzzle(i);
			}
		}
		// Obtain the puzzle solution as a String
		String solution = solvedToString();
		
		return solution;
	}// END solve
	
	// Method called by the search method that creates and initializes
	// a 2D integer array that will be used to store the locations of all
	// the chars from the found word
	private void initializeLocator(String word)
	{
		locator = new int[word.length()][2];
	} //END initializeLocator
	
	// Method that searches the word inside the word search
	// iterates through each element in the 2D char array and uses
	// the recursiveSearch method to attempt to find the word
	private boolean search(String word)
	{
		// Initialize the word's locator and other variables that will
		// be needed to attempt to find the word
		initializeLocator(word);
		int counter = 0;
		boolean found = false;
		
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				// Did we find the first char of the word?
				if(puzzle[i][j] == word.charAt(0))
				{
					// Adjust the locator as an assumption that the word may have been found
					locator[counter][0] = i;
					locator[counter][1] = j;
					counter++;
					// Call the recursive search method to see if the word has been found
					if(recursiveSearch(word.substring(1, word.length()), i, j, counter))
					{
						// Word was found so set found to true and break out of the for loop
						found = true;
						break;
					}
					else
					{
						// Word was not found so set the counter back to 0 for the locator
						counter = 0;
					}
				}
			}// END INNER FOR LOOP
			// Checking if the word was found in order to break out of the outer for loop
			if(found)
			{
				break;
			}
		}// END OUTER FOR LOOP
		
		// Return whether the word was found in the word search or not
		return found;
	} //END search
	
	// Recursive method that searches the first char of the passed in word in the puzzle
	// adjusting the words locator with the location of the char if found in an adjacent cell
	// and calling itself while passing the word version of itself minus its first letter
	private boolean recursiveSearch(String word, int i, int j, int counter)
	{	
		// Set the default to false as the first char of the word has not been yet found
		boolean found = false;
		
		// BASE CASE - If we reached here then all the characters in the word were found so return true
		if(word.length() == 0)
		{
			return true;
		}
		
		// Checking upper-left adjacency of current spot as long as within grid boundaries and char not found
		if(!found && i > 0 && j > 0 && word.charAt(0) == puzzle[i-1][j-1])
		{
			// Assuming that the cell that found the char of the word hasn't been used before
			boolean used = false;
			
			// Iterate through the values that we currently have in the locator to check that
			// the newly found adjacent cell hasn't been used before for this word
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i-1) && locator[index][1] == (j-1))
				{
					used = true;
				}
			}
			// If the location of the char hasn't been used before, then use it now by storing it in locator
			// and recursively calling the function to search for the next char in the word
			if(!used)
			{
				locator[counter][0] = (i-1);
				locator[counter][1] = (j-1);
				found = recursiveSearch(word.substring(1, word.length()), (i-1), (j-1), counter + 1);
			}
		}
		
		// Checking upper adjacency of current spot as long as within grid boundaries and char not found
		if(!found && i > 0 && word.charAt(0) == puzzle[i-1][j])
		{
			// Assuming that the cell that found the char of the word hasn't been used before
			boolean used = false;
			
			// Iterate through the values that we currently have in the locator to check that
			// the newly found adjacent cell hasn't been used before for this word
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i-1) && locator[index][1] == (j))
				{
					used = true;
				}
			}
			// If the location of the char hasn't been used before, then use it now by storing it in locator
			// and recursively calling the function to search for the next char in the word
			if(!used)
			{
				locator[counter][0] = (i-1);
				locator[counter][1] = (j);
				found = recursiveSearch(word.substring(1, word.length()), (i-1), (j), counter + 1);
			}
		}		
		
		// Checking upper-right adjacency of current spot as long as within grid boundaries and char not found
		if(!found && i > 0 && j < (columns - 1) && word.charAt(0) == puzzle[i-1][j+1])
		{
			// Assuming that the cell that found the char of the word hasn't been used before
			boolean used = false;
			
			// Iterate through the values that we currently have in the locator to check that
			// the newly found adjacent cell hasn't been used before for this word
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i-1) && locator[index][1] == (j+1))
				{
					used = true;
				}
			}
			// If the location of the char hasn't been used before, then use it now by storing it in locator
			// and recursively calling the function to search for the next char in the word
			if(!used)
			{
				locator[counter][0] = (i-1);
				locator[counter][1] = (j+1);
				found = recursiveSearch(word.substring(1, word.length()), (i-1), (j+1), counter + 1);
			}
		}
		
		// Checking right adjacency of current spot as long as within grid boundaries and char not found
		if(!found && j < (columns - 1) && word.charAt(0) == puzzle[i][j+1])
		{
			// Assuming that the cell that found the char of the word hasn't been used before
			boolean used = false;
			
			// Iterate through the values that we currently have in the locator to check that
			// the newly found adjacent cell hasn't been used before for this word
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i) && locator[index][1] == (j+1))
				{
					used = true;
				}
			}
			// If the location of the char hasn't been used before, then use it now by storing it in locator
			// and recursively calling the function to search for the next char in the word
			if(!used)
			{
				locator[counter][0] = (i);
				locator[counter][1] = (j+1);
				found = recursiveSearch(word.substring(1, word.length()), (i), (j+1), counter + 1);
			}
		}
		
		// Checking bottom-right adjacency of current spot as long as within grid boundaries and char not found
		if(!found && i < (rows - 1) && j < (columns - 1) && word.charAt(0) == puzzle[i+1][j+1])
		{
			// Assuming that the cell that found the char of the word hasn't been used before
			boolean used = false;
			
			// Iterate through the values that we currently have in the locator to check that
			// the newly found adjacent cell hasn't been used before for this word
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i+1) && locator[index][1] == (j+1))
				{
					used = true;
				}
			}
			// If the location of the char hasn't been used before, then use it now by storing it in locator
			// and recursively calling the function to search for the next char in the word
			if(!used)
			{
				locator[counter][0] = (i+1);
				locator[counter][1] = (j+1);
				found = recursiveSearch(word.substring(1, word.length()), (i+1), (j+1), counter + 1);
			}
		}
		
		// Checking bottom adjacency of current spot as long as within grid boundaries and char not found
		if(!found && i < (rows - 1) && word.charAt(0) == puzzle[i+1][j])
		{
			// Assuming that the cell that found the char of the word hasn't been used before
			boolean used = false;
			
			// Iterate through the values that we currently have in the locator to check that
			// the newly found adjacent cell hasn't been used before for this word
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i+1) && locator[index][1] == (j))
				{
					used = true;
				}
			}
			// If the location of the char hasn't been used before, then use it now by storing it in locator
			// and recursively calling the function to search for the next char in the word
			if(!used)
			{
				locator[counter][0] = (i+1);
				locator[counter][1] = (j);
				found = recursiveSearch(word.substring(1, word.length()), (i+1), (j), counter + 1);
			}
		}
		
		// Checking bottom-left adjacency of current spot as long as within grid boundaries and char not found
		if(!found && i < (rows - 1) && j > 0 && word.charAt(0) == puzzle[i+1][j-1])
		{
			// Assuming that the cell that found the char of the word hasn't been used before
			boolean used = false;
			
			// Iterate through the values that we currently have in the locator to check that
			// the newly found adjacent cell hasn't been used before for this word
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i+1) && locator[index][1] == (j-1))
				{
					used = true;
				}
			}
			// If the location of the char hasn't been used before, then use it now by storing it in locator
			// and recursively calling the function to search for the next char in the word
			if(!used)
			{
				locator[counter][0] = (i+1);
				locator[counter][1] = (j-1);
				found = recursiveSearch(word.substring(1, word.length()), (i+1), (j-1), counter + 1);
			}
		}
		
		// Checking left adjacency of current spot as long as within grid boundaries and char not found
		if(!found && j > 0 && word.charAt(0) == puzzle[i][j-1])
		{
			// Assuming that the cell that found the char of the word hasn't been used before
			boolean used = false;
			
			// Iterate through the values that we currently have in the locator to check that
			// the newly found adjacent cell hasn't been used before for this word
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i) && locator[index][1] == (j-1))
				{
					used = true;
				}
			}
			// If the location of the char hasn't been used before, then use it now by storing it in locator
			// and recursively calling the function to search for the next char in the word
			if(!used)
			{
				locator[counter][0] = (i);
				locator[counter][1] = (j-1);
				found = recursiveSearch(word.substring(1, word.length()), (i), (j-1), counter + 1);
			}
		}
		
		return found;
	}//END recursiveSearch
	
	// Method that uses the location of the found words
	// to add its index to solvedPuzzle for later usage
	private void addToPuzzle(int index)
	{
		// Iterate through the chars of the found word
		// storing the location of each char with its appropriate index
		// in solvedPuzzle for later usage
		for(int i = 0; i < locator.length; i++)
		{
			// Store the x and y location of char and compare it to solvedPuzzle
			// to store the appropriate index for later usage
			int r = locator[i][0];
			int c = locator[i][1];
				
			// If it's not zero then a different word has a char there
			// so it will be shared and later highlighted with Blue so
			// adjust accordingly
			if(solvedPuzzle[r][c] != 0)
			{
				solvedPuzzle[r][c] = 5;
			}
			else
			{
				// Match that location with the current word
				// through its own index
				solvedPuzzle[r][c] = index;
			}
		}
	}// END addToPuzzle
	
	// Method that returns the 2D char array as a string with the found words highlighted
	private String solvedToString()
	{
		// Declare the string to store the solved puzzle and initialize it with required html code for a table
		String puzzleString = "<HTML><BODY><TABLE BORDER=1>";
		
		// Iterate through the puzzle checking solvedPuzzle to verify if the current location
		// contains a found word and highlight with the appropriate index if so, otherwise
		// no highlighting necessary
		for(int i = 0; i < rows; i++)
		{
			// New table row
			puzzleString += "<TR>";
			for(int j = 0; j < columns; j++)
			{
				// No highlighting necessary as this element doesn't contain any of our words
				if(solvedPuzzle[i][j] == 0)
				{
					// Each element is it's own column, with the appropriate font size
					puzzleString += "<TD><FONT SIZE=6><CENTER>" + puzzle[i][j] + "</CENTER></FONT></TD>";
				}
				else
				{
					// This is a location shared by more than 1 found word so use
					// the 0 index location in colors which is blue
					if(solvedPuzzle[i][j] == 5)
					{
						// Each element is it's own column, with the appropriate font size
						puzzleString += colors[0] + puzzle[i][j] + "</CENTER></FONT></TD>";
					}
					// This is a location that is specific to only one of our words so use that
					// words specific index to obtain the appropriate highlight color for the cell
					else
					{
						// Each element is it's own column, with the appropriate font size
						puzzleString += colors[solvedPuzzle[i][j]] + puzzle[i][j] + "</CENTER></FONT></TD>";
					}
				}
			}
			puzzleString += "</TR>";
		}
		puzzleString += "</TABLE></BODY></HTML>";
		
		return puzzleString;
	}//END solvedToString
	
	// Overrides the toString() method and provides the proper table format of 
	// 2D char array as a string. Used for displaying the unsolved word search
	public String toString()
	{
		// Declare the string to store the unsolved puzzle and initialize it with the appropriate html code for a table
		String puzzleString = "<HTML><BODY><TABLE BORDER=1>";
				
		// Iterate through the puzzle creating the appropriate string
		for(int i = 0; i < rows; i++)
		{
			puzzleString += "<TR>";
			for(int j = 0; j < columns; j++)
			{
				puzzleString += "<TD><FONT SIZE=6><CENTER>" + puzzle[i][j] + "</CENTER></FONT></TD>";
			}
			puzzleString += "</TR>";
		}
		puzzleString += "</TABLE></BODY></HTML>";
				
		return puzzleString;
	}
}//END Puzzle Class
