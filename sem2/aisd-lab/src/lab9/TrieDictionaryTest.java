package lab9;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrieDictionaryTest {

    private TrieDictionary<Object> dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new TrieDictionary<>();
    }

    @Test
    void testInsertAndSearch_Simple() {
        assertNull(dictionary.insert("apple", 1));
        assertEquals(1, dictionary.search("apple"));
    }

    @Test
    void testInsert_UpdateValue() {
        dictionary.insert("apple", 1);
        assertEquals(1, dictionary.insert("apple", 2));
        assertEquals(2, dictionary.search("apple"));
    }

    @Test
    void testSearch_NonExistentKey() {
        assertNull(dictionary.search("banana"));
        dictionary.insert("apple", 1);
        assertNull(dictionary.search("apply"));
    }

    @Test
    void testInsert_MultipleKeys_SharedPrefix() {
        assertNull(dictionary.insert("app", 10));
        assertNull(dictionary.insert("apple", 1));
        assertNull(dictionary.insert("apply", 2));

        assertEquals(10, dictionary.search("app"));
        assertEquals(1, dictionary.search("apple"));
        assertEquals(2, dictionary.search("apply"));
        assertNull(dictionary.search("ap"));
    }

    @Test
    void testInsert_DifferentBranches() {
        assertNull(dictionary.insert("cat", 1));
        assertNull(dictionary.insert("car", 2));
        assertNull(dictionary.insert("dog", 3));

        assertEquals(1, dictionary.search("cat"));
        assertEquals(2, dictionary.search("car"));
        assertEquals(3, dictionary.search("dog"));
        assertNull(dictionary.search("can"));
    }

    @Test
    void testInsert_DifferentBranches2() {
        assertNull(dictionary.insert("cat", 1));
        assertNull(dictionary.insert("car", 2));
        assertNull(dictionary.insert("dog", 3));
        assertNull(dictionary.insert("rocket", 50));

        assertEquals(1, dictionary.search("cat"));
        assertEquals(2, dictionary.search("car"));
        assertEquals(3, dictionary.search("dog"));
        assertEquals(50, dictionary.search("rocket"));
        assertNull(dictionary.search("can"));
    }

    @Test
    void testInsert_DifferentBranchesFromList() {
        assertNull(dictionary.insert("baby", 1));
        assertNull(dictionary.insert("bad", 2));
        assertNull(dictionary.insert("bank", 3));
        assertNull(dictionary.insert("box", "slowo"));
        assertNull(dictionary.insert("dad", 506));
        assertNull(dictionary.insert("dance", 560));

        assertEquals(1, dictionary.search("baby"));
        assertEquals(2, dictionary.search("bad"));
        assertEquals(3, dictionary.search("bank"));
        assertEquals("slowo", dictionary.search("box"));
        assertEquals(506, dictionary.search("dad"));
        assertEquals(560, dictionary.search("dance"));
        assertNull(dictionary.search("can"));
    }

    @Test
    void testInsert_LexicographicalOrderOfSiblings() {
        dictionary.insert("b", 1);
        dictionary.insert("a", 2);
        dictionary.insert("c", 3);

        assertEquals(2, dictionary.search("a"));
        assertEquals(1, dictionary.search("b"));
        assertEquals(3, dictionary.search("c"));

        dictionary.insert("banana", 10);
        dictionary.insert("bandana", 20);
        dictionary.insert("ban", 30);

        assertEquals(10, dictionary.search("banana"));
        assertEquals(20, dictionary.search("bandana"));
        assertEquals(30, dictionary.search("ban"));
    }


    @Test
    void testRemove_Simple() {
        dictionary.insert("key1", 100);
        assertEquals(100, dictionary.remove("key1"));
        assertNull(dictionary.search("key1"));
        assertFalse(dictionary.hasValueAtKey("key1"));
    }

    @Test
    void testRemove_NonExistentKey() {
        assertNull(dictionary.remove("nonexistent"));
        dictionary.insert("exists", 1);
        assertNull(dictionary.remove("exist"));
    }

    @Test
    void testRemove_KeyIsPrefixOfAnother() {
        dictionary.insert("app", 1);
        dictionary.insert("apple", 2);

        assertEquals(1, dictionary.remove("app"));
        assertNull(dictionary.search("app"));
        assertEquals(2, dictionary.search("apple"));
    }

    @Test
    void testRemove_SharedPrefix_RemoveLonger() {
        dictionary.insert("app", 1);
        dictionary.insert("apple", 2);

        assertEquals(2, dictionary.remove("apple"));
        assertNull(dictionary.search("apple"));
        assertEquals(1, dictionary.search("app"));
    }

    @Test
    void testRemove_KeyThatCausesNodeDeletion() {
        dictionary.insert("unique", 10);
        dictionary.insert("un", 20);

        assertEquals(10, dictionary.remove("unique"));
        assertNull(dictionary.search("unique"));
        assertFalse(dictionary.hasValueAtKey("unique"));
        assertEquals(20, dictionary.search("un"));

        dictionary.insert("another", 30);
        assertEquals(30, dictionary.remove("another"));
        assertNull(dictionary.search("another"));
        assertFalse(dictionary.hasValueAtKey("another"));
        assertEquals(20, dictionary.search("un"));
    }


    @Test
    void testInsert_NullOrEmptyKey() {
        assertThrows(IllegalArgumentException.class, () -> dictionary.insert(null, 1));
        assertThrows(IllegalArgumentException.class, () -> dictionary.insert("", 1));
    }

    @Test
    void testInsert_NullValue() {
        assertThrows(IllegalArgumentException.class, () -> dictionary.insert("key", null));
    }

    @Test
    void testSearch_NullOrEmptyKey() {
        assertNull(dictionary.search(null));
        assertNull(dictionary.search(""));
    }

    @Test
    void testRemove_NullOrEmptyKey() {
        assertNull(dictionary.remove(null));
        assertNull(dictionary.remove(""));
    }

    @Test
    void testComplexScenario_InsertRemoveInterleaved() {
        dictionary.insert("a", 1);
        dictionary.insert("b", 2);
        dictionary.insert("ab", 3);
        dictionary.insert("abc", 4);
        dictionary.insert("abd", 5);

        assertEquals(4, dictionary.remove("abc"));
        assertNull(dictionary.search("abc"));
        assertEquals(3, dictionary.search("ab"));
        assertEquals(5, dictionary.search("abd"));

        dictionary.insert("abc", 6);
        assertEquals(6, dictionary.search("abc"));

        assertEquals(3, dictionary.remove("ab"));
        assertNull(dictionary.search("ab"));
        assertEquals(1, dictionary.search("a"));
        assertEquals(6, dictionary.search("abc"));
        assertEquals(5, dictionary.search("abd"));

        assertEquals(1, dictionary.remove("a"));
        assertNull(dictionary.search("a"));

        assertEquals(6, dictionary.remove("abc"));
        assertNull(dictionary.search("abc"));

        assertEquals(5, dictionary.remove("abd"));
        assertNull(dictionary.search("abd"));

        assertEquals(2, dictionary.remove("b"));
        assertNull(dictionary.search("b"));
        assertFalse(dictionary.containsKey("a"));
    }

    @Test
    void testInsert_WordsWithDifferentCases() {
        dictionary.insert("Test", 1);
        dictionary.insert("test", 2);

        assertEquals(1, dictionary.search("Test"));
        assertEquals(2, dictionary.search("test"));
        assertNull(dictionary.search("TEST"));
    }

    @Test
    void testRemove_EnsuringNodesArePotentiallyCleaned() {
        dictionary.insert("tree", 1);
        dictionary.insert("trie", 2);
        dictionary.insert("try", 3);

        assertEquals(1, dictionary.remove("tree"));
        assertNull(dictionary.search("tree"));
        assertFalse(dictionary.hasValueAtKey("tree"));

        assertEquals(2, dictionary.remove("trie"));
        assertNull(dictionary.search("trie"));
        assertFalse(dictionary.hasValueAtKey("trie"));

        assertEquals(3, dictionary.remove("try"));
        assertNull(dictionary.search("try"));
        assertFalse(dictionary.hasValueAtKey("try"));
        assertFalse(dictionary.containsKey("t"));
    }

    @Test
    void testSuggestEmptyPrefixReturnsAllWordsSorted() {
        dictionary.insert("dog", 1);
        dictionary.insert("deer", 2);
        dictionary.insert("deal", 3);
        List<String> suggestions = dictionary.suggest("");
        assertEquals(List.of("deal", "deer", "dog"), suggestions);
    }

    @Test
    void testSuggestWithExistingPrefixIncludesFullWordAndDescendants() {
        dictionary.insert("app", 1);
        dictionary.insert("apple", 2);
        dictionary.insert("application", 3);
        dictionary.insert("banana", 4);

        List<String> suggestions = dictionary.suggest("app");
        assertEquals(List.of("app", "apple", "application"), suggestions);
    }

    @Test
    void testSuggestFullWordWithNoChildrenAndLongerPrefix() {
        dictionary.insert("solo", 10);

        List<String> s1 = dictionary.suggest("solo");
        assertEquals(List.of("solo"), s1);

        List<String> s2 = dictionary.suggest("sol");
        assertEquals(List.of("solo"), s2);

        List<String> s3 = dictionary.suggest("soloooo");
        assertTrue(s3.isEmpty());
    }

    @Test
    void testSuggestWithNoMatchesReturnsEmptyList() {
        dictionary.insert("a", 1);
        dictionary.insert("b", 2);

        List<String> suggestions = dictionary.suggest("c");
        assertTrue(suggestions.isEmpty());
    }
}