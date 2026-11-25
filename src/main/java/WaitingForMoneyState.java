//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

public class WaitingForMoneyState implements StateOfVendingMachine {

    private final VendingMachine machine;

    public WaitingForMoneyState(VendingMachine machine) {
        if (machine == null) {
            throw new IllegalArgumentException("Machine cannot be null.");
        }
        this.machine = machine;
    }

    @Override
    public void selectSnack(String name) {
        throw new IllegalStateException("A snack is already selected.");
    }

    @Override
    public void insertMoney(double amount) {

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                    "Inserted money must be positive and finite."
            );
        }

        if (!Double.isFinite(machine.getInsertedMoney() + amount)) {
            throw new IllegalArgumentException(
                    "Total inserted money is too large."
            );
        }

        machine.addMoney(amount);

        System.out.printf(
                "Inserted $%.2f. Total: $%.2f%n",
                amount,
                machine.getInsertedMoney()
        );

        if (machine.getInsertedMoney() + 1e-9
                >= machine.getSelectedSnack().getPrice()) {

            machine.setState(new DispensingState(machine));
            machine.dispenseSnack();
        }
    }

    @Override
    public void dispenseSnack() {
        throw new IllegalStateException("Insert enough money first.");
    }

    @Override
    public void returnMoney() {
        machine.setState(new ReturningMoneyState(machine));
        machine.returnMoney();
    }

    @Override
    public void cancelTransaction() {
        returnMoney();
    }

    @Override
    public String getStateName() {
        return "WAITING_FOR_MONEY";
    }
}
