package br.com.ibnetwork.xingu.utils.backtrack;

public abstract class Grid
{
    public static final int PATH = 0;
    
    public static final int VISITED = 1;
    
    protected Point[][] matrix;
    
    protected int ROWS, COLS;
    
    private Handler handler;
    
    public Grid(int size, Handler handler)
    {
    	ROWS = size;
    	COLS = size;
    	matrix = new Point[size][size];
    	this.handler = handler;
    }
    
    public abstract void init();

	public void walk(int row, int col) 
    {
    	if (isValid(row, col)) 
    	{
    		matrix[row][col].status = Grid.VISITED;
            if (end(row, col)) 
            {    
            	//print();
            	handler.gridEnded(this);
            } 
            else 
            {
            	walk(row+1, col); // Go Down
            	//walk(row-1, col); // Go Up
            	//walk(row, col-1); // Go Left
            	walk(row, col+1); // Go Right
            }
            matrix[row][col].status = PATH;      // Backtrack
        }
    }
    
	public void print()
    {
        for (int row = 0; row < ROWS; row++) 
        {
        	for (int col = 0; col < COLS; col++) 
        	{
        		Point point = matrix[row][col];
                System.out.print(point);
            }
            System.out.println();
        }
        System.out.println();    	
    }

	private boolean isValid(int row, int col) 
    {
    	return (0 <= row && row < ROWS) &&
               (0 <= col && col < COLS) &&
               matrix[row][col].status == Grid.PATH;
    }
    
    private boolean end(int row, int col) 
    {
        return (row == ROWS-1 && col == COLS-1);
    }

	public int getRows()
    {
	    return ROWS;
    }

	public int getCols()
    {
	    return COLS;
    }

	public Point getPoint(int row, int col)
    {
	    return matrix[row][col];
    }

}
