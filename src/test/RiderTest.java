package test;
import ziplineGame.Rider;

public class RiderTest extends student.TestCase
{
    private Rider rider;
    public void setUp()
    {
        rider = new Rider("A B", 100, "None");
    }
    public void testGetName()
    {
        assertEquals("A B", rider.getName());
    }
    public void testGetHeight()
    {
        assertEquals(100, rider.getHeight());
    }
    public void testGetItems()
    {
        assertEquals("None", rider.getItems());
    }
    public void testSetItems()
    {
        assertTrue(rider.setItems("None"));
        assertEquals("None", rider.getItems());
        assertTrue(rider.setItems("Backpack"));
        assertEquals("Backpack", rider.getItems());
        assertFalse(rider.setItems(null));
        assertFalse(rider.setItems(""));
        assertEquals("Backpack", rider.getItems());
    }
}