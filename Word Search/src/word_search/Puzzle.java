package word_search;

public class Puzzle 
{
	private char[][] puzzle;
	private int rows;
	private int columns;
	private int[][] locator;
	
	public Puzzle(int r, int c)
	{
		rows = r;
		columns = c;
		puzzle = new char[rows][columns];
	}
	
	// Method that loads the puzzle into a 2D char array from a string
	public void load(String puzzleString)
	{
		int index = 0;
		int i = 0;
		int j = 0;
		int length = puzzleString.length();
		
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
	}
	
	private void initializeLocator(String word)
	{
		locator = new int[word.length()][2];
	}
	// Method that searches the word inside the puzzle
	public void search(String word)
	{
		initializeLocator(word);
		int counter = 0;
		boolean found = false;
		
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				if(puzzle[i][j] == word.charAt(0))
				{
					locator[counter][0] = i;
					locator[counter][1] = j;
					counter++;
					if(recursiveSearch(word.substring(1, word.length()), i, j, counter))
					{
						found = true;
						break;
					}
					else
					{
						counter = 0;
					}
				}
			}
			
			if(found)
			{
				break;
			}
		}
	}
	
	private boolean recursiveSearch(String word, int i, int j, int counter)
	{	
		boolean found = false;
		
		if(word.length() == 0)
		{
			return true;
		}
		
		// Checking upper-left adjacency of current spot as long as within grid boundaries
		if(!found && i > 0 && j > 0 && word.charAt(0) == puzzle[i-1][j-1])
		{
			boolean used = false;
			
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i-1) && locator[index][1] == (j-1))
				{
					used = true;
				}
			}
			
			if(!used)
			{
				locator[counter][0] = (i-1);
				locator[counter][1] = (j-1);
				found = recursiveSearch(word.substring(1, word.length()), (i-1), (j-1), counter + 1);
			}
		}
		
		// Checking upper adjacency of current spot as long as within grid boundaries
		if(!found && i > 0 && word.charAt(0) == puzzle[i-1][j])
		{
			boolean used = false;
			
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i-1) && locator[index][1] == (j))
				{
					used = true;
				}
			}
			
			if(!used)
			{
				locator[counter][0] = (i-1);
				locator[counter][1] = (j);
				found = recursiveSearch(word.substring(1, word.length()), (i-1), (j), counter + 1);
			}
		}		
		
		// Checking upper-right adjacency of current spot as long as within grid boundaries
		if(!found && i > 0 && j < (columns - 1) && word.charAt(0) == puzzle[i-1][j+1])
		{
			boolean used = false;
			
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i-1) && locator[index][1] == (j+1))
				{
					used = true;
				}
			}
			
			if(!used)
			{
				locator[counter][0] = (i-1);
				locator[counter][1] = (j+1);
				found = recursiveSearch(word.substring(1, word.length()), (i-1), (j+1), counter + 1);
			}
		}
		
		// Checking right adjacency of current spot as long as within grid boundaries
		if(!found && j < (columns - 1) && word.charAt(0) == puzzle[i][j+1])
		{
			boolean used = false;
			
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i) && locator[index][1] == (j+1))
				{
					used = true;
				}
			}
			
			if(!used)
			{
				locator[counter][0] = (i);
				locator[counter][1] = (j+1);
				found = recursiveSearch(word.substring(1, word.length()), (i), (j+1), counter + 1);
			}
		}
		
		// Checking bottom-right adjacency of current spot as long as within grid boundaries
		if(!found && i < (rows - 1) && j < (columns - 1) && word.charAt(0) == puzzle[i+1][j+1])
		{
			boolean used = false;
			
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i+1) && locator[index][1] == (j+1))
				{
					used = true;
				}
			}
			
			if(!used)
			{
				locator[counter][0] = (i+1);
				locator[counter][1] = (j+1);
				found = recursiveSearch(word.substring(1, word.length()), (i+1), (j+1), counter + 1);
			}
		}
		
		// Checking bottom adjacency of current spot as long as within grid boundaries
		if(!found && i < (rows - 1) && word.charAt(0) == puzzle[i+1][j])
		{
			boolean used = false;
			
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i+1) && locator[index][1] == (j))
				{
					used = true;
				}
			}
			
			if(!used)
			{
				locator[counter][0] = (i+1);
				locator[counter][1] = (j);
				found = recursiveSearch(word.substring(1, word.length()), (i+1), (j), counter + 1);
			}
		}
		
		// Checking bottom-left adjacency of current spot as long as within grid boundaries
		if(!found && i < (rows - 1) && j > 0 && word.charAt(0) == puzzle[i+1][j-1])
		{
			boolean used = false;
			
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i+1) && locator[index][1] == (j-1))
				{
					used = true;
				}
			}
			
			if(!used)
			{
				locator[counter][0] = (i+1);
				locator[counter][1] = (j-1);
				found = recursiveSearch(word.substring(1, word.length()), (i+1), (j-1), counter + 1);
			}
		}
		
		// Checking left adjacency of current spot as long as within grid boundaries
		if(!found && j > 0 && word.charAt(0) == puzzle[i][j-1])
		{
			boolean used = false;
			
			for(int index = 0; index < counter && !used; index++)
			{
				if(locator[index][0] == (i) && locator[index][1] == (j-1))
				{
					used = true;
				}
			}
			
			if(!used)
			{
				locator[counter][0] = (i);
				locator[counter][1] = (j-1);
				found = recursiveSearch(word.substring(1, word.length()), (i), (j-1), counter + 1);
			}
		}
		
		return found;
	}
	
	// Method that returns the 2D char array as a string
	public String puzzleToString()
	{
		String puzzleString = "";
		
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				puzzleString += puzzle[i][j];
			}
			puzzleString += "\n";
		}
		
		return puzzleString;
	}
	
	// Method that returns the 2D char array as a string
	public String locatorToString()
	{
		String locatorString = "";
		
		for(int i = 0; i < locator.length; i++)
		{
			locatorString += "(";
			for(int j = 0; j < locator[0].length; j++)
			{
				locatorString += locator[i][j] + ",";
			}
			locatorString += ") ";
		}
			
		return locatorString;
	}
}
