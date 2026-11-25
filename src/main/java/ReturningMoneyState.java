//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

public class ReturningMoneyState implements StateOfVendingMachine {

    private final VendingMachine machine;

    public ReturningMoneyState(VendingMachine machine) {
        if (machine == null) {
            throw new IllegalArgumentException("Machine cannot be null.");
        }
        this.machine = machine;
    }

    @Override
    public void selectSnack(String name) {
        throw new IllegalStateException("Machine is returning money.");
    }

    @Override
    public void insertMoney(double amount) {
        throw new IllegalStateException("Machine is returning money.");
    }

    @Override
    public void dispenseSnack() {
        throw new IllegalStateException("Machine is returning money.");
    }

    @Override
    public void returnMoney() {
        machine.refundAndReset();
    }

    @Override
    public void cancelTransaction() {
        returnMoney();
    }

    @Override
    public String getStateName() {
        return "RETURNING_MONEY";
    }
}
