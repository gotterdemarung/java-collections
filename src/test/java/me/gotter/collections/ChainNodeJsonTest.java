package me.gotter.collections;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ChainNodeJsonTest {

    @Test
    public void testSimple()
    {
        assertEquals("null", new ChainNode().toJSON());
        assertEquals("5", new ChainNode(5).toJSON());
        assertEquals("true", new ChainNode(true).toJSON());
        assertEquals("false", new ChainNode(false).toJSON());
        assertEquals("1.2", new ChainNode(1.2f).toJSON());
        assertEquals("1.2", new ChainNode(1.2).toJSON());
        assertEquals("\"tex\\\"t\"", new ChainNode("tex\"t").toJSON());
    }

    @Test
    public void testList()
    {
        ChainNode cn = new ChainNode();
        cn.add(5);
        cn.add(false);

        assertEquals("[5,false]", cn.toJSON());
    }

    @Test
    public void testMap()
    {
        ChainNode cn = new ChainNode();
        cn.set("id", 12345);
        cn.set("name", "Admin");

        assertEquals("{\"id\":12345,\"name\":\"Admin\"}", cn.toJSON());
    }

    @Test
    public void testDepth()
    {
        ChainNode cn = new ChainNode();
        ChainNode inner = new ChainNode();
        cn.set("id", 12345);
        cn.set("roles", inner);
        inner.add(15);
        inner.add("admin");

        assertEquals("{\"id\":12345,\"roles\":[15,\"admin\"]}", cn.toJSON());
    }

}
