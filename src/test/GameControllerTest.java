package test;

import ziplineGame.GameController;
import ziplineGame.Queue;
import ziplineGame.Player;
import ziplineGame.Rider;

public class GameControllerTest extends student.TestCase
{
    private Queue queue;
    private Player player;
    private GameController game;
    
    public void setUp()
    {
        queue = new Queue();
        player = new Player(5.0);
        game = new GameController(queue, player);   
    }
    public void testIsValid()
    {
        assertTrue(game.isValid("1"));
        assertTrue(game.isValid("4"));
        assertTrue(game.isValid("9"));
        assertFalse(game.isValid("0"));
        assertFalse(game.isValid("10"));
        assertFalse(game.isValid("-1"));
        assertFalse(game.isValid("a"));
        assertFalse(game.isValid("null"));
    }
    public void testGetCurrentRating()
    {
        assertEquals(5.0, game.getCurrentRating(), 0.01);
    }
    public void testServeNextRider()
    {
        assertFalse(game.serveNextRider());
        Rider rider = new Rider("A B", 100, "None");
        queue.enqueue(rider);
        assertTrue(game.serveNextRider());
        assertFalse(game.serveNextRider());
    }
    public void testGetFrontRiderName()
    {
        assertEquals("No current rider", game.getFrontRiderName());
        Rider rider = new Rider("A B", 100, "None");
        queue.enqueue(rider);
        assertEquals("A B", game.getFrontRiderName());
    }
    public void testGetFrontRiderHeight()
    {
        assertEquals(-1, game.getFrontRiderHeight());
        Rider rider = new Rider("A B", 100, "None");
        queue.enqueue(rider);
        assertEquals(100, game.getFrontRiderHeight());
    }
    public void testGetFrontRiderItems()
    {
        assertEquals("No current rider", game.getFrontRiderItems());
        Rider rider = new Rider("A B", 100, "Backpack");
        queue.enqueue(rider);
        assertEquals("Backpack", game.getFrontRiderItems());
    }
    public void testGetFrontRiderSummary()
    {
        assertEquals("No riders are waiting.", game.getFrontRiderSummary());
        Rider rider = new Rider("A B", 100, "Backpack");
        queue.enqueue(rider);
        assertTrue(game.getFrontRiderSummary().contains("A B"));
        assertTrue(game.getFrontRiderSummary().contains("100"));
        assertTrue(game.getFrontRiderSummary().contains("Backpack"));
    }
    public void testCorrectChoice()
    {
        Rider rider = new Rider("A B", 100, "None");
        queue.enqueue(rider);
        assertTrue(game.correctChoice());
        queue.dequeue();
        Rider rider2 = new Rider("A B", 40, "None");
        queue.enqueue(rider2);
        assertFalse(game.correctChoice());
        queue.dequeue();
        Rider rider3 = new Rider("A B", 100, "Backpack");
        queue.enqueue(rider3);
        assertFalse(game.correctChoice());
        Rider rider4 = new Rider("A B", 100, "None");
        game.addToBanList(rider4);
        assertFalse(game.correctChoice());
    }
    public void testGetNewRider()
    {
        assertTrue(game.getNewRider());
        assertFalse(queue.isEmpty());
    }
    public void testCalculateRatingChange()
    {
        Rider rider = new Rider("A B", 100, "None");
        queue.enqueue(rider);
        assertTrue(game.calculateRatingChange(true));
        assertEquals(10.0, game.getCurrentRating(), 0.01);
       
        assertFalse(game.calculateRatingChange(false));
        assertEquals(0.0, game.getCurrentRating(), 0.01);
    }
    public void testAddToBanList()
    {
        Rider rider = new Rider("A B", 100, "None");
        assertTrue(game.addToBanList(rider));
        assertFalse(game.addToBanList(rider));
        assertFalse(game.addToBanList(null));
    }
    public void testGetBanList()
    {
        assertTrue(game.getBanList().contains("A B") == false);
        Rider rider = new Rider("A B", 100, "None");
        game.addToBanList(rider);
        assertTrue(game.getBanList().contains("A B"));
    }
    public void testRemoveItems()
    {
        Rider rider = new Rider("A B", 100, "Backpack");
        queue.enqueue(rider);
        assertEquals("Backpack", rider.getItems());
        assertTrue(game.removeRiderItems());
        assertEquals("None", rider.getItems());
        assertFalse(game.removeRiderItems());
    }
    public void testEmptyQueueRatingUnchanged()
    {
        assertFalse(game.calculateRatingChange(false));
        assertFalse(game.calculateRatingChange(true));
        assertEquals(5.0, game.getCurrentRating(), 0.01);
    }
    public void testBanMatchesByNameAndHeight()
    {
        game.addToBanList(new Rider("Zed Q", 100, "None"));
        queue.enqueue(new Rider("Zed Q", 100, "None"));
        assertFalse(game.correctChoice());
    }
    public void testProcessDecision()
    {
        assertFalse(game.processDecision(true));
        queue.enqueue(new Rider("A B", 100, "None"));
        assertTrue(game.processDecision(true));
        assertEquals(10.0, game.getCurrentRating(), 0.01);
        assertTrue(game.hasWaitingRider());
    }
}
