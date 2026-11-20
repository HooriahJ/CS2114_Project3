package test;
import ziplineGame.Rider;

public class RiderTest
    extends student.TestCase
{
    private Rider rider;
    public void setUp()
    {
        rider = new Rider("Alex", 60, "Backpack");
    }
    public void testGetName()
    {
        assertEquals("Alex", rider.getName());
    }
    public void testGetHeight()
    {
        assertEquals(60, rider.getHeight());
    }
    public void testGetItems()
    {
        assertEquals("Backpack", rider.getItems());
    }
    public void testSetItems()
    {
        assertTrue(rider.setItems("Phone"));

        assertEquals("Phone", rider.getItems());
    }
    public void testSetItemsNull()
    {
        assertFalse(rider.setItems(null));

        assertEquals("Backpack", rider.getItems());
    }
    public void testSetItemsEmpty()
    {
        assertFalse(rider.setItems(""));

        assertEquals("Backpack", rider.getItems());
    }
    public void testNoItems()
    {
        Rider rider2 = new Rider("Sam", 55, "None");

        assertEquals("None", rider2.getItems());
    }


    public void testHasPriority()
    {
        Rider priorityRider = new Rider("Alex", 60, "None", true);

        assertTrue(priorityRider.hasPriority());
    }


    public void testDoesNotHavePriority()
    {
        assertFalse(rider.hasPriority());
    }


    public void testVerifyPriority()
    {
        Rider priorityRider = new Rider("Alex", 60, "None", true, false);

        assertTrue(priorityRider.verifyPriority());
        assertTrue(priorityRider.isPriorityVerified());
    }


    public void testFakePriorityPass()
    {
        Rider fakeRider = new Rider("Sam", 60, "None", true, true);

        assertFalse(fakeRider.verifyPriority());
        assertFalse(fakeRider.isPriorityVerified());
    }

    /** Whitespace cannot replace an existing item description. */
    public void testBlankItemsRejected()
    {
        assertFalse(rider.setItems("   "));
        assertEquals("Backpack", rider.getItems());
        assertEquals("None", new Rider("Sam", 60, "   ").getItems());
    }

    /** Invalid identity values are rejected before they enter a queue. */
    public void testInvalidIdentity()
    {
        assertInvalidRider(null, 60);
        assertInvalidRider("", 60);
        assertInvalidRider("   ", 60);
        assertInvalidRider("Alex", -1);
        assertInvalidRider("Alex", 0);
    }

    /** Missing and forged passes cannot become verified. */
    public void testMissingPassCannotBeVerified()
    {
        assertFalse(rider.verifyPriority());
        assertFalse(rider.isPriorityVerified());
        assertFalse(rider.hasFakePriorityPass());
        Rider valid = new Rider("Valid", 60, "None", true);
        assertTrue(valid.verifyPriority());
        assertTrue(valid.verifyPriority());
        assertTrue(valid.isPriorityVerified());
    }

    private void assertInvalidRider(String name, int height)
    {
        try
        {
            new Rider(name, height, "None");
            fail("Expected an invalid identity to be rejected");
        }
        catch (IllegalArgumentException expected)
        {
            // Expected validation failure.
        }
    }

}
