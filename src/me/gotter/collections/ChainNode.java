package me.gotter.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChainNode implements Map<String, ChainNode>, Iterable<ChainNode>{

	/**
	 * Scalar internal value
	 * Only one of the values can be set at time 
	 */
	protected Object valueObject = null;
	
	/**
	 * Hash table internal value
	 * Only one of the values can be set at time
	 */
	protected Map<String, ChainNode> valueHash = null;
	
	/**
	 * Array internal value
	 * Only one of the values can be set at time
	 */
	protected List<ChainNode> valueArray = null;
		
	// Constructors
	/**
	 * Creates new empty node
	 */
	public ChainNode()
	{
	}
	
	/**
	 * Creates new node with provided object as value
	 * 
	 * @param value
	 */
	public ChainNode(Object value)
	{
		if (value instanceof ChainNode) {
			// cloning values
			ChainNode source = (ChainNode) value;
			this.valueObject = source.valueObject;
			this.valueArray = source.valueArray;
			this.valueHash = source.valueHash;
		} else {
			this.set(value);
		}
	}
	
	// Validators
	/**
	 * Returns true if value of node is null (not just empty, but null exactly)
	 * @return
	 */
	public boolean isNull()
	{
		return valueObject == null && valueArray == null && valueHash == null;
	}

	/**
	 * Returns true if value of ChainNode is scalar
	 * 
	 * @return
	 */
	protected boolean isNotNullScalar()
	{
		return valueObject != null;
	}
	
	/**
	 * Returns true if value is boolean
	 * @return
	 */
	public boolean isBool()
	{
		return isNotNullScalar() && valueObject instanceof Boolean;
	}
	
	/**
	 * Returns true if value is boolean and == true
	 * @return
	 */
	public boolean isTrue()
	{
		return isBool() && ((Boolean) valueObject).booleanValue();
	}
	
	/**
	 * Returns true if value instance of String
	 * 
	 * @return
	 */
	public boolean isString()
	{
		return isNotNullScalar() && valueObject instanceof String;
	}

	/**
	 * Returns true if value is int
	 * 
	 * @return
	 */
	public boolean isInt()
	{
		return isNotNullScalar() && valueObject instanceof Integer;
	}
	
	/**
	 * Returns true if value is long
	 * 
	 * @return
	 */
	public boolean isLong()
	{
		return isNotNullScalar() && (isInt() || valueObject instanceof Long);
	}
	
	/**
	 * Returns true if value is float
	 * @return
	 */
	public boolean isFloat()
	{
		return isNotNullScalar() && valueObject instanceof Float;
	}
	
	/**
	 * Returns true if value is double
	 * 
	 * @return
	 */
	public boolean isDouble()
	{
		return isNotNullScalar() && (isFloat() || valueObject instanceof Double);
	}
	
	/**
	 * Returns true if ChainNode content can be iterated in foreach statement
	 * Can be either map or list
	 * 
	 * @return
	 */
	public boolean isIterable()
	{
		return valueHash != null || valueArray != null;
	}
	
	/**
	 * Returns true if ChainNode content is list
	 * 
	 * @return
	 */
	public boolean isList()
	{
		return valueArray != null;
	}
	
	/**
	 * Returns true if ChainNode content is hash map
	 * 
	 * @return
	 */
	public boolean isMap()
	{
		return valueHash != null;
	}
	
	/**
	 * Returns true if ChainNode content is ChainNode
	 * 
	 * @return
	 */
	public boolean isChainNode()
	{
		return !isNull() && valueObject instanceof ChainNode;
	}
	
	
	// Getters
	public Object get()
	{
		return valueObject;
	}
	
	public boolean getBool()
	{
		return ((Boolean) valueObject).booleanValue();
	}
	
	public String getString()
	{
		return valueObject.toString();
	}
	
	public int getInt()
	{
		return ((Integer) valueObject).intValue();
	}
	
	public long getLong()
	{
		if (isInt()) {
			return getInt();
		}
		return ((Long) valueObject).longValue();
	}
	
	public float getFloat()
	{
		return ((Float) valueObject).floatValue();
	}
	
	public double getDouble()
	{
		if (isFloat()) {
			return getFloat();
		}
		return ((Double) valueObject).doubleValue();
	}
	
	/**
	 * Returns node by path, provided dot-separated string
	 * 
	 * @param path
	 * @return
	 */
	public ChainNode path(String path)
	{
		if (path == null || path.length() == 0) {
			throw new NullPointerException();
		}
		
		return path(path.split("\\."));
	}
	
	/**
	 * Returns node by path, provided as string array
	 * 
	 * @param path
	 * @return
	 */
	public ChainNode path(String[] path) 
	{
		if (path == null || path.length == 0) {
			throw new NullPointerException();
		}
		
		return path(path, 0);
	}
	
	/**
	 * Internal method to perform path-search
	 * 
	 * @param path
	 * @param offset
	 * @return
	 */
	protected ChainNode path(String[] path, int offset)
	{
		if (path.length - 1 == offset) {
			return get(path[offset]);
		}
		
		return get(path[offset]).path(path, offset+1);
	}
	
	// Setter
	public ChainNode set(Object value)
	{
		// Erasing
		clear();
		
		// Setting new value
		if (value == null) {
			// Do nothing
		} else if (value instanceof Object[]) {
			this.valueArray = new ArrayList<ChainNode>();
			for (Object o : (Object[]) value) {
				this.valueArray.add(new ChainNode(o));
			}
		} else if (value instanceof Collection<?>) {
			this.valueArray = new ArrayList<ChainNode>();
			for (Object o : (Collection<?>) value) {
				this.valueArray.add(new ChainNode(o));
			}
		} else if (value instanceof HashMap<?, ?>) {
			this.valueHash = new HashMap<String, ChainNode>();
			HashMap<?,?> hm = (HashMap<?, ?>) value;
			for (Object key : hm.keySet()) {
				this.valueHash.put(key.toString(), new ChainNode(value));
			}
		} else {
			this.valueObject = value;
		}
		return this;
	}
	public ChainNode set(String key, Object value)
	{
		if (isNull()) {
			// Creating
			this.valueHash = new HashMap<String, ChainNode>();
		}
		
		if (!isMap()) {
			throw new RuntimeException("ChainNode is not map");
		}
		
		if (value instanceof ChainNode) {
			put(key, (ChainNode) value);
		} else {
			put(key, new ChainNode(value));
		}
		return this;
	}

	// Map interface 	
	@Override
	public void clear() {
		valueObject = null;
		valueArray = null;
		valueHash = null;
	}
	
	@Override
	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}
		if (!isMap()) {
			return false;
		}
		return valueHash.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public ChainNode get(Object key) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (isNull()) {
			valueHash = new HashMap<String, ChainNode>();
		}
		if (!isMap()) {
			throw new RuntimeException("ChainNode not a map");
		}
		if (!containsKey(key)) {
			ChainNode emptyNode = new ChainNode();
			put(key.toString(), emptyNode);
			return emptyNode;
		}
		return valueHash.get(key);
	}

	@Override
	public ChainNode put(String key, ChainNode value) {
		if (isNull()) {
			valueHash = new HashMap<String, ChainNode>();
		}
		if (!isMap()) {
			throw new RuntimeException("ChainNode not a map");
		}
		return valueHash.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends ChainNode> m) {
		if (isNull()) {
			valueHash = new HashMap<String, ChainNode>();
		}
		if (!isMap()) {
			throw new RuntimeException("ChainNode not a map");
		}
		valueHash.putAll(m);
	}

	@Override
	public Set<String> keySet() {
		if (isNull()) {
			return new HashSet<String>();
		}
		if (!isMap()) {
			throw new RuntimeException("ChainNode not a map");
		}
		return valueHash.keySet();
	}

	@Override
	public Collection<ChainNode> values() {
		if (isList()) {
			return valueArray;
		}
		if (isMap()) {
			return valueHash.values();
		}
		return new HashSet<ChainNode>();
	}

	@Override
	public Set<java.util.Map.Entry<String, ChainNode>> entrySet() {
		if (isNull()) {
			this.valueHash = new HashMap<String, ChainNode>();
		}
		if (!isMap()) {
			throw new RuntimeException("Chain node not a map");
		}
		return valueHash.entrySet();
	}
	
	@Override
	public int size() {
		if (isNull()) {
			return 0;
		}
		if (isMap()) {
			return valueHash.size(); 
		}
		if (isList()) {
			return valueArray.size();
		}
		
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public ChainNode remove(Object key) {
		if (isMap()) {
			return valueHash.remove(key);
		}
		return null;
	}
	
	// Iterator
	@Override
	public Iterator<ChainNode> iterator() {
		if (!isIterable()) {
			throw new RuntimeException("Not iterable");
		}
		if (valueArray != null) {
			return valueArray.iterator();
		}
		if (valueHash != null) {
			return valueHash.values().iterator();
		}
		
		throw new RuntimeException();
	}
	
	// Java core
	@Override
	public String toString()
	{
		if (isNull()) {
			return "";
		}
		return get().toString();
	}
	
	@Override
	public int hashCode()
	{
		if (valueObject != null) {
			return valueObject.hashCode();
		}
		if (valueArray != null) {
			return valueArray.hashCode();
		}
		if (valueHash != null) {
			return valueHash.hashCode();
		}
		
		return 0;
	}

	@Override
	public boolean equals(Object o)
	{
		if (isNull() && o == null) {
			return true;
		}
		if (isNull() && o != null) {
			return false;
		}
		if (o instanceof ChainNode) {
			ChainNode cn = (ChainNode) o;
			return (valueObject != null && valueObject.equals(cn.valueObject))
					|| (valueArray != null && valueArray.equals(cn.valueArray))
					|| (valueHash != null && valueHash.equals(cn.valueHash));
		}
		
		return this.equals(new ChainNode(o));
	}
}
