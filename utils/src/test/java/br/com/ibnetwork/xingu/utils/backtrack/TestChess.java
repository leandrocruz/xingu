package br.com.ibnetwork.xingu.utils.backtrack;

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
