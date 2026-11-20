package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import ziplineGame.Main;

/** Smoke tests for the real entry point and its repeatable demo scenario. */
public class MainTest extends student.TestCase
{
    /** Normal startup displays the title and accepts an exit command. */
    public void testStartupAndExit()
    {
        String output = runMain(new String[0], "12\n");
        assertTrue(output.contains("PAPA'S ZIPLINERIA"));
        assertTrue(output.contains("Thank you for playing!"));
    }

    /** A closed input stream exits rather than crashing or looping. */
    public void testStartupWithNoInput()
    {
        assertTrue(runMain(new String[0], "").contains("Game exited."));
    }

    /** Demo riders demonstrate real, forged, and missing passes in order. */
    public void testDemoPriorityFlow()
    {
        String output = runMain(new String[] { "--demo" },
            "7\n1\n6\n9\n7\n1\n6\n10\n7\n1\n6\n10\n12\n");
        assertTrue(output.contains("Rider Name: Avery Pass"));
        assertTrue(output.contains("Real priority pass verified."));
        assertTrue(output.contains("Rider Name: Blake Fake"));
        assertTrue(output.contains("Fake priority pass."));
        assertTrue(output.contains("Rider Name: Casey NoPass"));
        assertTrue(output.contains("No priority pass."));
        assertTrue(output.contains("Player Rating: 20.0"));
    }

    /** Unknown launch options explain usage and do not start a game. */
    public void testUnknownLaunchOption()
    {
        String output = runMain(new String[] { "--unknown" }, "12\n");
        assertTrue(output.contains("Usage:"));
        assertFalse(output.contains("Enter your choice:"));
    }

    private String runMain(String[] args, String input)
    {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream captured = new PrintStream(output);
        try
        {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            System.setOut(captured);
            Main.main(args);
            captured.flush();
            return output.toString();
        }
        finally
        {
            System.setIn(originalInput);
            System.setOut(originalOutput);
            captured.close();
        }
    }
}
