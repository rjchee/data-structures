package com.rchome.bst;

import java.util.Comparator;

/**
 * Created by Raymond on 12/9/2014.
 */
public class DefaultBST<K, V> extends BinarySearchTree<K, V> {

    public DefaultBST() {}

    public DefaultBST(Comparator<? super K> comparator) {
        super(comparator);
    }

    @Override
    protected void findOperation(Node current) {
        // no-op
    }

    @Override
    protected Node insertNode(Node node) {
        if(root == null) {
            root = node;
            return null;
        }
        Node current = root;
        K key = node.getKey();
        while(true) {
            int compare = compare(key, current.getKey());
            if(compare < 0) {
                if(current.left == null) {
                    current.left = node;
                    node.parent = current;
                    break;
                }

                current = current.left;
            }
            else if(compare > 0) {
                if(current.right == null) {
                    current.right = node;
                    node.parent = current;
                    break;
                }

                current = current.right;
            }
            else {
                if(current == root) {
                    root = node;
                }
                else if(current.isLeftChild()) {
                    current.parent.left = node;
                }
                else {
                    current.parent.right = node;
                }

                node.parent = current.parent;
                if(current.left != null) {
                    node.left = current.left;
                    current.left.parent = node;
                }
                if(current.right != null) {
                    node.right = current.right;
                    current.right.parent = node;
                }

                return current;
            }
        }
        return null;
    }

    @Override
    protected void removeNode(Node node) {
        if(node.left != null && node.right != null) {
            Node current = node.right;
            while(current.left != null) {
                current = current.left;
            }
            node.setKey(current.getKey());
            node.setValue(current.getValue());
            removeNode(current);
        }
        else {
            Node newChild = node.left == null ? node.right == null ? null : node.right : node.left;
            if(node == root) {
                root = newChild;
            }
            else if(node.isLeftChild()) {
                node.parent.left = newChild;
            }
            else {
                node.parent.right = newChild;
            }

            if(newChild != null) {
                newChild.parent = node.parent;
            }
        }
    }
}
