package com.rchome.bst;

import java.util.*;

/**
 * Created by Raymond on 12/9/2014.
 */
public abstract class BinarySearchTree<K, V> extends AbstractMap<K, V> {

    protected Node root;
    protected int modCount;
    private Comparator<? super K> comparator;
    private int size;

    private static boolean nullSafeEquals(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    protected class Node implements Entry<K, V> {
        Node left, right, parent;
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        protected Node() {}

        @Override
        public boolean equals(Object o) {
            if(o instanceof Entry) {
                Entry<K, V> e = (Entry<K, V>)o;
                return nullSafeEquals(this.key, e.getKey()) &&
                        nullSafeEquals(this.value, e.getValue());
            }
            return false;
        }

        @Override
        public String toString() {
            return String.format("%s=%s", key, value);
        }

        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry has been
         *                               removed from the backing map.
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * Returns the value corresponding to this entry.  If the mapping
         * has been removed from the backing map (by the iterator's
         * <tt>remove</tt> operation), the results of this call are undefined.
         *
         * @return the value corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry has been
         *                               removed from the backing map.
         */
        @Override
        public V getValue() {
            return value;
        }

        /**
         * Replaces the value corresponding to this entry with the specified
         * value (optional operation).  (Writes through to the map.)  The
         * behavior of this call is undefined if the mapping has already been
         * removed from the map (by the iterator's <tt>remove</tt> operation).
         *
         * @param value new value to be stored in this entry
         * @return old value corresponding to the entry
         * @throws UnsupportedOperationException if the <tt>put</tt> operation
         *                                       is not supported by the backing map
         * @throws ClassCastException            if the class of the specified value
         *                                       prevents it from being stored in the backing map
         * @throws NullPointerException          if the backing map does not permit
         *                                       null values, and the specified value is null
         * @throws IllegalArgumentException      if some property of this value
         *                                       prevents it from being stored in the backing map
         * @throws IllegalStateException         implementations may, but are not
         *                                       required to, throw this exception if the entry has been
         *                                       removed from the backing map.
         */
        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        void setKey(K key) {
            this.key = key;
        }

        boolean isLeftChild() {
            return parent != null && parent.left == this;
        }

        boolean isRightChild() {
            return parent != null && parent.right == this;
        }
    }

    public BinarySearchTree() {}

    public BinarySearchTree(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    /**
     * Returns the number of key-value mappings in this map.  If the
     * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return size;
    }

    public int getHeight() {
        if(root == null) {
            return 0;
        }
        Queue<Node> frontier = new ArrayDeque<>();
        Queue<Integer> heights = new ArrayDeque<>();
        frontier.add(root);
        heights.add(0);
        int maxHeight = 0;
        while(!frontier.isEmpty()) {
            Node current = frontier.remove();
            Integer height = heights.remove();
            if(height > maxHeight) {
                maxHeight = height;
            }
            if(current.left != null) {
                frontier.add(current.left);
                heights.add(height + 1);
            }
            if(current.right != null) {
                frontier.add(current.right);
                heights.add(height + 1);
            }
        }
        return maxHeight;
    }

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.  More formally, returns <tt>true</tt> if and only if
     * this map contains a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    protected Node getNode(Object key) {
        K key1 = (K) key;
        Node current = root;
        while(current != null) {
            findOperation(current);
            int compare = compare(key1, current.key);
            if(compare < 0) {
                current = current.left;
            }
            else if(compare > 0) {
                current = current.right;
            }
            else {
                return current;
            }
        }

        return null;
    }

    protected int compare(K key1, K key2) {
        if(comparator == null) {
            return ((Comparable<? super K>)key1).compareTo(key2);
        }
        return comparator.compare(key1, key2);
    }

    protected abstract void findOperation(Node current);

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     * <p/>
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     * <p/>
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}.  The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     * {@code null} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public V get(Object key) {
        Node node = getNode(key);
        if(node == null) {
            return null;
        }
        return node.getValue();
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A map
     * <tt>m</tt> is said to contain a mapping for a key <tt>k</tt> if and only
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * <tt>true</tt>.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>,
     * if the implementation supports <tt>null</tt> values.)
     * @throws UnsupportedOperationException if the <tt>put</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     * @throws NullPointerException          if the specified key or value is null
     *                                       and this map does not permit null keys or values
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     */
    @Override
    public V put(K key, V value) {
        if(key == null) {
            throw new NullPointerException();
        }
        Node prevNode = insertNode(new Node(key, value));
        if(prevNode == null) {
            size++;
            return null;
        }
        modCount++;
        return prevNode.getValue();
    }

    protected abstract Node insertNode(Node node);

    /**
     * Removes the mapping for a key from this map if it is present
     * (optional operation).   More formally, if this map contains a mapping
     * from key <tt>k</tt> to value <tt>v</tt> such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping
     * is removed.  (The map can contain at most one such mapping.)
     * <p/>
     * <p>Returns the value to which this map previously associated the key,
     * or <tt>null</tt> if the map contained no mapping for the key.
     * <p/>
     * <p>If this map permits null values, then a return value of
     * <tt>null</tt> does not <i>necessarily</i> indicate that the map
     * contained no mapping for the key; it's also possible that the map
     * explicitly mapped the key to <tt>null</tt>.
     * <p/>
     * <p>The map will not contain a mapping for the specified key once the
     * call returns.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the key is of an inappropriate type for
     *                                       this map
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified key is null and this
     *                                       map does not permit null keys
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public V remove(Object key) {
        Node node = getNode(key);
        if(node == null) {
            return null;
        }
        V value = node.getValue();
        modCount++;
        size--;
        removeNode(node);
        return value;
    }

    protected abstract void removeNode(Node node);

    /**
     * Removes all of the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation
     *                                       is not supported by this map
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns a {@link java.util.Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation, or through the
     * <tt>setValue</tt> operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
     * <tt>clear</tt> operations.  It does not support the
     * <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        /**
         * Returns an iterator over the elements contained in this collection.
         *
         * @return an iterator over the elements contained in this collection
         */
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new BinarySearchTreeIterator();
        }

        @Override
        public int size() {
            return BinarySearchTree.this.size();
        }
    }

    private class BinarySearchTreeIterator implements Iterator<Map.Entry<K, V>> {

        private Node next, prev;
        private int expectedModCount;

        public BinarySearchTreeIterator() {
            expectedModCount = modCount;
            next = root;
            while(next != null && next.left != null) {
                next = next.left;
            }
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return next != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws java.util.NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Node next() {
            if(next == null) {
                throw new NoSuchElementException();
            }
            if(expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            prev = next;
            if(next.right == null) {
                while(next.isRightChild()) {
                    next = next.parent;
                }
                next = next.parent;
            }
            else {
                next = next.right;
                while(next.left != null) {
                    next = next.left;
                }
            }
            return prev;
        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.  The behavior of an iterator
         * is unspecified if the underlying collection is modified while the
         * iteration is in progress in any way other than by calling this
         * method.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this iterator
         * @throws IllegalStateException         if the {@code next} method has not
         *                                       yet been called, or the {@code remove} method has already
         *                                       been called after the last call to the {@code next}
         *                                       method
         */
        @Override
        public void remove() {
            if(prev == null) {
                throw new IllegalStateException();
            }
            removeNode(prev);
            size--;
            prev = null;
        }
    }
}
