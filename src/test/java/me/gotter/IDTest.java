package me.gotter;

import org.junit.Assert;
import org.junit.Test;

public class IDTest
{
    @Test
    public void testConstructor()
    {
        Assert.assertEquals("15", new ID(15).toString());
        Assert.assertEquals("3214", new ID(3214L).toString());
        Assert.assertEquals("82", new ID("82").toString());
        Assert.assertEquals("uuid-18", new ID(new ID("uuid-18")).toString());
        Assert.assertEquals("13.0", new ID(13f).toString());
    }

    @Test(expected = NullPointerException.class)
    public void failConstructor()
    {
        String x = null;
        new ID(x);
    }

    @Test
    public void testToLong()
    {
        Assert.assertEquals(16L, new ID(16).toLong());
        Assert.assertEquals(1234567L, new ID(1234567).toLong());
        Assert.assertEquals(-400L, new ID("-400").toLong());
    }

    @Test(expected = NumberFormatException.class)
    public void failToLong1()
    {
        new ID(15f).toLong();
    }

    @Test(expected = NumberFormatException.class)
    public void failToLong2()
    {
        new ID("abc").toLong();
    }

    @Test
    public void testToInt()
    {
        Assert.assertEquals(16, new ID(16L).toInt());
        Assert.assertEquals(1234567, new ID(1234567).toInt());
        Assert.assertEquals(-400, new ID("-400").toInt());
    }

    @Test(expected = NumberFormatException.class)
    public void failToInt1()
    {
        new ID(15f).toInt();
    }

    @Test(expected = NumberFormatException.class)
    public void failToInt2()
    {
        new ID("abc").toInt();
    }

    @Test(expected = NumberFormatException.class)
    public void failToIntOverflow()
    {
        ID big = new ID(Integer.MAX_VALUE + "000");
        // Normal
        Assert.assertEquals(Integer.MAX_VALUE * 1000L, big.toLong());
        // Exception
        big.toInt();
    }

    @Test
    public void testHashCode()
    {
        Assert.assertEquals("77".hashCode(), new ID(77).hashCode());
        Assert.assertEquals("-732648".hashCode(), new ID("-732648").hashCode());
    }

    @Test
    public void testEquals()
    {
        Assert.assertTrue(new ID(15).equals(new ID("15")));
        Assert.assertTrue(new ID(15).equals(new ID(15L)));
        Assert.assertFalse(new ID(15).equals(new ID(15f)));
        Assert.assertFalse(new ID(15).equals(new ID(14)));

        Assert.assertTrue(new ID(88).equals(88));
        Assert.assertTrue(new ID(-420).equals("-420"));
    }

    @Test
    public void testSpecialEquals()
    {
        Assert.assertTrue(ID.NEW instanceof ID);
        Assert.assertTrue(ID.EMPTY instanceof ID);
        Assert.assertFalse(new ID(Integer.MIN_VALUE).equals(ID.NEW));
        Assert.assertFalse(new ID(Integer.MIN_VALUE).equals(ID.EMPTY));
    }

    @Test
    public void testSpecial()
    {
        Assert.assertTrue(ID.NEW.isSpecial());
        Assert.assertTrue(ID.NEW.isNew());
        Assert.assertFalse(ID.NEW.isEmpty());
        Assert.assertTrue(ID.EMPTY.isSpecial());
        Assert.assertFalse(ID.EMPTY.isNew());
        Assert.assertTrue(ID.EMPTY.isEmpty());
    }
}
