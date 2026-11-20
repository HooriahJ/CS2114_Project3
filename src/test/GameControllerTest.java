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

    /** Repeated evaluation must not award points twice for one rider. */
    public void testDecisionScoresOnlyOnce()
    {
        queue.enqueue(new Rider("Alex", 60, "None"));
        game.helpRegularQueue();
        assertTrue(game.calculateRatingChange(true));
        assertFalse(game.calculateRatingChange(true));
        game.helpRegularQueue();
        assertFalse(game.calculateRatingChange(false));
        assertEquals(10.0, player.getRating(), 0.01);
        game.serveNextRider();
        queue.enqueue(new Rider("Sam", 60, "None"));
        game.helpRegularQueue();
        assertTrue(game.processDecision(true));
        assertEquals(15.0, player.getRating(), 0.01);
    }

    /** Invalid menu text must not change the model. */
    public void testInvalidMenuInputs()
    {
        String[] invalid = { null, "", "   ", "null", "0", "-1", "13",
            "1.5", "999999999999999999999999", "1 2" };
        for (String input : invalid)
        {
            assertFalse(game.isValid(input));
        }
        assertTrue(game.isValid(" 1 "));
        assertTrue(game.isValid("12"));
        assertEquals(5.0, player.getRating(), 0.01);
        assertFalse(game.hasWaitingRider());
    }

    /** All actions on an empty model leave its rating and queues alone. */
    public void testEmptyActions()
    {
        assertFalse(game.hasWaitingRider());
        assertFalse(game.correctChoice());
        assertFalse(game.processDecision(true));
        assertFalse(game.processDecision(false));
        assertFalse(game.serveNextRider());
        assertFalse(game.removeRiderItems());
        assertFalse(game.helpPriorityQueue());
        assertFalse(game.isHelpingCorrectQueue());
        assertFalse(game.frontRiderHasPriority());
        assertFalse(game.isFrontPriorityVerified());
        assertEquals("No current rider.", game.verifyFrontPriority());
        assertEquals("No rider selected.", game.getFrontRiderName());
        assertEquals("No rider selected.", game.getFrontRiderItems());
        assertEquals(0, game.getFrontRiderHeight());
        assertTrue(game.getFrontRiderSummary().contains("No rider"));
        assertEquals(5.0, player.getRating(), 0.01);
    }

    /** Exactly 48 inches is allowed; 47 inches is not. */
    public void testHeightBoundaryAndOncePerDecision()
    {
        queue.enqueue(new Rider("At boundary", 48, "None"));
        queue.enqueue(new Rider("Below boundary", 47, "None"));
        game.helpRegularQueue();
        assertTrue(game.correctChoice());
        assertTrue(game.processDecision(true));
        assertFalse(game.processDecision(true));
        game.helpRegularQueue();
        assertFalse(game.correctChoice());
        assertTrue(game.processDecision(false));
        assertTrue(game.getBanList().contains("Below boundary"));
        assertEquals(15.0, player.getRating(), 0.01);
    }

    /** Inspections and removing items must preserve FIFO order. */
    public void testInspectAndRemoveItemsKeepFront()
    {
        Rider first = new Rider("First", 60, "Phone");
        Rider second = new Rider("Second", 60, "None");
        queue.enqueue(first);
        queue.enqueue(second);
        game.helpRegularQueue();
        assertFalse(game.correctChoice());
        assertEquals("First", game.getFrontRiderName());
        assertEquals(60, game.getFrontRiderHeight());
        assertEquals("Phone", game.getFrontRiderItems());
        assertTrue(game.removeRiderItems());
        assertFalse(game.removeRiderItems());
        assertSame(first, queue.peek());
        assertTrue(game.correctChoice());
        assertEquals(5.0, player.getRating(), 0.01);
        assertTrue(game.processDecision(true));
        assertSame(second, queue.peek());
    }

    /** A matching person remains banned even when represented by a new object. */
    public void testBanIdentityAndNull()
    {
        Rider rider = new Rider("Banned", 60, "None");
        assertFalse(game.addToBanList(null));
        assertTrue(game.addToBanList(rider));
        assertFalse(game.addToBanList(new Rider("Banned", 60, "Phone")));
        queue.enqueue(new Rider("Banned", 60, "None"));
        game.helpRegularQueue();
        assertFalse(game.correctChoice());
        assertTrue(game.processDecision(false));
        assertEquals(10.0, player.getRating(), 0.01);
    }

    /** Starting twice must not add another rider to an occupied regular line. */
    public void testStartPreservesExistingRider()
    {
        Rider rider = new Rider("Existing", 60, "None");
        queue.enqueue(rider);
        assertTrue(game.startGame());
        assertTrue(game.startGame());
        assertSame(rider, queue.peek());
        queue.dequeue();
        assertFalse(game.hasWaitingRider());
    }

    /** New arrivals are reachable through one of the two lines. */
    public void testStartAndGenerateRider()
    {
        assertTrue(game.startGame());
        assertTrue(game.hasWaitingRider());
        assertTrue(game.getNewRider());
        assertTrue(game.hasRegularQueueRider() || game.hasPriorityQueueRider());
    }

    /** Null collaborators are rejected at construction. */
    public void testNullDependencies()
    {
        assertBadController(null, player, new Random(1));
        assertBadController(queue, null, new Random(1));
        assertBadController(queue, player, null);
    }

    private void assertBadController(Queue line, Player person, Random source)
    {
        try
        {
            new GameController(line, person, source);
            fail("Expected invalid collaborators to be rejected");
        }
        catch (IllegalArgumentException expected)
        {
            // Expected validation failure.
        }
    }

}
