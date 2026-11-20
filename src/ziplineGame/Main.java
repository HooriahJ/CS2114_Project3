package ziplineGame;

import java.util.Random;

/** Launches the console game or a fixed scenario for the three-minute demo. */
public class Main
{
    /**
     * Starts a normal game, or a repeatable game when --demo is supplied.
     *
     * @param args empty for random arrivals, or a single --demo option
     */
    public static void main(String[] args)
    {
        if (args.length > 1 || (args.length == 1 && !"--demo".equals(args[0])))
        {
            System.out.println("Usage: java ziplineGame.Main [--demo]");
            return;
        }

        Queue queue = new Queue();
        PriorityQueue priority = new PriorityQueue();
        Player player = new Player(5.0);
        boolean demo = args.length == 1;
        if (demo)
        {
            priority.enqueue(new Rider("Avery Pass", 48, "None", true));
            priority.enqueue(new Rider("Blake Fake", 60, "None", true, true));
            priority.enqueue(new Rider("Casey NoPass", 60, "None"));
            queue.enqueue(new Rider("Drew Regular", 60, "None"));
            queue.enqueue(new Rider("Ellis Items", 60, "Phone"));
            queue.enqueue(new Rider("Frankie Short", 47, "None"));
        }
        GameController controller = new GameController(queue, priority, player,
            demo ? new Random(2114) : new Random());
        controller.startGame();
        MainGame game = new MainGame();
        System.out.println("PAPA'S ZIPLINERIA");
        if (demo)
        {
            System.out.println("Demo: three priority riders and three regular riders.");
        }
        game.run(controller);
    }
}
