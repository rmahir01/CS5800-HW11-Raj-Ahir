//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

import java.util.List;

public class Main {

    public static void main(String[] args) {

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

        VendingMachine machine = new VendingMachine(
                List.of(
                        new Snack("Coke", 1.50, 3),
                        new Snack("Pepsi", 1.50, 3),
                        new Snack("Cheetos", 1.25, 3),
                        new Snack("Doritos", 1.75, 3),
                        new Snack("KitKat", 1.00, 3),
                        new Snack("Snickers", 1.25, 2)
                ),
                coke
        );

        System.out.println("==========================================");
        System.out.println("             VENDING MACHINE              ");
        System.out.println("==========================================");

        System.out.println("\n--- SUCCESSFUL PURCHASES ---");

        buy(machine, "Coke", 2.00);
        buy(machine, "Pepsi", 1.50);
        buy(machine, "Cheetos", 1.25);
        buy(machine, "Doritos", 2.00);
        buy(machine, "KitKat", 1.00);

        System.out.println("\n--- SNICKERS PURCHASES ---");

        buy(machine, "Snickers", 1.25);
        buy(machine, "Snickers", 1.25);

        System.out.println("\n--- SOLD-OUT SNACK ---");

        buy(machine, "Snickers", 1.25);

        System.out.println("\n--- INSUFFICIENT PAYMENT ---\n");

        machine.selectSnack("Coke");
        machine.insertMoney(0.50);

        System.out.println("Additional payment required.");
        machine.insertMoney(1.00);

        System.out.println("\n--- TRANSACTION CANCELLATION ---\n");

        machine.selectSnack("Pepsi");
        machine.insertMoney(0.50);
        machine.cancelTransaction();

        System.out.println("\n==========================================");
        System.out.println("             FINAL INVENTORY");
        System.out.println("==========================================");

        for (Snack snack : machine.getAllSnacks()) {
            System.out.printf(
                    "%-12s | Price: $%-5.2f | Remaining: %d%n",
                    snack.getName(),
                    snack.getPrice(),
                    snack.getQuantity()
            );
        }

        System.out.println("------------------------------------------");
        System.out.println("Final machine state: " + machine.getStateName());
        System.out.println("==========================================");
    }

    private static void buy(
            VendingMachine machine,
            String snackName,
            double amount) {

        System.out.println("\nPurchasing: " + snackName);

        machine.selectSnack(snackName);
        machine.insertMoney(amount);
    }
}

