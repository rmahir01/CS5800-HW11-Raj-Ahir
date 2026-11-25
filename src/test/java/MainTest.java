//CS5800 - HW11 - BEHAVIORAL PATTERNS
//RAJ AHIR - 019330454

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void mainShowsAllSnacksSoldOutRefundAndFinalState() {

        PrintStream oldOutput = System.out;

        ByteArrayOutputStream outputBytes =
                new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(outputBytes));

            Main.main(new String[0]);

        } finally {
            System.setOut(oldOutput);
        }

        String output = outputBytes.toString();

        for (String snackName : new String[]{
                "Coke",
                "Pepsi",
                "Cheetos",
                "Doritos",
                "KitKat",
                "Snickers"
        }) {
            assertTrue(output.contains(snackName));
        }
        assertTrue(output.contains("Refunded"));
        assertTrue(output.contains("FINAL INVENTORY"));
        assertTrue(output.contains("Final machine state: IDLE"));
    }
}
