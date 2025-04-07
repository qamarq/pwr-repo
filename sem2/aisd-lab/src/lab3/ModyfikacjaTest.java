package lab3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ModyfikacjaTest {
    private final TwoWayCycledListWithSentinel<Object> list = new TwoWayCycledListWithSentinel<>();

    @Test
    public void testAdd() {
        list.clear();
        list.add("dom");
        list.add("pies");
        list.add("kot");
        Assertions.assertEquals(3, list.size());
    }

    @Test
    public void testGet() {
        list.clear();
        list.add("dom");
        list.add("pies");
        list.add("kot");
        Assertions.assertEquals("pies", list.get(1));
    }

    @Test
    public void testRemove() {
        list.clear();
        list.add("dom");
        list.add("pies");
        list.add("kot");
        Assertions.assertEquals("pies", list.remove(1));
        Assertions.assertEquals(2, list.size());
    }

    @Test
    public void testMiddle() {
        list.clear();
        list.add("dom");
        list.add("pies");
        list.add("kot");
        Assertions.assertEquals("pies", list.middle());
    }

    @Test
    public void testIndexOf() {
        list.clear();
        list.add("dom");
        list.add("pies");
        list.add("kot");
        Assertions.assertEquals(1, list.indexOf("pies"));
        Assertions.assertEquals(-1, list.indexOf("ryba"));
    }

    @Test
    public void testEmptyList() {
        list.clear();
        Assertions.assertTrue(list.isEmpty());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        Assertions.assertNull(list.middle());
    }
}
