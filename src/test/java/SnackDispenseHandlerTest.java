//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SnackDispenseHandlerTest {

    private VendingMachine machine(
            SnackDispenseHandler handler,
            Snack... snacks) {

        return new VendingMachine(List.of(snacks), handler);
    }

    @Test
    void handlerLinkAndCycleValidation() {

        CokeDispenser a = new CokeDispenser();
        PepsiDispenser b = new PepsiDispenser();

        a.setNextHandler(b);

        assertSame(b, a.getNextHandler());

        assertThrows(
                IllegalArgumentException.class,
                () -> a.setNextHandler(a)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> b.setNextHandler(a)
        );
    }

    @Test
    void handlersAreConnectedInRequiredOrder() {

        SnackDispenseHandler coke = new CokeDispenser();
        SnackDispenseHandler pepsi = new PepsiDispenser();
        SnackDispenseHandler cheetos = new CheetosDispenser();
        SnackDispenseHandler doritos = new DoritosDispenser();
        SnackDispenseHandler kitkat = new KitKatDispenser();
        SnackDispenseHandler snickers = new SnickersDispenser();

        coke.setNextHandler(pepsi);
        pepsi.setNextHandler(cheetos);
        cheetos.setNextHandler(doritos);
        doritos.setNextHandler(kitkat);
        kitkat.setNextHandler(snickers);

        assertSame(pepsi, coke.getNextHandler());
        assertSame(cheetos, pepsi.getNextHandler());
        assertSame(doritos, cheetos.getNextHandler());
        assertSame(kitkat, doritos.getNextHandler());
        assertSame(snickers, kitkat.getNextHandler());

        assertNull(snickers.getNextHandler());
    }

    @Test
    void sixHandlersEachHandleTheirSnack() {

        Snack[] stock = {
                new Snack("Coke", 1, 1),
                new Snack("Pepsi", 1, 1),
                new Snack("Cheetos", 1, 1),
                new Snack("Doritos", 1, 1),
                new Snack("KitKat", 1, 1),
                new Snack("Snickers", 1, 1)
        };

        SnackDispenseHandler a = new CokeDispenser();
        SnackDispenseHandler b = new PepsiDispenser();
        SnackDispenseHandler c = new CheetosDispenser();
        SnackDispenseHandler d = new DoritosDispenser();
        SnackDispenseHandler e = new KitKatDispenser();
        SnackDispenseHandler f = new SnickersDispenser();

        a.setNextHandler(b);
        b.setNextHandler(c);
        c.setNextHandler(d);
        d.setNextHandler(e);
        e.setNextHandler(f);

        VendingMachine machine = machine(a, stock);

        for (Snack snack : stock) {
            assertTrue(a.dispenseSnack(machine, snack));
        }

        for (Snack snack : stock) {
            assertEquals(0, snack.getQuantity());
        }
    }

    @Test
    void unmatchedSnackReturnsFalse() {

        CokeDispenser dispenser = new CokeDispenser();

        Snack snack = new Snack("Other", 1, 1);

        assertFalse(
                dispenser.dispenseSnack(
                        machine(dispenser, snack),
                        snack
                )
        );
    }

    @Test
    void soldOutMatchDoesNotFallThrough() {

        CokeDispenser coke = new CokeDispenser();
        coke.setNextHandler(new PepsiDispenser());

        Snack soldOutCoke = new Snack("Coke", 1, 0);
        Snack pepsi = new Snack("Pepsi", 1, 1);

        VendingMachine machine = machine(
                coke,
                soldOutCoke,
                pepsi
        );

        assertFalse(coke.dispenseSnack(machine, soldOutCoke));

        assertEquals(1, pepsi.getQuantity());
    }

    @Test
    void nullArgumentsThrow() {

        CokeDispenser dispenser = new CokeDispenser();

        VendingMachine machine = machine(
                dispenser,
                new Snack("Coke", 1, 1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> dispenser.dispenseSnack(
                        null,
                        new Snack("Coke", 1, 1)
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> dispenser.dispenseSnack(machine, null)
        );
    }
}
