package me.gotter.collections;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ChainNodeTest {

	static final Double DOUBLE_EPSILON = 0.0000001;
	
	@Test
	public void testNull()
	{
		ChainNode x = new ChainNode();
		assertTrue(x.equals(null));
		assertTrue(x.isNull());
		
		assertFalse(x.isBool());
		assertFalse(x.isInt());
		assertFalse(x.isLong());
		assertFalse(x.isString());
		assertFalse(x.isFloat());
		assertFalse(x.isDouble());
		assertFalse(x.isIterable());
		assertFalse(x.isList());
		assertFalse(x.isMap());

		x = new ChainNode(null);
		assertTrue(x.equals(null));
		assertTrue(x.isNull());
		
		assertFalse(x.isBool());
		assertFalse(x.isInt());
		assertFalse(x.isLong());
		assertFalse(x.isString());
		assertFalse(x.isFloat());
		assertFalse(x.isDouble());
		assertFalse(x.isIterable());
		assertFalse(x.isList());
		assertFalse(x.isMap());
	}

	@Test
	public void testBoolean()
	{
		assertFalse((new ChainNode(true)).isNull());
		assertFalse((new ChainNode(false)).isNull());
		
		assertFalse((new ChainNode(true)).isEmpty());
		assertFalse((new ChainNode(false)).isEmpty());
		
		assertTrue((new ChainNode(true)).isBool());
		assertTrue((new ChainNode(false)).isBool());
		
		assertTrue((new ChainNode(true)).isTrue());
		assertFalse((new ChainNode(false)).isTrue());
		
		assertTrue((new ChainNode(true)).getBool());
		assertFalse((new ChainNode(false)).getBool());
		
		assertEquals("false", (new ChainNode(false)).getString());
		assertEquals("true", (new ChainNode(true)).getString());
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(new ChainNode().equals(null));
		
		assertTrue(new ChainNode(true).equals(true));
		assertFalse(new ChainNode(true).equals(false));
		assertFalse(new ChainNode(true).equals(1));
		
		assertTrue(new ChainNode(22).equals(22));
		assertTrue(new ChainNode(33f).equals(33f));
		
		assertTrue(new ChainNode("str").equals("str"));
		assertTrue(new ChainNode("str").equals(new ChainNode("str")));
		assertFalse(new ChainNode("str").equals(true));
	}
	
	
	@Test
	public void testInt()
	{
		assertTrue((new ChainNode(5)).isInt());
		assertFalse((new ChainNode(5L)).isInt());
		assertFalse((new ChainNode(0)).isNull());
		assertFalse((new ChainNode(0)).isEmpty());
		
		assertEquals(5, (new ChainNode(5)).getInt());
		assertEquals("5", (new ChainNode(5)).getString());
	}
	
	@Test
	public void testLong()
	{
		assertTrue((new ChainNode(5)).isLong()); // Any int is long
		assertTrue((new ChainNode(5L)).isLong());
		assertFalse((new ChainNode(0)).isNull());
		assertFalse((new ChainNode(0)).isEmpty());
		
		assertEquals(5L, (new ChainNode(5)).getLong());
		assertEquals(5L, (new ChainNode(5L)).getLong());
		assertEquals("5", (new ChainNode(5)).getString());
	}
	
	@Test
	public void testFloat()
	{
		assertTrue((new ChainNode(5f)).isFloat());
		assertFalse((new ChainNode(.5)).isFloat()); // Double
		assertFalse((new ChainNode(5f)).isNull());
		assertFalse((new ChainNode(5f)).isEmpty());
		
		assertEquals(0.01f, (new ChainNode(0.01f)).getFloat(), 0);
	}
	
	@Test
	public void testDouble()
	{
		assertTrue((new ChainNode(5f)).isDouble()); // Any float is double
		assertTrue((new ChainNode(.5)).isDouble());
		assertFalse((new ChainNode(.5)).isNull());
		assertFalse((new ChainNode(.5)).isEmpty());
		
		assertEquals(0.01, (new ChainNode(0.01f)).getDouble(), DOUBLE_EPSILON);
		assertEquals(0.01, (new ChainNode(0.01)).getDouble(), 0);
	}
	
	@Test
	public void testString()
	{
		assertEquals("", new ChainNode().toString());
		assertEquals("true", new ChainNode(true).toString());
		assertEquals("false", new ChainNode(false).toString());
		assertEquals("5", new ChainNode(5).toString());
		assertEquals("0.12345", new ChainNode(.12345).toString());
		assertEquals("x", new ChainNode('x').toString());
		assertEquals("The string", new ChainNode("The string").toString());
	}
	
	@Test
	public void testClear()
	{
		ChainNode some = new ChainNode(10);
		assertFalse(some.isNull());
		some.clear();
		assertTrue(some.isNull());
	}
	
	@Test
	public void testHashCode()
	{
		assertEquals(0, new ChainNode().hashCode());
		assertEquals("10".hashCode(), new ChainNode("10").hashCode());
		assertEquals(new Integer(5).hashCode(), new ChainNode(5).hashCode());
		assertEquals(new Float(5).hashCode(), new ChainNode(5f).hashCode());
	}
	
	@Test
	public void testSetter()
	{
		ChainNode x = new ChainNode();
		assertTrue(x.set(5).isInt());
		assertTrue(x.set("5").isString());
	}
	
	@Test
	public void testMap()
	{
		ChainNode x = new ChainNode();
		assertFalse(x.isMap());
		assertTrue(x.isEmpty());
		assertEquals(0, x.size());
		
		x.set("user", "gotterdemarung");
		x.set("userId", 276351);
				
		assertEquals(2, x.size());
		assertFalse(x.isEmpty());
		assertTrue(x.isMap());
		assertTrue(x.isIterable());
		
		assertTrue(x.get("userId").isInt());
		assertTrue(x.get("user").isString());
		
		assertTrue(x.containsKey("user"));
		assertFalse(x.containsKey("password"));
		
		assertFalse(x.containsKey("access"));
		x.put("access", new ChainNode(true));
		assertEquals(3, x.size());
		assertTrue(x.containsKey("access"));
		assertTrue(x.get("access").isTrue());
		x.remove("access");
		assertEquals(2, x.size());
		assertFalse(x.containsKey("access"));
		
		try {
			x.containsValue("some");
			fail("Expecting exception");
		} catch (RuntimeException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testPath()
	{
		ChainNode x = new ChainNode();
		x.set("level11", true);
		x.set("level12", new ChainNode());
		x.get("level12").set("level2", new ChainNode());
		x.get("level12").get("level2").set("integer", 354);
		
		assertTrue(x.path("level11").isTrue());
		assertTrue(x.path("level12.level2.integer").equals(354));
	}
	
	@Test
	public void testIterator()
	{
		ChainNode x = new ChainNode();
		
		x.set(new String[]{"one", "two", "three"});
		assertFalse(x.isMap());
		assertTrue(x.isIterable());
		assertEquals(3, x.size());
		
		boolean one = false, two = false, three = false;
		for (ChainNode c : x) {
			if (c.equals("one")) one = true;
			if (c.equals("two")) two = true;
			if (c.equals("three")) three = true;
		}
		assertTrue(one && two && three);

        x = new ChainNode();
        x.add("one");
        x.add("two");
        x.add(3);
        assertFalse(x.isMap());
        assertTrue(x.isIterable());
        assertEquals(3, x.size());

        one = false; two = false; three = false;
        for (ChainNode c : x) {
            if (c.equals("one")) one = true;
            if (c.equals("two")) two = true;
            if (c.equals(3)) three = true;
        }
        assertTrue(one && two && three);
	}
	
}
