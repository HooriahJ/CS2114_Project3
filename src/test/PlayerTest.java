package test;

import ziplineGame.Player;

public class PlayerTest
    extends student.TestCase
{
    private Player player;

    public void setUp()
    {
        player = new Player(5.0);
    }


    public void testGetRating()
    {
        assertEquals(5.0, player.getRating(), 0.01);
    }


    public void testConstructor()
    {
        Player positive = new Player(10.0);
        Player zero = new Player(0.0);
        Player negative = new Player(-10.0);
        Player decimal = new Player(4.75);

        assertEquals(10.0, positive.getRating(), 0.01);
        assertEquals(0.0, zero.getRating(), 0.01);
        assertEquals(-10.0, negative.getRating(), 0.01);
        assertEquals(4.75, decimal.getRating(), 0.01);
    }


    public void testSetRating()
    {
        assertTrue(player.setRating(10.0));
        assertEquals(10.0, player.getRating(), 0.01);

        assertTrue(player.setRating(0.0));
        assertEquals(0.0, player.getRating(), 0.01);

        assertTrue(player.setRating(-10.0));
        assertEquals(-10.0, player.getRating(), 0.01);

        assertTrue(player.setRating(4.75));
        assertEquals(4.75, player.getRating(), 0.01);
    }


    public void testSetSameRating()
    {
        assertTrue(player.setRating(5.0));
        assertEquals(5.0, player.getRating(), 0.01);
    }

    /** Invalid numeric values must not poison later score calculations. */
    public void testNonFiniteRatingRejected()
    {
        double[] invalid = { Double.NaN, Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY };
        for (double value : invalid)
        {
            assertFalse(player.setRating(value));
            assertEquals(5.0, player.getRating(), 0.01);
            try
            {
                new Player(value);
                fail("Expected a finite starting rating");
            }
            catch (IllegalArgumentException expected)
            {
                // Expected validation failure.
            }
        }
    }

}
