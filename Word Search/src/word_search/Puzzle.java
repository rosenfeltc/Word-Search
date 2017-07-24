/* This is the Puzzle class that stores all the variables and performs all of the methods related to the Word Search puzzle:
 * from loading the Puzzle into an array of Chars, storing the words provided by the user, to searching those words in the puzzle and
 * highlighting them in the final solution returned as a String to the main method in the program class.
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
	// an integer for the number of columns, and an integer for the number
	// of words that will be searched
	public Puzzle(int r, int c, int n)
	{
		rows = r;
		columns = c;
		numberOfWords = n;
		puzzle = new char[rows][columns];
		solvedPuzzle = new int[rows][columns];
		words = new String[n + 1]; // Plus 1 because later on we will be storing words in their appropriate index so 0 index will be null
		
		// Add the colors that will be used for highlighting the found words
		colors = new String[5];
		colors[0] = "<span style=\"background-color: #0000ff\">";
		colors[1] = "<span style=\"background-color: #ff0000\">";
		colors[2] = "<span style=\"background-color: #00ff00\">";
		colors[3] = "<span style=\"background-color: #ffa500\">";
		colors[4] = "<span style=\"background-color: #ffff00\">";
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
			if(puzzleString.charAt(index) == ',')
			{
				j++;
				index++;
			}
			else if(puzzleString.charAt(index) == '\n')
			{
				i++;
				j = 0;
				index++;
			}
			else
			{
				puzzle[i][j] = puzzleString.charAt(index);
				index++;
			}
		}
	}// END load
	
	// Method that essentially stores the words provided by the user in
	// the main method of Program.java into a String array
	public void addWord(String word, int index)
	{
		this.words[index] = word;
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
			
			// If it's not zero then a different word has a Char there
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
	
	// Method called by the search method that creates and initializes
	// a 2D integer array that will be used to store the locations of all
	// the chars from the found word
	private void initializeLocator(String word)
	{
		locator = new int[word.length()][2];
	} //END initializeLocator
	
	// Method that searches the word inside the puzzle
	// iterates through all the puzzle and uses the recursiveSearch
	// method to attempt to find the word
	private boolean search(String word)
	{
		// Initialize the words locator and other variables that will
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
		// Set the default as the first char of the word has not been yet found
		boolean found = false;
		
		// If we reached here then all the characters in the word were found so return true
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
			//  and recursively calling the function to search for the next char in the word
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
			//  and recursively calling the function to search for the next char in the word
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
			//  and recursively calling the function to search for the next char in the word
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
			//  and recursively calling the function to search for the next char in the word
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
			//  and recursively calling the function to search for the next char in the word
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
			//  and recursively calling the function to search for the next char in the word
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
			//  and recursively calling the function to search for the next char in the word
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
			//  and recursively calling the function to search for the next char in the word
			if(!used)
			{
				locator[counter][0] = (i);
				locator[counter][1] = (j-1);
				found = recursiveSearch(word.substring(1, word.length()), (i), (j-1), counter + 1);
			}
		}
		
		return found;
	}//END recursiveSearch
	
	// Method that returns the 2D char array as a string with the found words highlighted
	private String solvedToString()
	{
		// Declare the string to store the solved puzzle and initialize it with required font
		String puzzleString = "<html><font face=\"Comic Sans MS\" size=24>";
		
		// Iterate through the puzzle checking solvedPuzzle to verify if the current location
		// contains a found word and highlight with the appropriate index if so, otherwise
		// no highlighting necessary
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				puzzleString += "|";
				if(solvedPuzzle[i][j] == 0)
				{
					puzzleString += puzzle[i][j];
				}
				else
				{
					// This is a location shared by more than 1 found word so use
					// the 0 index location in colors which is blue
					if(solvedPuzzle[i][j] == 5)
					{
						puzzleString += colors[0];
						puzzleString += puzzle[i][j];
						puzzleString += "</span>";
					}
					else
					{
						puzzleString += colors[solvedPuzzle[i][j]];
						puzzleString += puzzle[i][j];
						puzzleString += "</span>";
					}
				}
				puzzleString+= "|";
			}
			puzzleString += "<br>";
		}
		
		return puzzleString;
	}//END solvedToString
}//END Puzzle Class
