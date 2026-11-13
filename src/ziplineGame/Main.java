package ziplineGame;

public class Main
{
    public static void main(String[] args)
    {
        Queue queue = new Queue();
        Player player = new Player(5.0);
        GameController controller = new GameController(queue, player);
        controller.startGame();
        MainGame game = new MainGame();
        System.out.println("PAPA'S ZIPLINERIA");
        game.run(controller);
    }

}