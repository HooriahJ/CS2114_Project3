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
}
