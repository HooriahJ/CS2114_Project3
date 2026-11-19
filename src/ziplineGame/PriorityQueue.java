
package ziplineGame;

public class PriorityQueue
{
    private Node front;
    private Node rear;

    private class Node
    {
        Rider rider;
        Node next;

        Node(Rider rider)
        {
            this.rider = rider;
            this.next = null;
        }
    }

    public boolean enqueue(Rider rider)
    {
        if (rider == null)
        {
            return false;
        }

        Node newNode = new Node(rider);

        if (front == null)
        {
            front = newNode;
            rear = newNode;
        }
        else
        {
            rear.next = newNode;
            rear = newNode;
        }

        return true;
    }

    public boolean dequeue()
    {
        if (front == null)
        {
            return false;
        }

        front = front.next;

        if (front == null)
        {
            rear = null;
        }

        return true;
    }

    public Rider peek()
    {
        if (front == null)
        {
            return null;
        }

        return front.rider;
    }

    public boolean isEmpty()
    {
        return front == null;
    }

    public String toString()
    {
        if (front == null)
        {
            return "The priority queue is empty.";
        }

        return "There are riders waiting in the priority queue.";
    }
}
