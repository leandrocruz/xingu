package xingu.utils.backtrack;


public class ChessBoard
	implements Handler
{
	private Grid grid;
	
	private int maxCount;
	
	public ChessBoard(int dim)
	{
		grid = new SimpleGrid(dim, this);
	}

	public int letGoPig()
    {
		grid.init();
	    grid.walk(0, 0);
	    return maxCount;
    }

	public void gridEnded(Grid grid)
    {
		int count = 0;
		for (int row = 0; row < grid.getRows(); row++) 
		{
			for (int col = 0; col < grid.getCols(); col++) 
			{
				Point point = grid.getPoint(row,col);
				if(point.status == Grid.VISITED)
				{
					count = count + point.value;
				}
			}
		}
		if(count > maxCount)
		{
			//grid.print();
			//System.out.println("Count: " + count);
			maxCount = count;
		}
    }

}
