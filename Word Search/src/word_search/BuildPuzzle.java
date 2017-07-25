/* This is the buildPuzzle class that stores all the variables and performs all of the methods related to creating the Word Search puzzle:
 * from storing the words provided by the user, placing the words recursively into the created puzzle, to returning the puzzle as a CSV type
 * String and/or returning the String solution with the words highlighted.
 * Coded by Christopher Rosenfelt for CSI 213
 */
package word_search;

import java.util.Random;

public class BuildPuzzle 
{	
	// A random generator and ASCII boundaries which will be used for 
	// placing the random letters in our Word Search into spots that don't contain our words
	Random generator = new Random();
	final int ASCII_LOWER = 97;
	final int ASCII_UPPER = 122;
	
	// Private fields that will be useful to the different methods in this class
	private char[][] puzzle;
	private int[][] solvedPuzzle;
	private String[] words;
	private int numberOfWords;
	private int rows;
	private int columns;
	private int[][] locator;
	private final String[] colors;
	
	// Puzzle constructor that takes the number of words the user wants in the puzzle
	public BuildPuzzle(int n)
	{
		// Hardcoded 20x20 word search
		rows = 20;
		columns = 20;
		numberOfWords = n;
		words = new String[n+1];
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
	
	// Method that essentially stores the words provided by the user into a String array
	public void addWord(String word, int index)
	{
		this.words[index] = word;
	}// END addWord
	
	// Method that overrides the toString() method to display the created Word Search in a table format
	public String toString()
	{
		// Declare the string to store the unsolved puzzle and initialize it with the appropriate html code 
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
	
	// The essential method that will create a new word search by using the addToPuzzle method to
	// add the inputted words from the user and then will place a random char in the remaining empty spots
	public void create()
	{
		// Iterate through the number of words submitted by the user and add them to our word search
		for(int i = 1; i <= numberOfWords; i++)
		{
			addToPuzzle(words[i], i);
		}
		
		// Place a random char in any spots not occupied by a letter from the user's inputted words
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				if(solvedPuzzle[i][j] == 0)
				{
					// Force a positive number and use the difference of ASCII upper and Lower plus as a modulus
					int randomChar = Math.abs(generator.nextInt()) % (ASCII_UPPER - ASCII_LOWER + 1);
					// Add the lower ASCII to the previously calculated version of randomChar so that the random
					// number can represent any possible lowercase letter
					randomChar += ASCII_LOWER;
					// Add it to the current location of the word search
					puzzle[i][j] = (char) randomChar;
				}
			}
		}
	}
	
	// Method that takes a word and its given index and places that word inside the puzzle
	private void addToPuzzle(String word, int index)
	{
		// Initialize the locator to ensure we don't use the same cell twice
		// and to track the word's location in the word search
		locator = new int[word.length()][2];
		boolean okay = false;
		
		// Generate as long as we have a location in the 2D char array that is viable
		// for the placement of the first letter of the word
		while(!okay)
		{
			// Generate random row and column location for potential placement
			int rowPosition = generator.nextInt(rows);
			int columnPosition = generator.nextInt(columns);
		
			// Open spot not used by a word yet
			if(solvedPuzzle[rowPosition][columnPosition] == 0)
			{
				locator[0][0] = rowPosition;
				locator[0][1] = columnPosition;
				okay = recursiveLoad(word.substring(1, word.length()), index, rowPosition, columnPosition, 1);
				// Word was able to be completely placed so finish placing its first letter
				if(okay)
				{
					solvedPuzzle[rowPosition][columnPosition] = index;
					puzzle[rowPosition][columnPosition] = word.charAt(0);
				}
			}
			// Not an open spot but may be of use if the specific characters from the two words are the same
			else
			{
				if(puzzle[rowPosition][columnPosition] == word.charAt(0))
				{
					locator[0][0] = rowPosition;
					locator[0][1] = columnPosition;
					okay = recursiveLoad(word.substring(1, word.length()), index, rowPosition, columnPosition, 1);
					// Word was able to be completely placed so finish placing its first letter
					if(okay)
					{
						solvedPuzzle[rowPosition][columnPosition] = 5;
					}
				}
			}
		}//END WHILE
	}// END addToPuzzle
	
	// Method that recursively places the first letter of the word into an adjacent cell that
	// hasn't been used by the same word previously
	private boolean recursiveLoad(String word, int index, int i, int j, int counter)
	{
		// If we reached here then all the characters in the word were successfully placed so return true
		if(word.length() == 0)
		{
			return true;
		}
		
		// Max will determine how many times we will generate a random number to find one that works otherwise we will backtrack
		int max = 0;
		boolean okay = false;
		int adjacency; // will decide what adjacent cell to the current one to check
		
		while(max < 20)
		{
			// Generate a random adjacent cell to the current one
			adjacency = generator.nextInt(8);
			
			// If adjacency is 0, checking upper-left adjacency of current spot as long as within grid boundaries
			if(adjacency == 0 && i > 0 && j > 0)
			{
				// Assume that the cell hasn't been used before by our word
				boolean used = false;
				
				// Iterate through the values that we currently have in the locator to check that
				// the newly found adjacent cell hasn't been used before for this word
				for(int k = 0; k < counter && !used; k++)
				{
					if(locator[k][0] == (i-1) && locator[k][1] == (j-1))
					{
						used = true;
					}
				}
				// If the location of the char hasn't been used before, then use it now by storing it in locator
				// and recursively calling the function to place the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i-1][j-1] == 0)
					{
						locator[counter][0] = (i-1);
						locator[counter][1] = (j-1);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i-1), (j-1), counter + 1);
						
						if(okay)
						{
							solvedPuzzle[i-1][j-1] = index;
							puzzle[i-1][j-1] = word.charAt(0);
						}
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i-1][j-1] == word.charAt(0))
						{
							
							locator[counter][0] = (i-1);
							locator[counter][1] = (j-1);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i-1), (j-1), counter + 1);
							
							if(okay)
							{
								// 5 represent a cell shared by two or more words
								solvedPuzzle[i-1][j-1] = 5;
							}
						}
					}
				}
			} // END INITIAL IF STATEMENT

			// If adjacency is 1, checking upper adjacency of current spot as long as within grid boundaries
			if(adjacency == 1 && i > 0)
			{
				// Assume that the cell hasn't been used before by our word
				boolean used = false;
				
				// Iterate through the values that we currently have in the locator to check that
				// the newly found adjacent cell hasn't been used before for this word
				for(int k = 0; k < counter && !used; k++)
				{
					if(locator[k][0] == (i-1) && locator[k][1] == (j))
					{
						used = true;
					}
				}
				// If the location of the char hasn't been used before, then use it now by storing it in locator
				// and recursively calling the function to place the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i-1][j] == 0)
					{
						locator[counter][0] = (i-1);
						locator[counter][1] = (j);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i-1), (j), counter + 1);
						
						if(okay)
						{
							solvedPuzzle[i-1][j] = index;
							puzzle[i-1][j] = word.charAt(0);
						}
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i-1][j] == word.charAt(0))
						{
							locator[counter][0] = (i-1);
							locator[counter][1] = (j);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i-1), (j), counter + 1);
							
							if(okay)
							{
								// 5 represent a cell shared by two or more words
								solvedPuzzle[i-1][j] = 5;
							}
						}
					}
				}
			} // END INITIAL IF STATEMENT
			
			// If adjacency is 2, checking upper-right adjacency of current spot as long as within grid boundaries
			if(adjacency == 2 && i > 0 && j < (columns-1))
			{
				// Assume that the cell hasn't been used before by our word
				boolean used = false;
				
				// Iterate through the values that we currently have in the locator to check that
				// the newly found adjacent cell hasn't been used before for this word
				for(int k = 0; k < counter && !used; k++)
				{
					if(locator[k][0] == (i-1) && locator[k][1] == (j+1))
					{
						used = true;
					}
				}
				// If the location of the char hasn't been used before, then use it now by storing it in locator
				// and recursively calling the function to place the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i-1][j+1] == 0)
					{
						locator[counter][0] = (i-1);
						locator[counter][1] = (j+1);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i-1), (j+1), counter + 1);
						
						if(okay)
						{
							solvedPuzzle[i-1][j+1] = index;
							puzzle[i-1][j+1] = word.charAt(0);
						}
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i-1][j+1] == word.charAt(0))
						{
							locator[counter][0] = (i-1);
							locator[counter][1] = (j+1);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i-1), (j+1), counter + 1);
							
							if(okay)
							{
								// 5 represent a cell shared by two or more words
								solvedPuzzle[i-1][j+1] = 5;
							}
						}
					}
				}
			} // END INITIAL IF STATEMENT
			
			// If adjacency is 3, checking center-right adjacency of current spot as long as within grid boundaries
			if(adjacency == 3 && j < (columns-1))
			{
				// Assume that the cell hasn't been used before by our word
				boolean used = false;
				
				// Iterate through the values that we currently have in the locator to check that
				// the newly found adjacent cell hasn't been used before for this word
				for(int k = 0; k < counter && !used; k++)
				{
					if(locator[k][0] == (i) && locator[k][1] == (j+1))
					{
						used = true;
					}
				}
				// If the location of the char hasn't been used before, then use it now by storing it in locator
				// and recursively calling the function to place the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i][j+1] == 0)
					{
						locator[counter][0] = (i);
						locator[counter][1] = (j+1);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i), (j+1), counter + 1);
						
						if(okay)
						{
							solvedPuzzle[i][j+1] = index;
							puzzle[i][j+1] = word.charAt(0);
						}
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i][j+1] == word.charAt(0))
						{
							locator[counter][0] = (i);
							locator[counter][1] = (j+1);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i), (j+1), counter + 1);
							
							if(okay)
							{
								// 5 represent a cell shared by two or more words
								solvedPuzzle[i][j+1] = 5;
							}
						}
					}
				}
			} // END INITIAL IF STATEMENT
			
			// If adjacency is 4, checking bottom-right adjacency of current spot as long as within grid boundaries
			if(adjacency == 4 && i < (rows-1) && j < (columns-1))
			{
				// Assume that the cell hasn't been used before by our word
				boolean used = false;
				
				// Iterate through the values that we currently have in the locator to check that
				// the newly found adjacent cell hasn't been used before for this word
				for(int k = 0; k < counter && !used; k++)
				{
					if(locator[k][0] == (i+1) && locator[k][1] == (j+1))
					{
						used = true;
					}
				}
				// If the location of the char hasn't been used before, then use it now by storing it in locator
				// and recursively calling the function to place the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i+1][j+1] == 0)
					{
						locator[counter][0] = (i+1);
						locator[counter][1] = (j+1);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i+1), (j+1), counter + 1);
						
						if(okay)
						{
							solvedPuzzle[i+1][j+1] = index;
							puzzle[i+1][j+1] = word.charAt(0);
						}
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i+1][j+1] == word.charAt(0))
						{
							locator[counter][0] = (i+1);
							locator[counter][1] = (j+1);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i+1), (j+1), counter + 1);
							
							if(okay)
							{
								// 5 represent a cell shared by two or more words
								solvedPuzzle[i+1][j+1] = 5;
							}
						}
					}
				}
			} // END INITIAL IF STATEMENT
			
			// If adjacency is 5, checking bottom adjacency of current spot as long as within grid boundaries
			if(adjacency == 5 && i < (rows-1))
			{
				// Assume that the cell hasn't been used before by our word
				boolean used = false;
				
				// Iterate through the values that we currently have in the locator to check that
				// the newly found adjacent cell hasn't been used before for this word
				for(int k = 0; k < counter && !used; k++)
				{
					if(locator[k][0] == (i+1) && locator[k][1] == (j))
					{
						used = true;
					}
				}
				// If the location of the char hasn't been used before, then use it now by storing it in locator
				// and recursively calling the function to place the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i+1][j] == 0)
					{
						locator[counter][0] = (i+1);
						locator[counter][1] = (j);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i+1), (j), counter + 1);
						
						if(okay)
						{
							solvedPuzzle[i+1][j] = index;
							puzzle[i+1][j] = word.charAt(0);
						}
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i+1][j] == word.charAt(0))
						{
							locator[counter][0] = (i+1);
							locator[counter][1] = (j);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i+1), (j), counter + 1);
							
							if(okay)
							{
								// 5 represent a cell shared by two or more words
								solvedPuzzle[i+1][j] = 5;
							}
						}
					}
				}
			} // END INITIAL IF STATEMENT
			
			// If adjacency is 6, checking bottom-left adjacency of current spot as long as within grid boundaries
			if(adjacency == 6 && i < (rows-1) && j > 0)
			{
				// Assume that the cell hasn't been used before by our word
				boolean used = false;
				
				// Iterate through the values that we currently have in the locator to check that
				// the newly found adjacent cell hasn't been used before for this word
				for(int k = 0; k < counter && !used; k++)
				{
					if(locator[k][0] == (i+1) && locator[k][1] == (j-1))
					{
						used = true;
					}
				}
				// If the location of the char hasn't been used before, then use it now by storing it in locator
				// and recursively calling the function to place the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i+1][j-1] == 0)
					{
						locator[counter][0] = (i+1);
						locator[counter][1] = (j-1);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i+1), (j-1), counter + 1);
						
						if(okay)
						{
							solvedPuzzle[i+1][j-1] = index;
							puzzle[i+1][j-1] = word.charAt(0);
						}
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i+1][j-1] == word.charAt(0))
						{
							locator[counter][0] = (i+1);
							locator[counter][1] = (j-1);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i+1), (j-1), counter + 1);
							
							if(okay)
							{
								// 5 represent a cell shared by two or more words
								solvedPuzzle[i+1][j-1] = 5;
							}
						}
					}
				}
			} // END INITIAL IF STATEMENT
			
			// If adjacency is 7, checking center-left adjacency of current spot as long as within grid boundaries
			if(adjacency == 7 && j > 0)
			{
				// Assume that the cell hasn't been used before by our word
				boolean used = false;
				
				// Iterate through the values that we currently have in the locator to check that
				// the newly found adjacent cell hasn't been used before for this word
				for(int k = 0; k < counter && !used; k++)
				{
					if(locator[k][0] == (i) && locator[k][1] == (j-1))
					{
						used = true;
					}
				}
				// If the location of the char hasn't been used before, then use it now by storing it in locator
				// and recursively calling the function to place the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i][j-1] == 0)
					{
						locator[counter][0] = (i);
						locator[counter][1] = (j-1);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i), (j-1), counter + 1);
						
						if(okay)
						{
							solvedPuzzle[i][j-1] = index;
							puzzle[i][j-1] = word.charAt(0);
						}
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i][j-1] == word.charAt(0))
						{
							locator[counter][0] = (i);
							locator[counter][1] = (j-1);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i), (j-1), counter + 1);
							
							if(okay)
							{
								// 5 represent a cell shared by two or more words
								solvedPuzzle[i][j-1] = 5;
							}
						}
					}
				}
			} // END INITIAL IF STATEMENT
			
			// Word was successfully placed so break from while loop
			if(okay)
			{
				break;
			}
			// Word was not successfully placed so update max and maybe try a new adjacent cell
			max++;
		} // END WHILE LOOP
		return okay;
	}// END recursiveLoad METHOD
	
	// Method that returns the solved puzzle with highlighted words
	public String solvedToString()
	{
		// Declare the string to store the solved puzzle and initialize it with required html code for a table
		String puzzleString = "<HTML><BODY><TABLE BORDER=1>";
		
		// Iterate through the puzzle checking solvedPuzzle to verify if the current location
		// contains a found word and highlight with the appropriate index if so, otherwise
		// no highlighting necessary
		for(int i = 0; i < rows; i++)
		{
			puzzleString += "<TR>";
			for(int j = 0; j < columns; j++)
			{
				if(solvedPuzzle[i][j] == 0)
				{
					puzzleString += "<TD><FONT SIZE=6><CENTER>" + puzzle[i][j] + "</CENTER></FONT></TD>";
				}
				else
				{
					// This is a location shared by more than 1 found word so use
					// the 0 index location in colors which is blue
					if(solvedPuzzle[i][j] == 5)
					{
						puzzleString += colors[0] + puzzle[i][j] + "</CENTER></FONT></TD>";
					}
					else
					{
						puzzleString += colors[solvedPuzzle[i][j]] + puzzle[i][j] + "</CENTER></FONT></TD>";
					}
				}
			}
			puzzleString += "</TR>";
		}
		puzzleString += "</TABLE></BODY></HTML>";
		
		return puzzleString;
	}//END solvedToString
	
	// Method that uses the 2D char array to create and return a CSV-type String
	public String createCSV()
	{
		String csv = new String();
		
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				// The String will be CSV-type due to the addition of the ',' at each 'column'
				csv += puzzle[i][j] + ",";
			}
			csv += "\n";
		}
		return csv;
	}
}
