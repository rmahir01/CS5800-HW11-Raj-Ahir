//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StateTransitionTest {

    @Test
    void stateConstructorsRejectNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new IdleState(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new WaitingForMoneyState(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new DispensingState(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ReturningMoneyState(null)
        );
    }

    @Test
    void idleCancelIsSafe() {

        VendingMachine machine = new VendingMachine(
                List.of(new Snack("Coke", 1, 1)),
                new CokeDispenser()
        );

        assertDoesNotThrow(machine::cancelTransaction);

        assertEquals("IDLE", machine.getStateName());
    }

    @Test
    void stateNamesAndInvalidOperations() {

        VendingMachine machine = new VendingMachine(
                List.of(new Snack("Coke", 1, 1)),
                new CokeDispenser()
        );

        IdleState idle = new IdleState(machine);

        assertEquals("IDLE", idle.getStateName());

        assertThrows(
                IllegalStateException.class,
                () -> idle.insertMoney(1)
        );

        assertThrows(
                IllegalStateException.class,
                idle::dispenseSnack
        );

        assertThrows(
                IllegalStateException.class,
                idle::returnMoney
        );

        machine.selectSnack("Coke");

        WaitingForMoneyState waiting =
                (WaitingForMoneyState) machine.getState();

        assertEquals(
                "WAITING_FOR_MONEY",
                waiting.getStateName()
        );

        assertThrows(
                IllegalStateException.class,
                () -> waiting.selectSnack("Coke")
        );

        assertThrows(
                IllegalStateException.class,
                waiting::dispenseSnack
        );

        DispensingState dispensing = new DispensingState(machine);

        assertEquals(
                "DISPENSING",
                dispensing.getStateName()
        );

        assertThrows(
                IllegalStateException.class,
                () -> dispensing.selectSnack("Coke")
        );

        assertThrows(
                IllegalStateException.class,
                () -> dispensing.insertMoney(1)
        );

        assertThrows(
                IllegalStateException.class,
                dispensing::returnMoney
        );

        assertThrows(
                IllegalStateException.class,
                dispensing::cancelTransaction
        );

        ReturningMoneyState returning =
                new ReturningMoneyState(machine);

        assertEquals(
                "RETURNING_MONEY",
                returning.getStateName()
        );

        assertThrows(
                IllegalStateException.class,
                () -> returning.selectSnack("Coke")
        );

        assertThrows(
                IllegalStateException.class,
                () -> returning.insertMoney(1)
        );

        assertThrows(
                IllegalStateException.class,
                returning::dispenseSnack
        );
    }

    @Test
    void snickersEventuallySellsOutAndRefunds() {

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

        VendingMachine machine = new VendingMachine(
                List.of(
                        new Snack("Coke", 1, 1),
                        new Snack("Pepsi", 1, 1),
                        new Snack("Cheetos", 1, 1),
                        new Snack("Doritos", 1, 1),
                        new Snack("KitKat", 1, 1),
                        new Snack("Snickers", 1, 1)
                ),
                a
        );

        machine.selectSnack("Snickers");
        machine.insertMoney(1);
        assertEquals(0, machine.getSnack("Snickers").getQuantity());

        machine.selectSnack("Snickers");
        machine.insertMoney(1);
        assertEquals(1, machine.getLastRefund(), 0.0001);
        assertEquals("IDLE", machine.getStateName());
    }
}
