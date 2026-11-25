//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

import java.util.*;

public class VendingMachine {

    private final Map<String, Snack> snacks = new LinkedHashMap<>();

    private final SnackDispenseHandler dispenser;
    private StateOfVendingMachine state;

    private Snack selectedSnack;
    private Snack lastDispensedSnack;

    private double insertedMoney;
    private double lastRefund;
    private double lastChange;

    public VendingMachine(
            Collection<Snack> inventory,
            SnackDispenseHandler dispenser) {

        if (inventory == null || inventory.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one snack is required."
            );
        }

        if (dispenser == null) {
            throw new IllegalArgumentException(
                    "Dispenser chain cannot be null."
            );
        }

        for (Snack snack : inventory) {

            if (snack == null) {
                throw new IllegalArgumentException(
                        "Snack list cannot contain null."
                );
            }

            if (snacks.putIfAbsent(
                    snack.getName().toLowerCase(Locale.ROOT),
                    snack) != null) {

                throw new IllegalArgumentException(
                        "Duplicate snack: " + snack.getName()
                );
            }
        }

        this.dispenser = dispenser;
        this.state = new IdleState(this);
    }

    public void selectSnack(String name) {
        state.selectSnack(name);
    }

    public void insertMoney(double amount) {
        state.insertMoney(amount);
    }

    public void dispenseSnack() {
        state.dispenseSnack();
    }

    public void returnMoney() {
        state.returnMoney();
    }

    public void cancelTransaction() {
        state.cancelTransaction();
    }

    Snack findSnack(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Snack name cannot be blank."
            );
        }

        return snacks.get(name.trim().toLowerCase(Locale.ROOT));
    }

    void beginSelection(Snack snack) {
        selectedSnack = snack;
        insertedMoney = 0;
        lastRefund = 0;
        lastChange = 0;
        lastDispensedSnack = null;
    }

    void addMoney(double amount) {
        insertedMoney += amount;
    }

    void setState(StateOfVendingMachine state) {

        if (state == null) {
            throw new IllegalArgumentException("State cannot be null.");
        }

        this.state = state;
    }

    void completeSuccessfulDispense(Snack snack) {

        if (snack == null || snack != selectedSnack) {
            throw new IllegalArgumentException(
                    "Dispensed snack must be the selected snack."
            );
        }

        if (insertedMoney + 1e-9 < snack.getPrice()) {
            throw new IllegalStateException("Insufficient payment.");
        }

        lastDispensedSnack = snack;
        lastChange = Math.max(0, insertedMoney - snack.getPrice());

        insertedMoney = 0;
        selectedSnack = null;

        state = new IdleState(this);

        System.out.printf(
                "Dispensed %s. Change returned: $%.2f%n",
                snack.getName(),
                lastChange
        );
    }

    void refundAndReset() {

        lastRefund = insertedMoney;

        System.out.printf("Refunded $%.2f%n", lastRefund);

        insertedMoney = 0;
        selectedSnack = null;

        state = new IdleState(this);
    }

    public String getStateName() {
        return state.getStateName();
    }

    public StateOfVendingMachine getState() {
        return state;
    }

    public Snack getSelectedSnack() {
        return selectedSnack;
    }

    public double getInsertedMoney() {
        return insertedMoney;
    }

    public double getLastRefund() {
        return lastRefund;
    }

    public double getLastChange() {
        return lastChange;
    }

    public Snack getLastDispensedSnack() {
        return lastDispensedSnack;
    }

    public SnackDispenseHandler getDispenser() {
        return dispenser;
    }

    public Snack getSnack(String name) {

        Snack snack = findSnack(name);

        if (snack == null) {
            throw new IllegalArgumentException(
                    "Snack not found: " + name
            );
        }

        return snack;
    }

    public Collection<Snack> getAllSnacks() {
        return Collections.unmodifiableCollection(snacks.values());
    }
}

