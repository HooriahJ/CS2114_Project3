package ziplineGame;

import java.util.ArrayList;

public class GameController
{
    private Queue queue;
    private Player player;
    private ArrayList<Rider> banList;
    private ArrayList<Rider> previousRiders;

    public GameController(Queue queue, Player player)
    {
        this.queue = queue;
        this.player = player;
        this.banList = new ArrayList<Rider>();
        this.previousRiders = new ArrayList<Rider>();
        Rider[] startingBannedRiders = new Rider[3];

        for (int i = 0; i < 3; i++)
        {
            getNewRider();
            startingBannedRiders[i] = queue.peek();
            queue.dequeue();
        }

        for (int i = 0; i < 3; i++)
        {
            previousRiders.add(startingBannedRiders[i]);
            banList.add(startingBannedRiders[i]);
        }
    }


    public boolean isValid(String input)
    {
        if (input == null)
        {
            return false;
        }

        try
        {
            int choice = Integer.parseInt(input);

            return choice >= 1 && choice <= 9;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }


    public boolean serveNextRider()
    {
        Rider rider = queue.peek();

        if (rider == null)
        {
            return false;
        }

        if (rider.getHeight() < 48) // arbitrary number
        {
            addToBanList(rider);
        }

        previousRiders.add(rider);

        queue.dequeue();

        return true;
    }


    public double getCurrentRating()
    {
        return player.getRating();
    }


    public String getFrontRiderName()
    {
        Rider rider = queue.peek();

        if (rider == null)
        {
            return "No current rider";
        }

        return rider.getName();
    }


    public int getFrontRiderHeight()
    {
        Rider rider = queue.peek();

        if (rider == null)
        {
            return -1;
        }

        return rider.getHeight();
    }


    public String getFrontRiderItems()
    {
        Rider rider = queue.peek();

        if (rider == null)
        {
            return "No current rider";
        }

        return rider.getItems();
    }


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


    public boolean correctChoice()
    {
        Rider rider = queue.peek();

        if (rider == null)
        {
            return false;
        }

        if (rider.getHeight() < 48) // arbitrary number
        {
            return false;
        }

        if (banList.contains(rider))
        {
            return false;
        }

        if (!rider.getItems().equals("None"))
        {
            return false;
        }

        return true;
    }


    public boolean getNewRider()
    {
        if (!previousRiders.isEmpty() && Math.random() < 0.20)
        {
            int randomRider = (int)(Math.random() * previousRiders.size());

            return queue.enqueue(previousRiders.get(randomRider));
        }
        String[] firstNames = { "Liam", "Olivia", "Noah", "Emma", "Oliver",
            "Amelia", "Theodore", "Charlotte", "James", "Mia", "Henry",
            "Sophia", "Mateo", "Isabella", "Elijah", "Evelyn", "Lucas", "Ava",
            "William", "Sofia", "Benjamin", "Camila", "Levi", "Harper", "Ezra",
            "Luna", "Sebastian", "Eleanor", "Jack", "Violet", "Daniel",
            "Aurora", "Samuel", "Elizabeth", "Michael", "Eliana", "Grayson",
            "Hazel", "Ethan", "Chloe", "Asher", "Ellie", "John", "Nora",
            "Hudson", "Gianna", "Luca", "Lily", "Leo", "Emily", "Elias", "Aria",
            "Kai", "Scarlett", "Theo", "Willow", "Owen", "Penelope",
            "Alexander", "Zoe", "Dylan", "Ella", "Gabriel", "Avery", "Santiago",
            "Elena", "Mason", "Abigail", "Julian", "Mila", "David", "Lucy",
            "Joseph", "Isla", "Carter", "Ivy", "Matthew", "Layla", "Luke",
            "Delilah", "Aiden", "Riley", "Jackson", "Lainey", "Maverick",
            "Nova", "Miles", "Grace", "Wyatt", "Goldie", "Thomas", "Winter",
            "Isaac", "Arabella", "Josiah", "Anastasia", "Hugo", "Myla",
            "Arthur", "Leah" };
        String[] lastNames = { "Smith", "Johnson", "Williams", "Brown", "Jones",
            "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez",
            "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor",
            "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White",
            "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
            "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres",
            "Nguyen", "Hill", "Flores", "Green", "Adams", "Nelson", "Baker",
            "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts",
            "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker", "Cruz",
            "Edwards", "Collins", "Reyes", "Stewart", "Morris", "Morales",
            "Murphy", "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan",
            "Cooper", "Peterson", "Bailey", "Reed", "Kelly", "Howard", "Ramos",
            "Kim", "Cox", "Ward", "Richardson", "Watson", "Brooks", "Chavez",
            "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes",
            "Price", "Alvarez", "Castillo", "Sanders", "Patel", "Myers", "Long",
            "Ross", "Foster", "Jimenez" };
        String[] items = { "None", "None", "None", "None", "None", "Food",
            "Drink", "Phone", "Keys", "Wallet, Backpack" };
        int randomFirstName = (int)(Math.random() * firstNames.length);
        int randomLastName = (int)(Math.random() * lastNames.length);
        int randomItems = (int)(Math.random() * items.length);
        int height = (int)(Math.random() * 50) + 30;
        String finalName =
            firstNames[randomFirstName] + " " + lastNames[randomLastName];
        Rider rider = new Rider(finalName, height, items[randomItems]);
        return queue.enqueue(rider);
    }


    public boolean calculateRatingChange(boolean accepted)
    {
        if (player == null)
        {
            return false;
        }

        boolean shouldAccept = correctChoice();
        boolean correctDecision = accepted == shouldAccept;

        if (correctDecision)
        {
            player.setRating(player.getRating() + 5);
        }
        else
        {
            player.setRating(player.getRating() - 10);
        }

        return correctDecision;
    }


    public boolean addToBanList(Rider rider)
    {
        if (rider == null)
        {
            return false;
        }
        if (banList.contains(rider))
        {
            return false;
        }
        banList.add(rider);
        return true;
    }


    public String getBanList()
    {
        if (banList.isEmpty())
        {
            return "The ban list is empty.";
        }

        String summary = "Ban List:\n";

        for (int i = 0; i < banList.size(); i++)
        {
            summary = summary + banList.get(i).getName() + "\n";
        }

        return summary;
    }


    public boolean removeRiderItems()
    {
        Rider rider = queue.peek();
        if (rider == null)
        {
            return false;
        }
        if (rider.getItems().equals("None"))
        {
            return false;
        }
        return rider.setItems("None");
    }
}
