package test;

import java.util.Random;
import ziplineGame.GameController;
import ziplineGame.Player;
import ziplineGame.PriorityQueue;
import ziplineGame.Queue;
import ziplineGame.Rider;

/** Tests priority decisions against fixed riders without running the view. */
public class PriorityControllerTest extends student.TestCase
{
    private Queue regular;
    private PriorityQueue priority;
    private Player player;
    private GameController game;

    /** Builds two empty queues with a repeatable random source. */
    public void setUp()
    {
        regular = new Queue();
        priority = new PriorityQueue();
        player = new Player(5.0);
        game = new GameController(regular, priority, player, new Random(1));
    }

    /** A real pass must be checked before accepting its owner. */
    public void testRealPassNeedsVerification()
    {
        Rider rider = new Rider("Valid", 48, "None", true, false);
        priority.enqueue(rider);
        assertTrue(game.hasPriorityQueueRider());
        assertTrue(game.helpPriorityQueue());
        assertTrue(game.frontRiderHasPriority());
        assertFalse(game.isFrontPriorityVerified());
        assertFalse(game.correctChoice());
        assertEquals("Real priority pass verified.", game.verifyFrontPriority());
        assertTrue(game.isFrontPriorityVerified());
        assertTrue(game.correctChoice());
        assertSame(rider, priority.peek());
        assertEquals(5.0, player.getRating(), 0.01);
        assertEquals("Priority pass was already verified.",
            game.verifyFrontPriority());
        assertTrue(game.processDecision(true));
        assertEquals(10.0, player.getRating(), 0.01);
        assertTrue(priority.isEmpty());
        assertFalse(game.hasCurrentRider());
    }

    /** A forged pass is rejected and its owner is banned. */
    public void testFakePassRejectedAndBanned()
    {
        priority.enqueue(new Rider("Fake", 60, "None", true, true));
        game.helpPriorityQueue();
        assertEquals("Fake priority pass.", game.verifyFrontPriority());
        assertFalse(game.isFrontPriorityVerified());
        assertFalse(game.correctChoice());
        assertTrue(game.processDecision(false));
        assertTrue(game.getBanList().contains("Fake"));
        assertEquals(10.0, player.getRating(), 0.01);
    }

    /** A rider without a pass cannot use the priority line. */
    public void testMissingPassRejectedAndBanned()
    {
        priority.enqueue(new Rider("No pass", 60, "None"));
        game.helpPriorityQueue();
        assertFalse(game.frontRiderHasPriority());
        assertEquals("No priority pass.", game.verifyFrontPriority());
        assertFalse(game.isFrontPriorityVerified());
        assertTrue(game.processDecision(false));
        assertTrue(game.getBanList().contains("No pass"));
    }

    /** Either decision loses points when a waiting priority rider is skipped. */
    public void testIgnoringPriorityPenalizesAcceptAndReject()
    {
        Rider waiting = new Rider("Priority", 60, "None", true);
        priority.enqueue(waiting);
        regular.enqueue(new Rider("First regular", 60, "None"));
        regular.enqueue(new Rider("Second regular", 40, "None"));
        game.helpRegularQueue();
        assertFalse(game.isHelpingCorrectQueue());
        assertFalse(game.processDecision(true));
        assertEquals(-5.0, player.getRating(), 0.01);
        assertSame(waiting, priority.peek());
        game.helpRegularQueue();
        assertFalse(game.processDecision(false));
        assertEquals(-15.0, player.getRating(), 0.01);
        assertTrue(regular.isEmpty());
        assertSame(waiting, priority.peek());
    }

    /** Verification does not override safety or ban-list rules. */
    public void testVerifiedRiderStillMustMeetRules()
    {
        Rider shortRider = new Rider("Short", 47, "None", true);
        Rider carrying = new Rider("Carrying", 60, "Phone", true);
        Rider banned = new Rider("Banned priority", 60, "None", true);
        priority.enqueue(shortRider);
        priority.enqueue(carrying);
        priority.enqueue(banned);
        game.addToBanList(banned);
        game.helpPriorityQueue();
        game.verifyFrontPriority();
        assertFalse(game.correctChoice());
        assertTrue(game.processDecision(false));
        game.helpPriorityQueue();
        game.verifyFrontPriority();
        assertFalse(game.correctChoice());
        assertTrue(game.removeRiderItems());
        assertTrue(game.correctChoice());
        assertTrue(game.processDecision(true));
        game.helpPriorityQueue();
        game.verifyFrontPriority();
        assertFalse(game.correctChoice());
        assertTrue(game.processDecision(false));
        assertEquals(20.0, player.getRating(), 0.01);
    }

    /** Checking a pass in the regular line cannot verify a waiting priority rider. */
    public void testRegularVerificationDoesNotChangePriorityRider()
    {
        Rider waiting = new Rider("Priority", 60, "None", true);
        priority.enqueue(waiting);
        regular.enqueue(new Rider("Regular", 60, "None"));
        game.helpRegularQueue();
        assertEquals("This person is not from the priority queue.",
            game.verifyFrontPriority());
        assertFalse(waiting.isPriorityVerified());
        assertEquals(5.0, player.getRating(), 0.01);
    }

    /** Startup preserves a supplied priority-only line. */
    public void testStartDoesNotAddToOccupiedPriorityLine()
    {
        Rider waiting = new Rider("Priority", 60, "None", true);
        priority.enqueue(waiting);
        assertTrue(game.startGame());
        assertTrue(game.startGame());
        assertSame(waiting, priority.peek());
        priority.dequeue();
        assertFalse(game.hasWaitingRider());
    }

    /** Failed selection of an empty line preserves the current rider. */
    public void testEmptySelectionPreservesCurrentRider()
    {
        assertFalse(game.helpPriorityQueue());
        assertFalse(game.helpRegularQueue());
        assertTrue(game.getPriorityQueueSummary().contains("empty"));
        regular.enqueue(new Rider("Regular", 60, "None"));
        game.helpRegularQueue();
        assertFalse(game.helpPriorityQueue());
        assertEquals("Regular", game.getFrontRiderName());
        assertTrue(game.isHelpingCorrectQueue());
    }

    /** Constructor validation includes the newly supplied priority line. */
    public void testNullPriorityQueueRejected()
    {
        try
        {
            new GameController(regular, null, player, new Random(1));
            fail("Expected a null priority line to be rejected");
        }
        catch (IllegalArgumentException expected)
        {
            // Expected validation failure.
        }
    }

    /** A returning visit must not reuse a previous visit's pass verification. */
    public void testReturningRiderIsANewVisit()
    {
        Random returning = new Random(1)
        {
            @Override
            public double nextDouble()
            {
                return 0.0;
            }

            @Override
            public int nextInt(int bound)
            {
                return bound - 1;
            }

            @Override
            public boolean nextBoolean()
            {
                return true;
            }
        };
        game = new GameController(regular, priority, player, returning);
        Rider firstVisit = new Rider("Returning", 60, "None", true);
        priority.enqueue(firstVisit);
        game.helpPriorityQueue();
        game.verifyFrontPriority();
        assertTrue(game.processDecision(true));
        assertTrue(game.getNewRider());
        Rider secondVisit = priority.peek();
        assertEquals("Returning", secondVisit.getName());
        assertNotSame(firstVisit, secondVisit);
        assertFalse(secondVisit.isPriorityVerified());
        assertTrue(firstVisit.isPriorityVerified());
        game.helpPriorityQueue();
        assertFalse(game.correctChoice());
        game.verifyFrontPriority();
        assertTrue(game.processDecision(true));
    }
}
