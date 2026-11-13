package ziplineGame;

import java.util.ArrayList;
import java.util.Random;

// -------------------------------------------------------------------------
/**
 * Controller for Papa's Ziplineria. Mediates between the view (MainGame) and
 * the model (Queue, Rider, Player). The view only ever talks to this class: it
 * sends in user choices and gets back Strings, ints and booleans to display.
 * It never receives a Queue or Player reference, so it cannot change the
 * model directly.
 *
 * The ban list lives here (not in the model) because it is a rule the
 * controller enforces while evaluating a decision, not state the view reads.
 */
public class GameController
{
    // ----- Game rules / tuning values (one place to change them) -----
    /** Minimum height, in inches, to ride. Also shown in MainGame's rules. */
    public static final int MIN_HEIGHT = 48;
    /** Items value meaning the rider carries nothing. Matches Rider's default. */
    public static final String NO_ITEMS = "None";
    /** Rating gained for a correct accept/reject decision. */
    public static final double RATING_GAIN = 5.0;
    /** Rating lost for a wrong accept/reject decision. */
    public static final double RATING_LOSS = 10.0;
    /** Chance (0 to 1) that a new rider is actually a returning rider. */
    public static final double RETURN_CHANCE = 0.20;
    /** Number of riders pre-loaded onto the ban list. */
    public static final int STARTING_BANNED = 3;
    /** Number of options in MainGame's menu (valid choices are 1..this). */
    public static final int MENU_OPTIONS = 9;

    private static final int MIN_RANDOM_HEIGHT = 30;
    private static final int HEIGHT_RANGE = 50;

    private static final String[] FIRST_NAMES = { "Liam", "Olivia", "Noah",
        "Emma", "Oliver", "Amelia", "Theodore", "Charlotte", "James", "Mia",
        "Henry", "Sophia", "Mateo", "Isabella", "Elijah", "Evelyn", "Lucas",
        "Ava", "William", "Sofia", "Benjamin", "Camila", "Levi", "Harper",
        "Ezra", "Luna", "Sebastian", "Eleanor", "Jack", "Violet", "Daniel",
        "Aurora", "Samuel", "Elizabeth", "Michael", "Eliana", "Grayson",
        "Hazel", "Ethan", "Chloe", "Asher", "Ellie", "John", "Nora", "Hudson",
        "Gianna", "Luca", "Lily", "Leo", "Emily", "Elias", "Aria", "Kai",
        "Scarlett", "Theo", "Willow", "Owen", "Penelope", "Alexander", "Zoe",
        "Dylan", "Ella", "Gabriel", "Avery", "Santiago", "Elena", "Mason",
        "Abigail", "Julian", "Mila", "David", "Lucy", "Joseph", "Isla",
        "Carter", "Ivy", "Matthew", "Layla", "Luke", "Delilah", "Aiden",
        "Riley", "Jackson", "Lainey", "Maverick", "Nova", "Miles", "Grace",
        "Wyatt", "Goldie", "Thomas", "Winter", "Isaac", "Arabella", "Josiah",
        "Anastasia", "Hugo", "Myla", "Arthur", "Leah" };

    private static final String[] LAST_NAMES = { "Smith", "Johnson",
        "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
        "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson",
        "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee",
        "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez",
        "Lewis", "Robinson", "Walker", "Young", "Allen", "King", "Wright",
        "Scott", "Torres", "Nguyen", "Hill", "Flores", "Green", "Adams",
        "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter",
        "Roberts", "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker",
        "Cruz", "Edwards", "Collins", "Reyes", "Stewart", "Morris", "Morales",
        "Murphy", "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper",
        "Peterson", "Bailey", "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox",
        "Ward", "Richardson", "Watson", "Brooks", "Chavez", "Wood", "James",
        "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes", "Price", "Alvarez",
        "Castillo", "Sanders", "Patel", "Myers", "Long", "Ross", "Foster",
        "Jimenez" };

    // "None" appears several times so most riders carry nothing.
    private static final String[] ITEMS = { NO_ITEMS, NO_ITEMS, NO_ITEMS,
        NO_ITEMS, NO_ITEMS, "Food", "Drink", "Phone", "Keys",
        "Wallet, Backpack" };

    // ----- Fields -----
    private Queue queue;
    private Player player;
    private ArrayList<Rider> banList;
    private ArrayList<Rider> previousRiders;
    private Random random;

    // ----------------------------------------------------------
    /**
     * Create a controller with its own random source.
     *
     * @param queue
     *            the line of waiting riders (must not be null)
     * @param player
     *            the player whose rating is tracked (must not be null)
     */
    public GameController(Queue queue, Player player)
    {
        this(queue, player, new Random());
    }


    // ----------------------------------------------------------
    /**
     * Create a controller with a supplied random source. Passing a seeded
     * Random makes rider generation repeatable, so tests do not depend on luck.
     *
     * @param queue
     *            the line of waiting riders (must not be null)
     * @param player
     *            the player whose rating is tracked (must not be null)
     * @param random
     *            source of randomness (must not be null)
     */
    public GameController(Queue queue, Player player, Random random)
    {
        if (queue == null || player == null || random == null)
        {
            throw new IllegalArgumentException(
                "queue, player and random must not be null");
        }
        this.queue = queue;
        this.player = player;
        this.random = random;
        this.banList = new ArrayList<Rider>();
        this.previousRiders = new ArrayList<Rider>();

        // Riders are generated without touching the queue, so a queue that
        // already holds riders is never disturbed by constructing a controller.
        for (int i = 0; i < STARTING_BANNED; i++)
        {
            Rider banned = generateRider();
            previousRiders.add(banned);
            addToBanList(banned);
        }
    }


    // ----------------------------------------------------------
    /**
     * Put the first rider in line. Call once before the game loop so the player
     * never starts at an empty line. Does nothing if someone is already waiting.
     *
     * @return true if a rider is waiting afterwards
     */
    public boolean startGame()
    {
        if (queue.isEmpty())
        {
            return getNewRider();
        }
        return true;
    }


    // ----------------------------------------------------------
    /**
     * Validates a menu choice typed by the user.
     *
     * @param input
     *            raw text from the view
     * @return true only if input parses as an integer from 1 to MENU_OPTIONS
     */
    public boolean isValid(String input)
    {
        if (input == null)
        {
            return false;
        }

        try
        {
            int choice = Integer.parseInt(input.trim());
            return choice >= 1 && choice <= MENU_OPTIONS;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }


    // ----------------------------------------------------------
    /**
     * Removes the front rider from the line. A rider who breaks the rules is
     * added to the ban list on the way out, and every served rider is
     * remembered so they can return later.
     *
     * @return true if a rider was removed, false if the line was empty (the
     *         rating is never touched here)
     */
    public boolean serveNextRider()
    {
        Rider rider = queue.peek();

        if (rider == null)
        {
            return false;
        }

        if (breaksRules(rider))
        {
            addToBanList(rider);
        }

        if (!previousRiders.contains(rider))
        {
            previousRiders.add(rider);
        }

        return queue.dequeue();
    }


    // ----------------------------------------------------------
    /**
     * @return true if someone is waiting at the front of the line
     */
    public boolean hasWaitingRider()
    {
        return queue.peek() != null;
    }


    // ----------------------------------------------------------
    /**
     * @return the player's current rating (read only, the view cannot set it)
     */
    public double getCurrentRating()
    {
        return player.getRating();
    }


    // ----------------------------------------------------------
    /**
     * @return the front rider's name, or "No current rider"
     */
    public String getFrontRiderName()
    {
        Rider rider = queue.peek();

        if (rider == null)
        {
            return "No current rider";
        }

        return rider.getName();
    }


    // ----------------------------------------------------------
    /**
     * @return the front rider's height in inches, or -1 if no one is waiting
     */
    public int getFrontRiderHeight()
    {
        Rider rider = queue.peek();

        if (rider == null)
        {
            return -1;
        }

        return rider.getHeight();
    }


    // ----------------------------------------------------------
    /**
     * @return the front rider's items, or "No current rider"
     */
    public String getFrontRiderItems()
    {
        Rider rider = queue.peek();

        if (rider == null)
        {
            return "No current rider";
        }

        return rider.getItems();
    }


    // ----------------------------------------------------------
    /**
     * @return the front rider's name, height and items, or
     *         "No riders are waiting."
     */
    public String getFrontRiderSummary()
    {
        Rider rider = queue.peek();

        if (rider == null)
        {
            return "No riders are waiting.";
        }

        return "Name: " + rider.getName() + ", Height: " + rider.getHeight()
            + ", Items: " + rider.getItems();
    }


    // ----------------------------------------------------------
    /**
     * Whether the front rider SHOULD be accepted: tall enough, not banned, and
     * carrying nothing.
     *
     * @return true if accepting is the correct choice; false if the rider
     *         should be rejected or no one is waiting
     */
    public boolean correctChoice()
    {
        Rider rider = queue.peek();

        if (rider == null)
        {
            return false;
        }

        return !breaksRules(rider) && !isBanned(rider);
    }


    // ----------------------------------------------------------
    /**
     * Builds a new rider and adds it to the back of the line. With probability
     * RETURN_CHANCE the rider is someone who has already been served, which
     * gives the ban list a reason to exist.
     *
     * @return true if the rider was added
     */
    public boolean getNewRider()
    {
        if (!previousRiders.isEmpty() && random.nextDouble() < RETURN_CHANCE)
        {
            int index = random.nextInt(previousRiders.size());
            return queue.enqueue(previousRiders.get(index));
        }

        return queue.enqueue(generateRider());
    }


    // ----------------------------------------------------------
    /**
     * Applies the rating consequence of the player's decision on the front
     * rider: +RATING_GAIN if it matches correctChoice(), -RATING_LOSS if not.
     * Must be called BEFORE serveNextRider(), since it judges the rider at the
     * front. (processDecision does both in the right order.)
     *
     * @param accepted
     *            true if the player accepted the rider, false if rejected
     * @return true if the player's decision was correct; false if it was wrong
     *         or if no rider was waiting (in which case the rating does not
     *         change)
     */
    public boolean calculateRatingChange(boolean accepted)
    {
        if (!hasWaitingRider())
        {
            return false;
        }

        boolean correctDecision = (accepted == correctChoice());
        double change = correctDecision ? RATING_GAIN : -RATING_LOSS;
        player.setRating(player.getRating() + change);

        return correctDecision;
    }


    // ----------------------------------------------------------
    /**
     * One full turn: judge the decision, update the rating, remove the front
     * rider, and feed a fresh rider into the line. The view calls only this, so
     * it cannot get the order of those steps wrong.
     *
     * @param accepted
     *            true if the player accepted the rider, false if rejected
     * @return true if the decision was correct; false if wrong or no rider was
     *         waiting (nothing changes in that case)
     */
    public boolean processDecision(boolean accepted)
    {
        if (!hasWaitingRider())
        {
            return false;
        }

        boolean correct = calculateRatingChange(accepted);
        serveNextRider();
        getNewRider();
        return correct;
    }


    // ----------------------------------------------------------
    /**
     * Adds a rider to the ban list.
     *
     * @param rider
     *            rider to ban
     * @return true if added; false if rider is null or already banned
     */
    public boolean addToBanList(Rider rider)
    {
        if (rider == null || isBanned(rider))
        {
            return false;
        }
        banList.add(rider);
        return true;
    }


    // ----------------------------------------------------------
    /**
     * @return a printable list of banned rider names
     */
    public String getBanList()
    {
        if (banList.isEmpty())
        {
            return "The ban list is empty.";
        }

        StringBuilder summary = new StringBuilder("Ban List:\n");

        for (Rider banned : banList)
        {
            summary.append(banned.getName()).append("\n");
        }

        return summary.toString();
    }


    // ----------------------------------------------------------
    /**
     * Takes away the front rider's items.
     *
     * @return true if items were removed; false if no one is waiting or the
     *         rider has no items
     */
    public boolean removeRiderItems()
    {
        Rider rider = queue.peek();

        if (rider == null || rider.getItems().equals(NO_ITEMS))
        {
            return false;
        }

        return rider.setItems(NO_ITEMS);
    }


    // ----- Private helpers -----

    /** True if the rider is too short or still carrying items. */
    private boolean breaksRules(Rider rider)
    {
        return rider.getHeight() < MIN_HEIGHT
            || !rider.getItems().equals(NO_ITEMS);
    }


    /**
     * Ban check by identity of the person: name and height never change on a
     * Rider (items can), so those two fields decide whether it's the same
     * rider. This also catches a banned rider who is a different object.
     */
    private boolean isBanned(Rider rider)
    {
        for (Rider banned : banList)
        {
            if (banned.getName().equals(rider.getName())
                && banned.getHeight() == rider.getHeight())
            {
                return true;
            }
        }
        return false;
    }


    /** Builds a random rider without adding it to the queue. */
    private Rider generateRider()
    {
        String name = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)] + " "
            + LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        int height = random.nextInt(HEIGHT_RANGE) + MIN_RANDOM_HEIGHT;
        String items = ITEMS[random.nextInt(ITEMS.length)];
        return new Rider(name, height, items);
    }
}