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
     * @param rating finite initial rating; negative values are allowed
     * @throws IllegalArgumentException if rating is NaN or infinite
     */
    public Player(double rating)
    {
        if (Double.isNaN(rating) || Double.isInfinite(rating))
        {
            throw new IllegalArgumentException("rating must be finite");
        }
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
     * @param changeRating requested finite rating
     * @return true if updated, false for NaN or infinity (no change)
     */
    public boolean setRating(double changeRating)
    {
        if (Double.isNaN(changeRating) || Double.isInfinite(changeRating))
        {
            return false;
        }
        rating = changeRating;
        return true;
    }
}
