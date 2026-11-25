//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnackTest {

    @Test
    void constructorAndGetters() {
        Snack snack = new Snack(" Coke ", 1.5, 2);

        assertEquals("Coke", snack.getName());
        assertEquals(1.5, snack.getPrice());
        assertEquals(2, snack.getQuantity());
        assertTrue(snack.isAvailable());
    }

    @Test
    void rejectsInvalidConstructorArguments() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Snack("", 1, 1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Snack("A", 0, 1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Snack("A", Double.NaN, 1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Snack("A", 1, -1)
        );
    }

    @Test
    void dispenseDecrementsAndSoldOutThrows() {
        Snack snack = new Snack("A", 1, 1);

        snack.dispenseOne();

        assertEquals(0, snack.getQuantity());
        assertFalse(snack.isAvailable());

        assertThrows(
                IllegalStateException.class,
                snack::dispenseOne
        );
    }

    @Test
    void restockWorksAndValidates() {
        Snack snack = new Snack("A", 1, 1);

        snack.restock(2);

        assertEquals(3, snack.getQuantity());

        assertThrows(
                IllegalArgumentException.class,
                () -> snack.restock(0)
        );
    }

    @Test
    void restockOverflowThrows() {
        Snack snack = new Snack("A", 1, Integer.MAX_VALUE);

        assertThrows(
                IllegalArgumentException.class,
                () -> snack.restock(1)
        );
    }

    @Test
    void toStringIncludesName() {
        Snack snack = new Snack("A", 1, 1);

        assertTrue(snack.toString().contains("A"));
    }
}
