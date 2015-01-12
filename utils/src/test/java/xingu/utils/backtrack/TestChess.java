package xingu.utils.backtrack;

import xingu.utils.backtrack.ChessBoard;
import junit.framework.TestCase;

public class TestChess
    extends TestCase
{
	public void testChess()
		throws Exception
	{
		ChessBoard board = new ChessBoard(4);
		assertEquals(15,board.letGoPig());
	}
}
