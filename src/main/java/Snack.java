//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

public class Snack {
    private final String name;
    private final double price;
    private int quantity;

    public Snack(String name, double price, int quantity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Snack name cannot be blank.");
        }
        if (!Double.isFinite(price) || price <= 0) {
            throw new IllegalArgumentException("Price must be positive and finite.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        this.name = name.trim();
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public void dispenseOne() {
        if (!isAvailable()) {
            throw new IllegalStateException(name + " is sold out.");
        }
        quantity--;
    }

    public void restock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Restock amount must be positive.");
        }

        try {
            quantity = Math.addExact(quantity, amount);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Restock quantity too large.", e);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "%s - $%.2f (quantity: %d)",
                name, price, quantity
        );
    }
}
