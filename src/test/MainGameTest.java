
package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;

import ziplineGame.GameController;
import ziplineGame.MainGame;
import ziplineGame.Player;
import ziplineGame.Queue;
import ziplineGame.Rider;

public class MainGameTest
    extends student.TestCase
{
    private Queue queue;
    private Player player;
    private GameController controller;

    public void setUp()
    {
        queue = new Queue();
        queue.enqueue(new Rider("Test Rider", 60, "None"));

        player = new Player(5.0);
        controller = new GameController(queue, player, new Random(0));
    }


    public void testWhitespaceAroundMenuChoice()
    {
        String output = runGame(" 8 \n 1 \n 12 \n");

        assertTrue(
            output.contains(
                "You are now helping the person from the Regular Queue."));

        assertTrue(output.contains("Rider Name: Test Rider"));

        assertTrue(output.contains("Thank you for playing!"));

        assertFalse(output.contains("Invalid input."));
    }


    public void testEndOfInput()
    {
        String output = runGame("");

        assertTrue(output.contains("Game exited."));
        assertEquals(5.0, player.getRating(), 0.01);
        assertEquals("Test Rider", queue.peek().getName());
    }


    public void testInvalidInputThenExit()
    {
        String output = runGame("hello\n12\n");

        assertTrue(output.contains("Invalid input."));
        assertTrue(output.contains("Thank you for playing!"));
        assertEquals(5.0, player.getRating(), 0.01);
    }


    public void testEndOfInputAfterAction()
    {
        String output = runGame("8\n9\n");

        assertTrue(
            output.contains(
                "You are now helping the person from the Regular Queue."));

        assertTrue(output.contains("You correctly accepted the rider!"));

        assertTrue(output.contains("Game exited."));

        assertEquals(10.0, player.getRating(), 0.01);
        assertTrue(queue.isEmpty());
    }


    private String runGame(String input)
    {
        InputStream oldInput = System.in;
        PrintStream oldOutput = System.out;

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try
        {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            System.setOut(new PrintStream(output));

            new MainGame().run(controller);

            return output.toString();
        }
        finally
        {
            System.setIn(oldInput);
            System.setOut(oldOutput);
        }
    }

    /** Every invalid input from the scope is recoverable without scoring. */
    public void testAllMalformedMenuInputs()
    {
        String output = runGame("\n   \n-1\n0\n13\n1.5\nnull\n999999999999999999999\n12\n");
        assertEquals(8, output.split("Invalid input\\.", -1).length - 1);
        assertTrue(output.contains("Thank you for playing!"));
        assertEquals(5.0, player.getRating(), 0.01);
        assertEquals("Test Rider", queue.peek().getName());
    }

    /** Inspection and decision commands survive without a selected rider. */
    public void testCommandsWithoutSelection()
    {
        String output = runGame("1\n2\n3\n4\n5\n6\n7\n9\n10\n12\n");
        assertTrue(output.contains("No current rider."));
        assertTrue(output.contains("You are not currently helping a rider."));
        assertEquals(5.0, player.getRating(), 0.01);
        assertEquals("Test Rider", queue.peek().getName());
    }

    /** Console item removal uses the model before evaluating acceptance. */
    public void testRemoveItemsAndAccept()
    {
        queue.peek().setItems("Phone");
        String output = runGame("8\n3\n4\n4\n9\n12\n");
        assertTrue(output.contains("Rider Items: Phone"));
        assertTrue(output.contains("You removed the rider's items."));
        assertTrue(output.contains("They have no items."));
        assertTrue(output.contains("You correctly accepted the rider!"));
        assertEquals(10.0, player.getRating(), 0.01);
        assertTrue(queue.isEmpty());
    }

    /** Rejecting an eligible rider is penalized through the console path. */
    public void testRejectEligibleRider()
    {
        String output = runGame("8\n10\n12\n");
        assertTrue(output.contains("You incorrectly rejected the rider."));
        assertEquals(-5.0, player.getRating(), 0.01);
        assertTrue(queue.isEmpty());
    }
}
