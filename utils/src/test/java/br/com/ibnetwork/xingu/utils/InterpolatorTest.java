package br.com.ibnetwork.xingu.utils;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class InterpolatorTest
{
    @Test
    public void testPlain()
    {
        String s = "just a plain string";
        assertEquals(s, Interpolator.interpolate(s, null));
    }

    @Test
    public void testSimple()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("1", "one");
        map.put("2", "two");
        map.put("3", "three");
        assertEquals("one two three", Interpolator.interpolate("${1} ${2} ${3}", map));
        assertEquals("two one three", Interpolator.interpolate("${2} ${1} ${3}", map));
        assertEquals("three one", Interpolator.interpolate("${3} ${1}", map));
        assertEquals("0 one 2", Interpolator.interpolate("0 ${1} 2", map));
    }

    @Test
    public void testAlternative()
    {
        String expression = "hi ${name?world}!";
        Map<String, String> map = new HashMap<String, String>();
        assertEquals("hi world!", Interpolator.interpolate(expression, map));
        map.put("name", "leandro");
        assertEquals("hi leandro!", Interpolator.interpolate(expression, map));
        
        map.remove("name");
        map.put("default.value", "rodrigo");
        assertEquals("hi world!", Interpolator.interpolate(expression, map));
        assertEquals("hi rodrigo!", Interpolator.interpolate("hi ${name?${default.value}}!", map));
    }

    @Test
    public void testNested()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "ab");
        map.put("b", "cd");
        map.put("acd", "zzz");
        assertEquals("abcd", Interpolator.interpolate("${a}${b}", map));
        assertEquals("zzz", Interpolator.interpolate("${a${b}}", map));
        map.remove("b");
        assertEquals("ab", Interpolator.interpolate("${a${b?}}", map));
        assertEquals("zzz", Interpolator.interpolate("${a${b?cd}}", map));
    }
}
