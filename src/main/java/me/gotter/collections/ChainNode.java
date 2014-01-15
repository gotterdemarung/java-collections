package me.gotter.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Special collection, containing either hash map, either list, either plain
 * object
 * 
 */
public class ChainNode implements Map<String, ChainNode>, Iterable<ChainNode> {

	/**
	 * Scalar internal value Only one of the values can be set at time
	 */
	protected Object valueObject = null;

	/**
	 * Hash table internal value Only one of the values can be set at time
	 */
	protected Map<String, ChainNode> valueHash = null;

	/**
	 * Array internal value Only one of the values can be set at time
	 */
	protected List<ChainNode> valueArray = null;

	/////////////////////////    Constructors    /////////////////////////
	/**
	 * Creates new empty node
	 */
	public ChainNode() {
	}

	/**
	 * Creates new node with provided object as value
	 * 
	 * @param value data
	 */
	public ChainNode(Object value) {
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

	/////////////////////////     Validators    /////////////////////////
	/**
	 * @return True if value of node is null (not just empty, but null exactly)
	 */
	public boolean isNull() {
		return valueObject == null && valueArray == null && valueHash == null;
	}

	/**
	 * @return True if value of ChainNode is scalar
	 */
	protected boolean isNotNullScalar() {
		return valueObject != null;
	}

	/**
	 * @return True if value is boolean
	 */
	public boolean isBool() {
		return isNotNullScalar() && valueObject instanceof Boolean;
	}

	/**
	 * @return True if value is boolean and == true
	 */
	public boolean isTrue() {
		return isBool() && getBool();
	}

	/**
	 * @return True if value instance of String
	 */
	public boolean isString() {
		return isNotNullScalar() && valueObject instanceof String;
	}

	/**
	 * @return True if value is int
	 */
	public boolean isInt() {
		return isNotNullScalar() && valueObject instanceof Integer;
	}

	/**
s	 * @return True if value is long
	 */
	public boolean isLong() {
		return isNotNullScalar() && (isInt() || valueObject instanceof Long);
	}

	/**
	 * @return True if value is float
	 */
	public boolean isFloat() {
		return isNotNullScalar() && valueObject instanceof Float;
	}

	/**
	 * @return True if value is double
	 */
	public boolean isDouble() {
		return isNotNullScalar()
				&& (isFloat() || valueObject instanceof Double);
	}

	/**
	 * Returns true if ChainNode content can be iterated in foreach statement
	 * Can be either map or list
	 * 
	 * @return Check result
	 */
	public boolean isIterable() {
		return valueHash != null || valueArray != null;
	}

	/**
	 * @return True if ChainNode content is list
	 */
	public boolean isList() {
		return valueArray != null;
	}

	/**
	 * @return True if ChainNode content is hash map
	 */
	public boolean isMap() {
		return valueHash != null;
	}

	/////////////////////////    Getters    /////////////////////////
	/**
	 * @return value, contained in ChainNode
	 */
	public Object get() {
		return valueObject;
	}

	/**
	 * @return boolean value, stored in ChainNode
	 */
	public boolean getBool() {
		return (Boolean) valueObject;
	}

	/**
	 * @return toString representation of the content of ChainNode
	 */
	public String getString() {
		if (isNull()) {
			return "";
		}
		return valueObject.toString();
	}

	/**
	 * @return integer value, stored in ChainNode
	 */
	public int getInt() {
		return (Integer) valueObject;
	}

	/**
	 * @return long or integer value, stored in ChainNode
	 */
	public long getLong() {
		if (isInt()) {
			return getInt();
		}
		return (Long) valueObject;
	}

	/**
	 * @return float value, stored in ChainNode
	 */
	public float getFloat() {
		return (Float) valueObject;
	}

	/**
	 * @return float or double value, stored in ChainNode
	 */
	public double getDouble() {
		if (isFloat()) {
			return getFloat();
		}
		return (Double) valueObject;
	}

	/**
	 * Returns node by path, provided dot-separated string
	 * 
	 * @param path Dot-separated path
	 * @return node
	 */
	public ChainNode path(String path) {
		if (path == null || path.length() == 0) {
			throw new NullPointerException();
		}

		return path(path.split("\\."));
	}

	/**
	 * Returns node by path, provided as string array
	 * 
	 * @param path Array path
	 * @return node
	 */
	public ChainNode path(String[] path) {
		if (path == null || path.length == 0) {
			throw new NullPointerException();
		}

		return path(path, 0);
	}

	/**
	 * Internal method to perform path-search
	 * 
	 * @param path Path
	 * @param offset Offset
	 * @return node
	 */
	protected ChainNode path(String[] path, int offset) {
		if (path.length - 1 == offset) {
			return get(path[offset]);
		}

		return get(path[offset]).path(path, offset + 1);
	}

	/////////////////////////    Setter    /////////////////////////
	/**
	 * Replaces value of the node with provided one
	 * 
	 * @param value Value
	 * @return Current node
	 */
	public ChainNode set(Object value) {
		// Erasing
		clear();

		// Setting new value
		if (value == null) {
			this.valueObject = null;
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
			HashMap<?, ?> hm = (HashMap<?, ?>) value;
			for (Object key : hm.keySet()) {
				this.valueHash.put(key.toString(), new ChainNode(value));
			}
		} else {
			this.valueObject = value;
		}
		return this;
	}

	/**
	 * If ChainNode is a map, replaces or sets value by its key
	 * 
	 * @param key   Key
	 * @param value Value
	 * @return Current node
	 */
	public ChainNode set(String key, Object value) {
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

	/////////////////////////    Map interface    /////////////////////////
	
	/**
	 * Removes all content from ChainNode
	 */
	@Override
	public void clear() {
		valueObject = null;
		valueArray = null;
		valueHash = null;
	}

	@Override
	public boolean containsKey(Object key) {
        return key != null
                && isMap()
                && valueHash.containsKey(key)
        ;
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

	@SuppressWarnings("NullableProblems")
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

	@SuppressWarnings("NullableProblems")
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

	@SuppressWarnings("NullableProblems")
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

	@SuppressWarnings("NullableProblems")
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

	/////////////////////////    Iterator interface    /////////////////////////
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

	/////////////////////////    Java core    /////////////////////////
	@Override
	public String toString() {
		if (isNull()) {
			return "";
		}
		return get().toString();
	}

	@Override
	public int hashCode() {
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
	public boolean equals(Object o) {
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
