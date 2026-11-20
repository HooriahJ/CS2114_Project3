
package ziplineGame;

/** A rider identity with mutable items and per-visit pass verification. */
public class Rider
{
    private final String name;
    private final int height;
    private String items;
    private boolean hasPriority;
    private boolean fakePriorityPass;
    private boolean priorityVerified;

    public Rider(String name, int height, String items)
    {
        this(name, height, items, false, false);
    }


    public Rider(String name, int height, String items, boolean hasPriority)
    {
        this(name, height, items, hasPriority, false);
    }


    /**
     * Creates a new visit with an initially unverified pass.
     * @param name nonblank rider name
     * @param height positive height in inches
     * @param items items carried; null or blank means none
     * @param hasPriority whether the rider carries a priority pass
     * @param fakePriorityPass whether that pass is forged
     * @throws IllegalArgumentException for an invalid name or height
     */
    public Rider(
        String name,
        int height,
        String items,
        boolean hasPriority,
        boolean fakePriorityPass)
    {
        if (name == null || name.trim().isEmpty() || height <= 0)
        {
            throw new IllegalArgumentException(
                "riders need a nonblank name and positive height");
        }
        this.name = name;
        this.height = height;

        if (items == null || items.trim().isEmpty())
        {
            this.items = "None";
        }
        else
        {
            this.items = items;
        }

        this.hasPriority = hasPriority;
        this.fakePriorityPass = fakePriorityPass;
        this.priorityVerified = false;
    }

    public String getName()
    {
        return name;
    }

    public int getHeight()
    {
        return height;
    }

    public String getItems()
    {
        return items;
    }

    /**
     * Changes carried items without changing rider identity or verification.
     * @param changeItems nonblank description, or "None" for no items
     * @return false for null/blank input, leaving existing items unchanged
     */
    public boolean setItems(String changeItems)
    {
        if (changeItems == null || changeItems.trim().isEmpty())
        {
            return false;
        }

        items = changeItems;
        return true;
    }

    public boolean hasPriority()
    {
        return hasPriority;
    }

    public boolean hasFakePriorityPass()
    {
        return fakePriorityPass;
    }

    public boolean isPriorityVerified()
    {
        return priorityVerified;
    }

    public boolean verifyPriority()
    {
        if (!hasPriority || fakePriorityPass)
        {
            return false;
        }

        priorityVerified = true;
        return true;
    }
}
