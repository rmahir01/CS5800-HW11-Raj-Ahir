//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

public abstract class SnackDispenseHandler {

    private SnackDispenseHandler nextHandler;

    public void setNextHandler(SnackDispenseHandler next) {

        if (next == this) {
            throw new IllegalArgumentException(
                    "A handler cannot point to itself."
            );
        }

        for (SnackDispenseHandler p = next; p != null; p = p.nextHandler) {
            if (p == this) {
                throw new IllegalArgumentException(
                        "Handler chain cannot contain a cycle."
                );
            }
        }

        nextHandler = next;
    }

    public SnackDispenseHandler getNextHandler() {
        return nextHandler;
    }

    protected abstract String getHandledSnackName();

    public boolean dispenseSnack(VendingMachine machine, Snack snack) {

        if (machine == null || snack == null) {
            throw new IllegalArgumentException(
                    "Machine and snack are required."
            );
        }

        if (getHandledSnackName().equalsIgnoreCase(snack.getName())) {

            if (!snack.isAvailable()) {
                return false;
            }

            snack.dispenseOne();
            return true;
        }

        return nextHandler != null
                && nextHandler.dispenseSnack(machine, snack);
    }
}
