package ziplineGame;

import java.util.Scanner;

public class MainGame

{
    private String userInput;
    private boolean isRunning;

    public void run(GameController controller)
    {
        Scanner scanner = new Scanner(System.in);
        isRunning = true;
        while (isRunning)
        {
            System.out.println(
                "---------------------------------------------------------------");
            System.out.println("ZIPLINE RIDER RULES");
            System.out.println("Minimum height: 48 inches");
            System.out.println("Riders cannot bring items onto the ride.");
            System.out.println(
                "You may be banned from riding if you do not follow these rules.");
            System.out.println(
                "---------------------------------------------------------------");
            displayStatus(
                controller.getFrontRiderSummary(),
                controller.getCurrentRating());
            System.out.println("1. Check name of rider");
            System.out.println("2. Check height of rider");
            System.out.println("3. Check items of rider");
            System.out.println("4. Remove items of rider");
            System.out.println("5. Check ban list");
            System.out.println("6. Accept Rider");
            System.out.println("7. Reject Rider");
            System.out.println("8. Get New Rider");
            System.out.println("9. Exit Game");
            System.out.print("\nEnter your choice:  ");
            if (!scanner.hasNextLine())
            {
                isRunning = false;
                System.out.println("Game exited. Thank you for playing!");
                break;
            }
            userInput = scanner.nextLine().trim();
            System.out.println("---------------------------");
            if (controller.isValid(userInput) == false)
            {
                System.out.println("Invalid input. Please try again.");
            }
            else
            {
                int choice = Integer.parseInt(userInput); // change to int
                if (choice == 1)
                {
                    System.out.println(
                        "Rider Name: " + controller.getFrontRiderName());
                }
                else if (choice == 2)
                {
                    if (controller.getFrontRiderHeight() == -1)
                    {
                        System.out
                            .println("Rider Height: " + "No current rider");
                    }
                    else
                    {
                        System.out.println(
                            "Rider Height: " + controller.getFrontRiderHeight()
                                + " inches");
                    }
                }
                else if (choice == 3)
                {
                    System.out.println(
                        "Rider Items: " + controller.getFrontRiderItems());
                }
                else if (choice == 4)
                {
                    if (controller.removeRiderItems())
                    {
                        System.out.println("Removed riders items.");
                    }
                    else
                    {
                        System.out.println("Could not remove rider items.");
                    }
                }
                else if (choice == 5)
                {
                    System.out.println(controller.getBanList());
                }
                else if (choice == 6)
                {
                    if (controller.getFrontRiderSummary()
                        .equals("No riders are waiting."))
                    {
                        System.out.println("No riders are waiting.");
                    }
                    else
                    {
                        System.out.println("Rider accepted onto ride.");
                        if (controller.calculateRatingChange(true))
                        {
                            System.out.println("You made the correct choice.");
                        }
                        else
                        {
                            System.out.println("You made the wrong choice.");
                        }
                        controller.serveNextRider();
                    }
                }
                else if (choice == 7)
                {
                    if (controller.getFrontRiderSummary()
                        .equals("No riders are waiting."))
                    {
                        System.out.println("No riders are waiting.");
                    }
                    else
                    {
                        System.out.println("Rider rejected.");
                        if (controller.calculateRatingChange(false))
                        {
                            System.out.println("You made the correct choice.");
                        }
                        else
                        {
                            System.out.println("You made the wrong choice.");
                        }
                        controller.serveNextRider();
                    }
                }
                else if (choice == 8)
                {
                    if (controller.getNewRider())
                    {
                        System.out.println("New rider added to the queue.");
                    }
                    else
                    {
                        System.out.println("Could not add new rider.");
                    }
                }
                else if (choice == 9)
                {
                    isRunning = false;
                    System.out.println("Game exited. Thank you for playing!");
                }
            }
        }
    }


    public void displayStatus(String frontRiderSummary, double rating)
    {
        if (frontRiderSummary.equals("No riders are waiting."))
        {
            System.out.println("Front Rider: No riders are waiting.");
        }
        else
        {
            System.out.println("Front Rider: Rider waiting.");
        }
        System.out.println("Player Rating: " + rating);
        System.out.println("-------------------------");
    }
}
