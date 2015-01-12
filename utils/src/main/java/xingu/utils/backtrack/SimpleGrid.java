package xingu.utils.backtrack;

public class SimpleGrid
    extends Grid
{
	public SimpleGrid(int size, Handler handler)
    {
	    super(size, handler);
    }

	@Override
	public void init()
	{
    	for (int row = 0; row < ROWS; row++) 
        {
        	for (int col = 0; col < COLS; col++) 
        	{
                matrix[row][col] = new Point(Grid.PATH,0);
            }
        }
	
    	matrix[0][0].value = 2;
    	matrix[1][0].value = 0;
    	matrix[2][0].value = 1;
    	matrix[3][0].value = 4;

    	matrix[0][1].value = 2;
    	matrix[1][1].value = 3;
    	matrix[2][1].value = 2;
    	matrix[3][1].value = 1;

    	matrix[0][2].value = 3;
    	matrix[1][2].value = 1;
    	matrix[2][2].value = 2;
    	matrix[3][2].value = 2;

    	matrix[0][3].value = 0;
    	matrix[1][3].value = 1;
    	matrix[2][3].value = 1;
    	matrix[3][3].value = 2;

	}
}
