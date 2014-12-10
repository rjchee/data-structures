package com.rchome.bst.test;

import com.rchome.bst.BinarySearchTree;
import com.rchome.bst.DefaultBST;
import org.junit.Before;
import org.junit.Test;

import java.rmi.server.ServerRef;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class BinarySearchTreeTest {
    public static final int TEST_SIZE = 100_000;
    private BinarySearchTree<Integer, Boolean> tree;
    private TreeMap<Integer, Boolean> map;

    @Before
    public void setUp() throws Exception {
        tree = new DefaultBST<>();
        map = new TreeMap<>();
        Random random = new Random();
        for (int i = 0; i < TEST_SIZE; i++) {
            int key = random.nextInt();
            boolean value = random.nextBoolean();
            tree.put(key, value);
            map.put(key, value);
        }
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(map.size(), tree.size());

        Integer i = map.keySet().iterator().next();
        map.remove(i);
        tree.remove(i);
        assertEquals(map.size(), tree.size());
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertEquals(map.isEmpty(), tree.isEmpty());

        map.clear();
        tree.clear();
        assertEquals(map.isEmpty(), tree.isEmpty());
    }

    @Test
    public void testContainsKey() throws Exception {
        for (int i = 0; i < 100_000; i++) {
            assertEquals(String.valueOf(i), map.containsKey(i), tree.containsKey(i));
        }
        for (Integer key : map.keySet()) {
            assertTrue(tree.containsKey(key));
        }
    }

    @Test
    public void testGet() throws Exception {
        for (Integer key : map.keySet()) {
            assertEquals(key.toString(), map.get(key), tree.get(key));
        }
    }

    @Test
    public void testPut() throws Exception {
        for (Integer key : map.keySet()) {
            Boolean value = map.get(key);
            assertEquals(key.toString(), value, tree.put(key, !value));
            assertTrue(tree.containsKey(key));
        }

        int i = 0;
        while(map.containsKey(i)) {
            i++;
        }
        assertNull(tree.put(i, false));
        assertTrue(tree.containsKey(i));
    }

    @Test
    public void testRemove() throws Exception {
        for (Integer key : map.keySet()) {
            Boolean value = map.get(key);
            assertEquals(key.toString(), value, tree.remove(key));
            try {
                assertFalse(tree.containsKey(key));
            } catch (Exception e) {
                System.err.println(tree.toString());
                throw e;
            }
        }

        int i = 0;
        while(map.containsKey(i)) {
            i++;
        }
        assertNull(tree.remove(i));
        assertFalse(tree.containsKey(i));
    }

    @Test
    public void testClear() throws Exception {
        tree.clear();
        map.clear();
        assertEquals(map, tree);
    }

    @Test
    public void testEntrySet() throws Exception {
        System.err.println(map.size());
        assertEquals(map.entrySet(), tree.entrySet());
    }

    @Test
    public void testIterator() throws Exception {
        Iterator<Map.Entry<Integer, Boolean>> it = tree.entrySet().iterator();
        for(Map.Entry<Integer, Boolean> entry : map.entrySet()) {
            assertTrue(it.hasNext());
            assertEquals(String.valueOf(map.size()), entry, it.next());
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testIteratorRemove() throws Exception {
        Iterator<Map.Entry<Integer, Boolean>> it = tree.entrySet().iterator();
        for(Map.Entry<Integer, Boolean> entry : map.entrySet()) {
            it.next();
            it.remove();
            assertFalse(entry.toString() + " " + map.size(), tree.containsKey(entry.getKey()));
        }
        assertTrue(tree.isEmpty());
        assertTrue(tree.size() == 0);
    }

    @Test
    public void testEfficiency() {
        long seed = new Random().nextLong();
        Map<Integer, Integer> treeMap = new TreeMap<>();
        long time = System.currentTimeMillis();
        putStressTest(seed, treeMap);
        System.out.println(System.currentTimeMillis() - time);

        Map<Integer, Integer> defaultBST = new DefaultBST<>();
        time = System.currentTimeMillis();
        putStressTest(seed, defaultBST);
        System.out.println(System.currentTimeMillis() - time);

        getStressTest(seed, treeMap);
        getStressTest(seed, defaultBST);
        removeStressTest(seed, treeMap);
        removeStressTest(seed, defaultBST);
    }

    private static final int STRESS_SIZE = 10_000_000;

    private void putStressTest(long seed, Map<Integer, Integer> map) {
        Random random = new Random(seed);
        for (int i = 0; i < STRESS_SIZE; i++) {
            map.put(random.nextInt(), random.nextInt());
        }
    }

    private void getStressTest(long seed, Map<Integer, Integer> map) {
        long time = System.currentTimeMillis();
        Random random = new Random(seed);
        for (int i = 0; i < STRESS_SIZE; i++) {
            map.get(random.nextInt());
        }
        System.out.println(System.currentTimeMillis() - time);
    }

    private void removeStressTest(long seed, Map<Integer, Integer> map) {
        long time = System.currentTimeMillis();
        Random random = new Random(seed);
        for (int i = 0; i < STRESS_SIZE; i++) {
            map.remove(random.nextInt());
        }
        System.out.println(System.currentTimeMillis() - time);
    }
}