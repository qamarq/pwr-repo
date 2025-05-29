package lab9;

import java.util.ArrayList;
import java.util.List;

class TrieDictionary<V> {

    private static class TrieNode<V> {
        V value;
        char character;
        TrieNode<V> firstChild;
        TrieNode<V> nextSibling;

        TrieNode(char character) {
            this.character = character;
            this.value = null;
            this.firstChild = null;
            this.nextSibling = null;
        }
    }

    private TrieNode<V> root;

    public TrieDictionary() {
        this.root = new TrieNode<>('\0');
    }

    public V insert(String key, V value) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }

        TrieNode<V> currentNode = root;

        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            TrieNode<V> childNode = findChildNode(currentNode, ch);

            if (childNode == null) {
                childNode = new TrieNode<>(ch);
                if (currentNode.firstChild == null) {
                    currentNode.firstChild = childNode;
                } else {
                    TrieNode<V> sibling = currentNode.firstChild;
                    TrieNode<V> prevSibling = null;
                    while (sibling != null && sibling.character < ch) {
                        prevSibling = sibling;
                        sibling = sibling.nextSibling;
                    }
                    if (prevSibling == null) {
                        childNode.nextSibling = currentNode.firstChild;
                        currentNode.firstChild = childNode;
                    } else if (sibling == null) {
                        prevSibling.nextSibling = childNode;
                    } else {
                        prevSibling.nextSibling = childNode;
                        childNode.nextSibling = sibling;
                    }
                }
            }
            currentNode = childNode;
        }

        V oldValue = currentNode.value;
        currentNode.value = value;
        return oldValue;
    }

    private TrieNode<V> findChildNode(TrieNode<V> parentNode, char character) {
        TrieNode<V> currentChild = parentNode.firstChild;
        while (currentChild != null) {
            if (currentChild.character == character) {
                return currentChild;
            }
            if (currentChild.character > character) {
                return null;
            }
            currentChild = currentChild.nextSibling;
        }
        return null;
    }

    public V search(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        TrieNode<V> currentNode = findNode(key);
        return (currentNode != null && currentNode.value != null) ? currentNode.value : null;
    }

    private TrieNode<V> findNode(String key) {
        if (key == null) return null;
        TrieNode<V> currentNode = root;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            TrieNode<V> childNode = findChildNode(currentNode, ch);
            if (childNode == null) {
                return null;
            }
            currentNode = childNode;
        }
        return currentNode;
    }

    public V remove(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }

        TrieNode<V> nodeToRemoveValue = findNode(key);
        if (nodeToRemoveValue == null || nodeToRemoveValue.value == null) {
            return null;
        }

        V oldValue = nodeToRemoveValue.value;
        nodeToRemoveValue.value = null;

        removeUnusedNodes(root, key, 0);

        return oldValue;
    }

    private boolean hasChildren(TrieNode<V> node) {
        return node.firstChild != null;
    }

    private boolean removeUnusedNodes(TrieNode<V> currentNode, String key, int depth) {
        if (currentNode == null) {
            return false;
        }

        if (depth == key.length()) {
            return currentNode.value == null && !hasChildren(currentNode);
        }

        char ch = key.charAt(depth);
        TrieNode<V> childNode;
        TrieNode<V> prevSibling = null;
        TrieNode<V> currentSibling = currentNode.firstChild;

        while (currentSibling != null && currentSibling.character != ch) {
            prevSibling = currentSibling;
            currentSibling = currentSibling.nextSibling;
        }
        childNode = currentSibling;

        if (childNode == null) {
            return false;
        }

        if (removeUnusedNodes(childNode, key, depth + 1)) {
            if (prevSibling == null) {
                currentNode.firstChild = childNode.nextSibling;
            } else {
                prevSibling.nextSibling = childNode.nextSibling;
            }
            return currentNode.value == null && !hasChildren(currentNode) && currentNode != root;
        }
        return false;
    }

    boolean containsKey(String key) {
        return findNode(key) != null;
    }

    boolean hasValueAtKey(String key) {
        TrieNode<V> node = findNode(key);
        return node != null && node.value != null;
    }

    public List<String> suggest(String prefix) {
        List<String> suggestions = new ArrayList<>();
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        }
        TrieNode<V> node = findNode(prefix);
        if (node == null) {
            return suggestions;
        }
        StringBuilder sb = new StringBuilder(prefix);
        collectSuggestions(node, sb, suggestions);
        return suggestions;
    }

    private void collectSuggestions(
            TrieNode<V> node,
            StringBuilder prefix,
            List<String> suggestions
    ) {
        if (node.value != null) {
            suggestions.add(prefix.toString());
        }
        TrieNode<V> child = node.firstChild;
        while (child != null) {
            prefix.append(child.character);
            collectSuggestions(child, prefix, suggestions);
            prefix.deleteCharAt(prefix.length() - 1);
            child = child.nextSibling;
        }
    }
}