package me.gotter;

/**
 * Structure, representing ID
 * <p/>
 * Stores value in string
 */
public class ID {
    public static final ID NEW;
    public static final ID EMPTY;

    static {
        NEW = new ID(Integer.MIN_VALUE);
        EMPTY = new ID(Integer.MIN_VALUE);
    }


    /**
     * Stored value
     */
    private String id;

    /**
     * Constructs ID by integer
     *
     * @param id
     */
    public ID(int id) {
        this(id + "");
    }

    /**
     * Constructs ID by long
     *
     * @param id
     */
    public ID(long id) {
        this(id + "");
    }

    /**
     * Constructs ID by string
     *
     * @param id
     */
    public ID(String id) {
        this((Object) id);
    }

    /**
     * Constructs ID by any object
     *
     * @param id
     */
    public ID(Object id) {
        if (id == null) {
            throw new NullPointerException("ID cannot be null");
        }
        this.id = id.toString();
    }

    /**
     * Clones ID
     *
     * @param id
     */
    public ID(ID id) {
        if (id == null) {
            throw new NullPointerException("ID cannot be null");
        }
        this.id = id.id;
    }

    /**
     * Returns true if current ID is special ID
     *
     * @return
     */
    public boolean isSpecial() {
        return this == NEW || this == EMPTY;
    }

    /**
     * Returns true if current ID associated with new
     * non existent entry
     *
     * @return
     */
    public boolean isNew() {
        return this == NEW;
    }

    /**
     * Returns true if current ID not valid
     *
     * @return
     */
    public boolean isEmpty() {
        return this == EMPTY;
    }

    /**
     * Returns long representation of ID
     *
     * @return
     * @throws NumberFormatException
     */
    public long toLong() throws NumberFormatException {
        if (isSpecial()) {
            throw new NumberFormatException("Cannot convert special ID to long");
        }
        return Long.parseLong(id);
    }

    /**
     * Returns int representaion of ID
     *
     * @return
     * @throws NumberFormatException
     */
    public int toInt() throws NumberFormatException {
        if (isSpecial()) {
            throw new NumberFormatException("Cannot convert special ID to int");
        }
        return Integer.parseInt(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        // Special rules for special cases
        if (this == NEW || this == EMPTY || o == NEW || o == EMPTY) {
            return false;
        }

        if (!(o instanceof ID)) {
            return this.equals(new ID(o));
        }

        ID id1 = (ID) o;


        if (!id.equals(id1.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        if (isSpecial()) {
            return "";
        }
        return id;
    }
}
