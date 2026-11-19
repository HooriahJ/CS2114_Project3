
package ziplineGame;

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

    public Rider(String name, int height, String items,
        boolean hasPriority)
    {
        this(name, height, items, hasPriority, false);
    }

    public Rider(String name, int height, String items,
        boolean hasPriority, boolean fakePriorityPass)
    {
        this.name = name;
        this.height = height;

        if (items == null || items.equals(""))
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

    public boolean setItems(String changeItems)
    {
        if (changeItems == null || changeItems.equals(""))
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
