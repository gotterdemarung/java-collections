package me.gotter.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChainNode implements Map<String, ChainNode>, Iterable<ChainNode>{

	/**
	 * Internal value
	 */
	protected Object value = null;
	
	/**
	 * Special flag, indicating, that at the moment value of chain node is native
	 * i.e. contains set of other ChainNodes
	 */
	private boolean isNative = false;
	
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
			isNative = source.isNative;
			this.value = source.value;
		} else {
			this.value = value; 
		}
	}
	
	// Validators
	/**
	 * Returns true if value of node is null (not just empty, but null exactly)
	 * @return
	 */
	public boolean isNull()
	{
		return value == null;
	}

	/**
	 * Returns true if value is boolean
	 * @return
	 */
	public boolean isBool()
	{
		return !isNull() && value instanceof Boolean;
	}
	
	/**
	 * Returns true if value is boolean and == true
	 * @return
	 */
	public boolean isTrue()
	{
		return isBool() && ((Boolean) value).booleanValue();
	}
	
	/**
	 * Returns true if value instance of String
	 * 
	 * @return
	 */
	public boolean isString()
	{
		return !isNull() && value instanceof String;
	}

	/**
	 * Returns true if value is int
	 * 
	 * @return
	 */
	public boolean isInt()
	{
		return !isNull() && value instanceof Integer;
	}
	
	/**
	 * Returns true if value is long
	 * 
	 * @return
	 */
	public boolean isLong()
	{
		return !isNull() && (isInt() || value instanceof Long);
	}
	
	/**
	 * Returns true if value is float
	 * @return
	 */
	public boolean isFloat()
	{
		return !isNull() && value instanceof Float;
	}
	
	/**
	 * Returns true if value is double
	 * 
	 * @return
	 */
	public boolean isDouble()
	{
		return !isNull() && (isFloat() || value instanceof Double);
	}
	
	/**
	 * Returns true if ChainNode content can be iterated in foreach statement
	 * 
	 * @return
	 */
	public boolean isIterable()
	{
		return !isNull() && (isMap() || value instanceof Object[] || value instanceof Iterable<?>);
	}
	
	/**
	 * Returns true if ChainNode content is Collection
	 * 
	 * @return
	 */
	public boolean isCollection()
	{
		return !isNull() && value instanceof Collection<?>;
	}
	
	/**
	 * Returns true if ChainNode content is hash map
	 * 
	 * @return
	 */
	public boolean isMap()
	{
		return !isNull() && value instanceof Map<?,?>;
	}
	
	/**
	 * Returns true if ChainNode content is ChainNode
	 * 
	 * @return
	 */
	public boolean isChainNode()
	{
		return !isNull() && value instanceof ChainNode;
	}
	
	/**
	 * Returns true if ChainNode contains list of other ChainNodes
	 * 
	 * @return
	 */
	public boolean isChainNodeIterable()
	{
		return isNative && isIterable();
	}
	
	/**
	 * Returns true if ChainNode is hash map
	 * 
	 * @return
	 */
	public boolean isChainNodeMap()
	{
		return isNative && isMap();
	}
	
	
	// Getters
	public Object get()
	{
		return value;
	}
	
	public boolean getBool()
	{
		return ((Boolean) value).booleanValue();
	}
	
	public String getString()
	{
		return value.toString();
	}
	
	public int getInt()
	{
		return ((Integer) value).intValue();
	}
	
	public long getLong()
	{
		if (isInt()) {
			return getInt();
		}
		return ((Long) value).longValue();
	}
	
	public float getFloat()
	{
		return ((Float) value).floatValue();
	}
	
	public double getDouble()
	{
		if (isFloat()) {
			return getFloat();
		}
		return ((Double) value).doubleValue();
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
		this.value = value;
		this.isNative = false;
		return this;
	}
	public ChainNode set(String key, Object value)
	{
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
		value = null;
	}
	
	@Override
	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}
		return mapReference().containsKey(key);
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
		if (!containsKey(key)) {
			ChainNode emptyNode = new ChainNode();
			put(key.toString(), emptyNode);
			return emptyNode;
		}
		return mapReference().get(key);
	}

	@Override
	public ChainNode put(String key, ChainNode value) {
		return mapReference().put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends ChainNode> m) {
		mapReference().putAll(m);
	}

	@Override
	public Set<String> keySet() {
		return mapReference().keySet();
	}

	@Override
	public Collection<ChainNode> values() {
		return mapReference().values();
	}

	@Override
	public Set<java.util.Map.Entry<String, ChainNode>> entrySet() {
		return mapReference().entrySet();
	}
	
	@Override
	public int size() {
		if (isNull()) {
			return 0;
		}
		if (isMap()) {
			return ((Map<?,?>) value).size(); 
		}
		if (value instanceof Object[]) {
			return ((Object[]) value).length;
		}
		
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public ChainNode remove(Object key) {
		return mapReference().remove(key);
	}
	
	// Iterator
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<ChainNode> iterator() {
		if (isNull()) {
			throw new RuntimeException("Node is empty");
		}
		if (!isIterable()) {
			return Arrays.asList(new ChainNode(this.value)).iterator();
		}
		if (isChainNodeMap()) {
			return ((Map<String, ChainNode>) value).values().iterator();
		}
		
		return ((Iterable<ChainNode>) value).iterator();
	}
	
	
	// Internal helpers
	@SuppressWarnings("unchecked")
	private Map<String, ChainNode> mapReference()
	{
		if (isChainNodeMap()){
			// Already done
		} 
		else if (isNull()) {
			value = new HashMap<String, ChainNode>();
			isNative = true;
		} else {
			throw new RuntimeException("Cannot create map for " + value.getClass());
		}
		return (Map<String, ChainNode>) value;
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
		if (isNull()) {
			return 0;
		}
		
		return get().hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		ChainNode cn = new ChainNode(o);
		if (this.value == null) {
			return cn.value == null;
		}
		
		return this.value.equals(o);
	}
}
