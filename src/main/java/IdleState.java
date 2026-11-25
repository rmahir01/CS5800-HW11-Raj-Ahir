//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

public class IdleState implements StateOfVendingMachine {

    private final VendingMachine machine;

    public IdleState(VendingMachine machine) {
        if (machine == null) {
            throw new IllegalArgumentException("Machine cannot be null.");
        }
        this.machine = machine;
    }

    @Override
    public void selectSnack(String name) {

        Snack snack = machine.findSnack(name);

        if (snack == null) {
            throw new IllegalArgumentException("Snack not found: " + name);
        }

        machine.beginSelection(snack);
        machine.setState(new WaitingForMoneyState(machine));

        System.out.printf(
                "Selected %s. Please insert $%.2f.%n",
                snack.getName(),
                snack.getPrice()
        );
    }

    @Override
    public void insertMoney(double amount) {
        throw new IllegalStateException(
                "Select a snack before inserting money."
        );
    }

    @Override
    public void dispenseSnack() {
        throw new IllegalStateException("No snack has been selected.");
    }

    @Override
    public void returnMoney() {
        throw new IllegalStateException("There is no money to return.");
    }

    @Override
    public void cancelTransaction() {
        System.out.println("Machine is idle; nothing to cancel.");
    }

    @Override
    public String getStateName() {
        return "IDLE";
    }
}
