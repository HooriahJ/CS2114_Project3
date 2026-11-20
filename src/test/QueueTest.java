package test;
import ziplineGame.Queue;
import ziplineGame.Rider;
public class QueueTest extends student.TestCase
{
    private Queue queue;
    private Rider rider;
    public void setUp()
    {
        queue = new Queue();
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

    /** Interleaved insertions, emptying, and reuse retain FIFO order. */
    public void testInterleavedOrderAndReuse()
    {
        Rider a = new Rider("First", 60, "None");
        Rider b = new Rider("Second", 60, "None");
        Rider c = new Rider("Third", 60, "None");
        queue.enqueue(a);
        queue.enqueue(b);
        assertSame(a, queue.peek());
        assertFalse(queue.enqueue(null));
        assertSame(a, queue.peek());
        queue.dequeue();
        queue.enqueue(c);
        assertSame(b, queue.peek());
        queue.dequeue();
        assertSame(c, queue.peek());
        queue.dequeue();
        assertNull(queue.peek());
        assertFalse(queue.dequeue());
        queue.enqueue(a);
        assertSame(a, queue.peek());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

}