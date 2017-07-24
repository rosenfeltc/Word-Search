package word_search;

public class Puzzle 
{
	private char[][] puzzle;
	private int[][] solvedPuzzle;
	private String[] words;
	private int numberOfWords;
	private int rows;
	private int columns;
	private int[][] locator;
	private final String[] colors;
	
	public Puzzle(int r, int c, int n)
	{
		rows = r;
		columns = c;
		puzzle = new char[rows][columns];
		solvedPuzzle = new int[rows][columns];
		words = new String[n + 1];
		numberOfWords = n;
		
		// Add the colors for highlighting
		colors = new String[5];
		colors[0] = "<span style=\"background-color: #0000ff\">";
		colors[1] = "<span style=\"background-color: #ff0000\">";
		colors[2] = "<span style=\"background-color: #00ff00\">";
		colors[3] = "<span style=\"background-color: #ffa500\">";
		colors[4] = "<span style=\"background-color: #ffff00\">";
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
	
	public String solve()
	{
		for(int i = 1; i <= numberOfWords; i++)
		{
			if(search(words[i]))
			{
				addToPuzzle(i);
			}
		}
		// Obtain the puzzle solution
		String solution = toString();
		
		return solution;
	}
	
	public void addToPuzzle(int index)
	{
		for(int i = 0; i < locator.length; i++)
		{
			int r = locator[i][0];
			int c = locator[i][1];
			if(solvedPuzzle[r][c] != 0)
			{
				solvedPuzzle[r][c] = numberOfWords + 1;
			}
			else
			{
				solvedPuzzle[r][c] = index;
			}
		}
	}
	
	public void addWord(String word, int index)
	{
		this.words[index] = word;
	}
	
	private void initializeLocator(String word)
	{
		locator = new int[word.length()][2];
	}
	// Method that searches the word inside the puzzle
	public boolean search(String word)
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
		
		return found;
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
	public String toString()
	{
		String puzzleString = "";
		
		for(int i = 0; i < rows; i++)
		{
			puzzleString += "<html><font face=\"Comic Sans MS\" size=24>";
			for(int j = 0; j < columns; j++)
			{
				puzzleString += "|";
				if(solvedPuzzle[i][j] == 0)
				{
					puzzleString += puzzle[i][j];
				}
				else
				{
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
			puzzleString +="</font></html>";
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
