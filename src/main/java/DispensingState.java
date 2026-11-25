//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

public class DispensingState implements StateOfVendingMachine {

    private final VendingMachine machine;

    public DispensingState(VendingMachine machine) {
        if (machine == null) {
            throw new IllegalArgumentException("Machine cannot be null.");
        }
        this.machine = machine;
    }

    @Override
    public void selectSnack(String name) {
        throw new IllegalStateException("Machine is dispensing.");
    }

    @Override
    public void insertMoney(double amount) {
        throw new IllegalStateException("Machine is dispensing.");
    }

    @Override
    public void dispenseSnack() {

        Snack snack = machine.getSelectedSnack();

        if (machine.getDispenser().dispenseSnack(machine, snack)) {

            machine.completeSuccessfulDispense(snack);

        } else {

            System.out.println(
                    snack.getName()
                            + " is sold out or has no matching handler. Returning payment."
            );

            machine.setState(new ReturningMoneyState(machine));
            machine.returnMoney();
        }
    }

    @Override
    public void returnMoney() {
        throw new IllegalStateException(
                "Cannot return money during dispensing."
        );
    }

    @Override
    public void cancelTransaction() {
        throw new IllegalStateException(
                "Cannot cancel while dispensing."
        );
    }

    @Override
    public String getStateName() {
        return "DISPENSING";
    }
}
