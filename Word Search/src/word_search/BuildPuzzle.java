package word_search;

import java.util.Random;

public class BuildPuzzle 
{	
	// A random generator and ASCII boundaries
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
		rows = 20;
		columns = 20;
		numberOfWords = n;
		words = new String[n];
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
	
	// Method that essentially stores the words provided by the user in
	// the main method of Program.java into a String array
	public void addWord(String word, int index)
	{
		this.words[index] = word;
	}// END addWord
	
	public String toString()
	{
		// Declare the string to store the unsolved puzzle and initialize it with 
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
	
	public void create()
	{
		for(int i = 1; i <= numberOfWords; i++)
		{
			addToPuzzle(words[i], i);
		}
	}
	
	private void addToPuzzle(String word, int index)
	{
		locator = new int[word.length()][2];
		boolean okay = false;
		
		while(!okay)
		{
			int rowPosition = generator.nextInt(rows);
			int columnPosition = generator.nextInt(columns);
		
			if(solvedPuzzle[rowPosition][columnPosition] == 0)
			{
				okay = recursiveLoad(word.substring(1, word.length()), index, rowPosition, columnPosition, 1);
				if(okay)
				{
					solvedPuzzle[rowPosition][columnPosition] = index;
					puzzle[rowPosition][columnPosition] = word.charAt(0);
					locator[0][0] = rowPosition;
					locator[0][1] = columnPosition;
				}
			}
			else
			{
				if(puzzle[rowPosition][columnPosition] == word.charAt(0))
				{
					okay = recursiveLoad(word.substring(1, word.length()), index, rowPosition, columnPosition, 1);
					if(okay)
					{
						solvedPuzzle[rowPosition][columnPosition] = 5;
						locator[0][0] = rowPosition;
						locator[0][1] = columnPosition;
					}
				}
			}
		}
	}
	
	private boolean recursiveLoad(String word, int index, int i, int j, int counter)
	{
		// If we reached here then all the characters in the word were successfully placed so return true
		if(word.length() == 0)
		{
			return true;
		}
		
		// Max will determine how many times we will generate a random number to find one that works or we will backtrack
		int max = 0;
		boolean okay = false;
		int adjacency; // will decide what adjacent cell to the current one to check
		
		while(max < 20)
		{
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
				//  and recursively calling the function to search for the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i-1][j-1] == 0)
					{
						solvedPuzzle[i-1][j-1] = index;
						puzzle[i-1][j-1] = word.charAt(0);
						locator[counter][0] = (i-1);
						locator[counter][1] = (j-1);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i-1), (j-1), counter + 1);
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i-1][j-1] == word.charAt(0))
						{
							// 5 represent a cell shared by two or more words
							solvedPuzzle[i-1][j-1] = 5;
							locator[counter][0] = (i-1);
							locator[counter][1] = (j-1);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i-1), (j-1), counter + 1);
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
				//  and recursively calling the function to search for the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i-1][j] == 0)
					{
						solvedPuzzle[i-1][j] = index;
						puzzle[i-1][j] = word.charAt(0);
						locator[counter][0] = (i-1);
						locator[counter][1] = (j);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i-1), (j), counter + 1);
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i-1][j] == word.charAt(0))
						{
							// 5 represent a cell shared by two or more words
							solvedPuzzle[i-1][j] = 5;
							locator[counter][0] = (i-1);
							locator[counter][1] = (j);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i-1), (j), counter + 1);
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
				//  and recursively calling the function to search for the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i-1][j+1] == 0)
					{
						solvedPuzzle[i-1][j+1] = index;
						puzzle[i-1][j+1] = word.charAt(0);
						locator[counter][0] = (i-1);
						locator[counter][1] = (j+1);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i-1), (j+1), counter + 1);
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i-1][j+1] == word.charAt(0))
						{
							// 5 represent a cell shared by two or more words
							solvedPuzzle[i-1][j+1] = 5;
							locator[counter][0] = (i-1);
							locator[counter][1] = (j+1);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i-1), (j+1), counter + 1);
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
				//  and recursively calling the function to search for the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i][j+1] == 0)
					{
						solvedPuzzle[i][j+1] = index;
						puzzle[i][j+1] = word.charAt(0);
						locator[counter][0] = (i);
						locator[counter][1] = (j+1);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i), (j+1), counter + 1);
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i][j+1] == word.charAt(0))
						{
							// 5 represent a cell shared by two or more words
							solvedPuzzle[i][j+1] = 5;
							locator[counter][0] = (i);
							locator[counter][1] = (j+1);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i), (j+1), counter + 1);
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
				//  and recursively calling the function to search for the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i+1][j+1] == 0)
					{
						solvedPuzzle[i+1][j+1] = index;
						puzzle[i+1][j+1] = word.charAt(0);
						locator[counter][0] = (i+1);
						locator[counter][1] = (j+1);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i+1), (j+1), counter + 1);
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i+1][j+1] == word.charAt(0))
						{
							// 5 represent a cell shared by two or more words
							solvedPuzzle[i+1][j+1] = 5;
							locator[counter][0] = (i+1);
							locator[counter][1] = (j+1);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i+1), (j+1), counter + 1);
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
				//  and recursively calling the function to search for the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i+1][j] == 0)
					{
						solvedPuzzle[i+1][j] = index;
						puzzle[i+1][j] = word.charAt(0);
						locator[counter][0] = (i+1);
						locator[counter][1] = (j);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i+1), (j), counter + 1);
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i+1][j] == word.charAt(0))
						{
							// 5 represent a cell shared by two or more words
							solvedPuzzle[i+1][j] = 5;
							locator[counter][0] = (i+1);
							locator[counter][1] = (j);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i+1), (j), counter + 1);
						}
					}
				}
			} // END INITIAL IF STATEMENT
			
			// If adjacency is 6, checking bottom-left adjacency of current spot as long as within grid boundaries
			if(adjacency == 0 && i < (rows-1) && j > 0)
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
				//  and recursively calling the function to search for the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i+1][j-1] == 0)
					{
						solvedPuzzle[i+1][j-1] = index;
						puzzle[i+1][j-1] = word.charAt(0);
						locator[counter][0] = (i+1);
						locator[counter][1] = (j-1);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i+1), (j-1), counter + 1);
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i+1][j-1] == word.charAt(0))
						{
							// 5 represent a cell shared by two or more words
							solvedPuzzle[i+1][j-1] = 5;
							locator[counter][0] = (i+1);
							locator[counter][1] = (j-1);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i+1), (j-1), counter + 1);
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
				//  and recursively calling the function to search for the next char in the word
				if(!used)
				{
					// 0 means the spot hasn't been used by a previous word
					if(solvedPuzzle[i][j-1] == 0)
					{
						solvedPuzzle[i][j-1] = index;
						puzzle[i][j-1] = word.charAt(0);
						locator[counter][0] = (i);
						locator[counter][1] = (j-1);
						okay = recursiveLoad(word.substring(1, word.length()), index, (i), (j-1), counter + 1);
					}
					else
					{
						// If the spot has been used by a different word, our word can use it
						// only if the current char is the same
						if(puzzle[i][j-1] == word.charAt(0))
						{
							// 5 represent a cell shared by two or more words
							solvedPuzzle[i][j-1] = 5;
							locator[counter][0] = (i);
							locator[counter][1] = (j-1);
							okay = recursiveLoad(word.substring(1, word.length()), index, (i), (j-1), counter + 1);
						}
					}
				}
			} // END INITIAL IF STATEMENT
						
			if(okay)
			{
				break;
			}
			max++;
		} // END WHILE LOOP
		return okay;
	}// END recursiveLoad METHOD
}
