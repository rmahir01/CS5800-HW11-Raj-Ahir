//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

public interface StateOfVendingMachine {

    void selectSnack(String name);

    void insertMoney(double amount);

    void dispenseSnack();

    void returnMoney();

    void cancelTransaction();

    String getStateName();
}

