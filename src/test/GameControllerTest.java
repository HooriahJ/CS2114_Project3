package test;

import java.util.Random;

import ziplineGame.GameController;
import ziplineGame.Queue;
import ziplineGame.Player;
import ziplineGame.Rider;

public class GameControllerTest
    extends student.TestCase
{
    private Queue queue;
    private Player player;
    private GameController game;

    public void setUp()
    {
        queue = new Queue();
        player = new Player(5.0);

        game = new GameController(queue, player, new Random(1));
    }


    public void testHelpRegularQueue()
    {
        assertFalse(game.helpRegularQueue());

        queue.enqueue(new Rider("Alex", 60, "None"));

        assertTrue(game.helpRegularQueue());
        assertTrue(game.hasCurrentRider());
    }


    public void testGetFrontRiderName()
    {
        queue.enqueue(new Rider("Alex", 60, "None"));

        game.helpRegularQueue();

        assertEquals("Alex", game.getFrontRiderName());
    }


    public void testGetFrontRiderHeight()
    {
        queue.enqueue(new Rider("Alex", 60, "None"));

        game.helpRegularQueue();

        assertEquals(60, game.getFrontRiderHeight());
    }


    public void testGetFrontRiderItems()
    {
        queue.enqueue(new Rider("Alex", 60, "Backpack"));

        game.helpRegularQueue();

        assertEquals("Backpack", game.getFrontRiderItems());
    }


    public void testHasCurrentRider()
    {
        assertFalse(game.hasCurrentRider());

        queue.enqueue(new Rider("Alex", 60, "None"));

        game.helpRegularQueue();

        assertTrue(game.hasCurrentRider());
    }


    public void testCorrectChoice()
    {
        queue.enqueue(new Rider("Alex", 60, "None"));

        game.helpRegularQueue();

        assertTrue(game.correctChoice());
    }


    public void testIncorrectChoice()
    {
        queue.enqueue(new Rider("Alex", 40, "None"));

        game.helpRegularQueue();

        assertFalse(game.correctChoice());
    }


    public void testRemoveRiderItems()
    {
        queue.enqueue(new Rider("Alex", 60, "Backpack"));

        game.helpRegularQueue();

        assertTrue(game.removeRiderItems());
        assertEquals("None", game.getFrontRiderItems());
    }


    public void testRemoveItemsWhenNone()
    {
        queue.enqueue(new Rider("Alex", 60, "None"));

        game.helpRegularQueue();

        assertFalse(game.removeRiderItems());
    }


    public void testAddToBanList()
    {
        Rider rider = new Rider("Test Rider", 60, "None");

        assertTrue(game.addToBanList(rider));
        assertFalse(game.addToBanList(rider));
    }


    public void testCalculateRatingChange()
    {
        queue.enqueue(new Rider("Test Rider", 60, "None"));

        game.helpRegularQueue();

        assertTrue(game.calculateRatingChange(true));
        assertEquals(10.0, game.getCurrentRating(), 0.01);
    }


    public void testCalculateRatingChangeIncorrect()
    {
        queue.enqueue(new Rider("Test Rider", 60, "None"));

        game.helpRegularQueue();

        assertFalse(game.calculateRatingChange(false));
        assertEquals(-5.0, game.getCurrentRating(), 0.01);
    }


    public void testCalculateRatingChangeNoRider()
    {
        assertFalse(game.calculateRatingChange(true));

        assertEquals(5.0, game.getCurrentRating(), 0.01);
    }


    public void testServeNextRider()
    {
        queue.enqueue(new Rider("Alex", 60, "None"));

        game.helpRegularQueue();

        assertTrue(game.serveNextRider());
        assertFalse(game.hasCurrentRider());
        assertTrue(queue.isEmpty());
    }
}
