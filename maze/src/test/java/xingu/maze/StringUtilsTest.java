package xingu.maze;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import xingu.maze.Utils;

public class StringUtilsTest
{
    @Test
    public void testReadLines() 
        throws Exception
    {
        String input = "\n"
            + "junk\n"
            + "# not a comment\n"
            + "\n"
            + "$$ comment\n"
            + "$$ comment\n"
            + "junk";
            
        InputStream is = new ByteArrayInputStream(input.getBytes());
        List<String> lines = Utils.readLines(is, true, "$$");
        assertEquals(3, lines.size());

        is = new ByteArrayInputStream(input.getBytes());
        lines = Utils.readLines(is, true, "$");
        assertEquals(3, lines.size());

        is = new ByteArrayInputStream(input.getBytes());
        lines = Utils.readLines(is, true, "#");
        assertEquals(4, lines.size());

        is = new ByteArrayInputStream(input.getBytes());
        lines = Utils.readLines(is);
        assertEquals(4, lines.size());
    }
}
