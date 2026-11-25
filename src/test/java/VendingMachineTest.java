//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VendingMachineTest {

    private VendingMachine machine(int quantity) {
        return new VendingMachine(
                List.of(new Snack("Coke", 1.5, quantity)),
                new CokeDispenser()
        );
    }

    @Test
    void startsIdleAndInventoryIsUnmodifiable() {

        VendingMachine machine = machine(2);

        assertEquals("IDLE", machine.getStateName());

        assertThrows(
                UnsupportedOperationException.class,
                () -> machine.getAllSnacks().clear()
        );
    }

    @Test
    void constructorValidatesInputs() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new VendingMachine(null, new CokeDispenser())
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new VendingMachine(
                        List.of(),
                        new CokeDispenser()
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new VendingMachine(
                        List.of(new Snack("A", 1, 1)),
                        null
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new VendingMachine(
                        java.util.Arrays.asList(
                                new Snack("A", 1, 1),
                                null
                        ),
                        new CokeDispenser()
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new VendingMachine(
                        List.of(
                                new Snack("A", 1, 1),
                                new Snack("a", 2, 2)
                        ),
                        new CokeDispenser()
                )
        );
    }

    @Test
    void getSnackCaseInsensitiveAndUnknownThrows() {

        VendingMachine machine = machine(1);

        assertEquals("Coke", machine.getSnack("cOKE").getName());

        assertThrows(
                IllegalArgumentException.class,
                () -> machine.getSnack("Pepsi")
        );
    }

    @Test
    void selectMovesToWaitingState() {

        VendingMachine machine = machine(1);

        machine.selectSnack("Coke");

        assertEquals("WAITING_FOR_MONEY", machine.getStateName());
        assertEquals("Coke", machine.getSelectedSnack().getName());
    }

    @Test
    void insufficientThenSufficientPaymentDispenses() {

        VendingMachine machine = machine(1);

        machine.selectSnack("Coke");
        machine.insertMoney(0.5);

        assertEquals("WAITING_FOR_MONEY", machine.getStateName());

        machine.insertMoney(1);

        assertEquals("IDLE", machine.getStateName());
        assertEquals(0, machine.getSnack("Coke").getQuantity());
        assertEquals(
                "Coke",
                machine.getLastDispensedSnack().getName()
        );
    }

    @Test
    void overpaymentReturnsChange() {

        VendingMachine machine = machine(1);

        machine.selectSnack("Coke");
        machine.insertMoney(2);

        assertEquals(0.5, machine.getLastChange(), 0.0001);
    }

    @Test
    void soldOutReturnsPayment() {

        VendingMachine machine = machine(0);

        machine.selectSnack("Coke");
        machine.insertMoney(1.5);

        assertEquals(1.5, machine.getLastRefund(), 0.0001);
        assertEquals("IDLE", machine.getStateName());
    }

    @Test
    void cancelRefundsPartialPayment() {

        VendingMachine machine = machine(1);

        machine.selectSnack("Coke");
        machine.insertMoney(0.75);
        machine.cancelTransaction();

        assertEquals(0.75, machine.getLastRefund(), 0.0001);
        assertNull(machine.getSelectedSnack());
        assertEquals("IDLE", machine.getStateName());
    }

    @Test
    void invalidPaymentsThrow() {

        VendingMachine machine = machine(1);

        machine.selectSnack("Coke");

        assertThrows(
                IllegalArgumentException.class,
                () -> machine.insertMoney(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> machine.insertMoney(-1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> machine.insertMoney(Double.NaN)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> machine.insertMoney(Double.POSITIVE_INFINITY)
        );
    }

    @Test
    void wrongStateOperationsThrow() {

        VendingMachine machine = machine(1);

        assertThrows(
                IllegalStateException.class,
                () -> machine.insertMoney(1)
        );

        assertThrows(
                IllegalStateException.class,
                machine::dispenseSnack
        );

        assertThrows(
                IllegalStateException.class,
                machine::returnMoney
        );

        machine.selectSnack("Coke");

        assertThrows(
                IllegalStateException.class,
                () -> machine.selectSnack("Coke")
        );

        assertThrows(
                IllegalStateException.class,
                machine::dispenseSnack
        );
    }

    @Test
    void invalidSelectionDoesNotChangeState() {

        VendingMachine machine = machine(1);

        assertThrows(
                IllegalArgumentException.class,
                () -> machine.selectSnack("unknown")
        );

        assertEquals("IDLE", machine.getStateName());
    }

    @Test
    void gettersReflectRefundAndPurchase() {

        VendingMachine machine = machine(2);

        machine.selectSnack("Coke");
        machine.insertMoney(0.25);

        assertEquals(0.25, machine.getInsertedMoney(), 0.0001);

        machine.returnMoney();

        assertEquals(0.25, machine.getLastRefund(), 0.0001);

        machine.selectSnack("Coke");
        machine.insertMoney(1.5);

        assertEquals(0, machine.getInsertedMoney(), 0.0001);
        assertEquals(0, machine.getLastRefund(), 0.0001);

        assertNotNull(machine.getState());
        assertNotNull(machine.getDispenser());
    }
}
