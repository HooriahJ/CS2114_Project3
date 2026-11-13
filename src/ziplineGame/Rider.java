package ziplineGame;

// -------------------------------------------------------------------------
/**
 * Rider class
 */
public class Rider
{
    private final String name;
    private final int height;
    private String items;

    // ----------------------------------------------------------
    /**
     * Create a new Rider object.
     * 
     * @param name
     * @param height
     * @param items
     */
    public Rider(String name, int height, String items)
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
    }


    // ----------------------------------------------------------
    /**
     * Get name of rider
     * 
     * @return name
     */
    public String getName()
    {
        return name;
    }


    // ----------------------------------------------------------
    /**
     * Get height of rider
     * 
     * @return height
     */
    public int getHeight()
    {
        return height;
    }


    // ----------------------------------------------------------
    /**
     * Get items of rider
     * 
     * @return items
     */
    public String getItems()
    {
        return items;
    }


    // ----------------------------------------------------------
    /**
     * Sets the riders items
     * 
     * @param changeItems
     *            items to change to
     * @return false if null or empty String, true otherwise
     */
    public boolean setItems(String changeItems)
    {
        if (changeItems == null || changeItems.equals(""))
        {
            return false;
        }
        items = changeItems;
        return true;
    }
}
