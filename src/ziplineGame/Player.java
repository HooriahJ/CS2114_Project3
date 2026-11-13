package ziplineGame;

// -------------------------------------------------------------------------
/**
 * Player class
 */
public class Player
{
    private double rating;

    // ----------------------------------------------------------
    /**
     * Create a new Player object.
     * 
     * @param rating
     */
    public Player(double rating)
    {
        this.rating = rating;
    }


    // ----------------------------------------------------------
    /**
     * Gets rating of player
     * 
     * @return rating
     */
    public double getRating()
    {
        return rating;
    }


    // ----------------------------------------------------------
    /**
     * Sets rating of player
     * 
     * @param changeRating
     * @return true if successful
     */
    public boolean setRating(double changeRating)
    {
        rating = changeRating;
        return true;
    }
}
