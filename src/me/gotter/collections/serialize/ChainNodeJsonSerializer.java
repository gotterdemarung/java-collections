package me.gotter.collections.serialize;

import me.gotter.collections.ChainNode;

public class ChainNodeJsonSerializer 
{
	static public final byte OBJECT_BEGIN = '{';
	static public final byte OBJECT_END = '}';
	static public final byte ARRAY_BEGIN = '[';
	static public final byte ARRAY_END = ']';
	static public final byte QUOTE = '"';
	static public final byte KEY_VALUE_SEPARATOR = ':';
	static public final byte LIST_SEPARATOR = ',';
	
	public static ChainNode fromJson(String json)
	{
		if (json == null) {
			return null;
		}
		byte[] bytes = json.getBytes();
		
		return getJsonPart(bytes, 0);
	}
	
	private static ChainNode getJsonPart(byte[] bytes, int offset)
	{
		ChainNode answer = new ChainNode();
		// Main loop
		for (int i = offset; i < bytes.length; i++) {
			
		}
	}
}
