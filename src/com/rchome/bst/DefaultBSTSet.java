package com.rchome.bst;

import java.util.Comparator;

/**
 * Created by Raymond on 12/9/2014.
 */
public class DefaultBSTSet<K> extends BinarySearchTreeSet<K> {
    public DefaultBSTSet() {
        super(new DefaultBST());
    }

    public DefaultBSTSet(Comparator<? super K> comparator) {
        super(new DefaultBST(comparator));
    }
}
