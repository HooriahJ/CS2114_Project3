package test;

import ziplineGame.PriorityQueue;
import ziplineGame.Rider;

public class PriorityQueueTest
    extends student.TestCase
{
    private PriorityQueue queue;
    private Rider rider;

    public void setUp()
    {
        queue = new PriorityQueue();
        rider = new Rider("A B", 100, "None");
    }


    public void testEnqueue()
    {
        assertTrue(queue.enqueue(rider));
        assertFalse(queue.isEmpty());
        assertFalse(queue.enqueue(null));
    }


    public void testDequeue()
    {
        assertFalse(queue.dequeue());
        queue.enqueue(rider);
        assertTrue(queue.dequeue());
        assertTrue(queue.isEmpty());
        assertFalse(queue.dequeue());
    }


    public void testPeek()
    {
        assertNull(queue.peek());
        queue.enqueue(rider);
        assertEquals(rider, queue.peek());
        Rider rider2 = new Rider("C D", 110, "None");
        queue.enqueue(rider2);
        assertEquals(rider, queue.peek());
    }


    public void testIsEmpty()
    {
        assertTrue(queue.isEmpty());
        queue.enqueue(rider);
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
}
