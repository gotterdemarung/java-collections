import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import me.gotter.collections.ChainNode;

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
		assertFalse(x.isChainNode());
		assertFalse(x.isIterable());
		assertFalse(x.isCollection());
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
		assertFalse(x.isChainNode());
		assertFalse(x.isIterable());
		assertFalse(x.isCollection());
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
}
