package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import ziplineGame.GameController;
import ziplineGame.MainGame;
import ziplineGame.Player;
import ziplineGame.Queue;
import ziplineGame.Rider;

public class MainGameTest extends student.TestCase
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
        String output = runGame(" 1 \n\t9 \n");
        assertTrue(output.contains("Rider Name: Test Rider"));
        assertTrue(output.contains("Game exited."));
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
        String output = runGame("hello\n\n0\n10\n9\n");
        assertTrue(output.contains("Invalid input."));
        assertTrue(output.contains("Game exited."));
        assertEquals(5.0, player.getRating(), 0.01);
        assertEquals("Test Rider", queue.peek().getName());
    }

    public void testEndOfInputAfterAction()
    {
        String output = runGame("6\n");
        assertTrue(output.contains("You made the correct choice."));
        assertTrue(output.contains("Game exited."));
        assertEquals(10.0, player.getRating(), 0.01);
        assertTrue(queue.isEmpty());
    }

    private String runGame(String input)
    {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream capturedOut = new PrintStream(output);
        try
        {
            System.setIn(new ByteArrayInputStream(
                input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(capturedOut);
            new MainGame().run(controller);
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
        finally
        {
            System.setIn(originalIn);
            System.setOut(originalOut);
            capturedOut.close();
        }
    }
}
