package word_search;

public class Puzzle 
{
	private char[][] puzzle;
	private int rows;
	private int columns;
	
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
	
	public String display()
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
}
