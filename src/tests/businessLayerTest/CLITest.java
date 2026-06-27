package tests.businessLayerTest;

import businessLayer.CLI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CLITest {
    private final InputStream standardIn = System.in;
    private final PrintStream standardOut = System.out;
    private ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach
    void setUp() {
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setIn(standardIn);
        System.setOut(standardOut);
    }

    @Test
    void testDisplayMessage() {
        CLI cli = new CLI();
        cli.displayMessage("Test message");
        assertEquals("Test message" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    void testDisplayBoard() {
        CLI cli = new CLI();
        cli.displayBoard("###\n#@#\n###");
        assertEquals("###\n#@#\n###" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    void testDisplayPlayerStats() {
        CLI cli = new CLI();
        cli.displayPlayerStats("HP: 100/100");
        assertEquals("HP: 100/100" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    void testGetInput() {
        String simulatedInput = "w" + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        CLI cli = new CLI();
        String result = cli.getInput();

        assertEquals("w", result);
    }

    @Test
    void testClose() {
        CLI cli = new CLI();
        assertDoesNotThrow(cli::close);
    }
}