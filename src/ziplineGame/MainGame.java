
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
            System.out
                .println("------------------------------------------------");
            System.out.println("ZIPLINE RIDER RULES");
            System.out.println("Minimum height: 48 inches");
            System.out.println("Riders cannot bring items onto the ride.");
            System.out.println("Priority riders need a valid priority pass.");
            System.out.println("Priority passes must be verified.");
            System.out
                .println("------------------------------------------------");

            displayStatus(controller);

            System.out.println("1. Check Name");
            System.out.println("2. Check Height");
            System.out.println("3. Check Items");
            System.out.println("4. Remove Items");
            System.out.println("5. Check Ban List");
            System.out.println("6. Verify Priority Pass");
            System.out.println("7. Help Person at Priority Queue");
            System.out.println("8. Help Person at Regular Queue");
            System.out.println("9. Accept Rider");
            System.out.println("10. Reject Rider");
            System.out.println("11. Get New Rider");
            System.out.println("12. Exit Game");

            System.out.print("\nEnter your choice: ");

            if (!scanner.hasNextLine())
            {
                isRunning = false;
                System.out.println("Game exited. Thank you for playing!");
                break;
            }

            userInput = scanner.nextLine().trim();

            System.out.println("---------------------------");

            if (!controller.isValid(userInput))
            {
                System.out.println("Invalid input. Please try again.");
            }
            else
            {
                int choice = Integer.parseInt(userInput);

                if (choice == 1)
                {
                    System.out.println(
                        "Rider Name: " + controller.getFrontRiderName());
                }
                else if (choice == 2)
                {
                    if (!controller.hasCurrentRider())
                    {
                        System.out.println("No current rider.");
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
                    if (!controller.hasCurrentRider())
                    {
                        System.out.println("No current rider.");
                    }
                    else if (controller.removeRiderItems())
                    {
                        System.out.println("You removed the rider's items.");
                    }
                    else
                    {
                        System.out.println("They have no items.");
                    }
                }
                else if (choice == 5)
                {
                    System.out.println(controller.getBanList());
                }
                else if (choice == 6)
                {
                    System.out.println(controller.verifyFrontPriority());
                }
                else if (choice == 7)
                {
                    if (controller.helpPriorityQueue())
                    {
                        System.out.println(
                            "You are now helping the person from the Priority Queue.");
                    }
                    else
                    {
                        System.out
                            .println("There is nobody at the Priority Queue.");
                    }
                }
                else if (choice == 8)
                {
                    if (controller.helpRegularQueue())
                    {
                        System.out.println(
                            "You are now helping the person from the Regular Queue.");
                    }
                    else
                    {
                        System.out
                            .println("There is nobody at the Regular Queue.");
                    }
                }
                else if (choice == 9)
                {
                    if (!controller.hasCurrentRider())
                    {
                        System.out
                            .println("You are not currently helping a rider.");
                    }
                    else
                    {
                        boolean correct = controller.processDecision(true);

                        if (correct)
                        {
                            System.out
                                .println("You correctly accepted the rider!");
                        }
                        else
                        {
                            System.out
                                .println("You incorrectly accepted the rider.");
                        }
                    }
                }
                else if (choice == 10)
                {
                    if (!controller.hasCurrentRider())
                    {
                        System.out
                            .println("You are not currently helping a rider.");
                    }
                    else
                    {
                        boolean correct = controller.processDecision(false);

                        if (correct)
                        {
                            System.out
                                .println("You correctly rejected the rider!");
                        }
                        else
                        {
                            System.out
                                .println("You incorrectly rejected the rider.");
                        }
                    }
                }
                else if (choice == 11)
                {
                    controller.getNewRider();
                    System.out.println("A new person has entered a queue!");
                }
                else if (choice == 12)
                {
                    System.out.println("Thank you for playing!");
                    isRunning = false;
                    scanner.close();
                }
            }
        }
    }


    private static void displayStatus(GameController controller)
    {
        System.out.println();

        if (controller.hasPriorityQueueRider())
        {
            System.out.println("Person waiting at Priority Queue");
        }
        else
        {
            System.out.println("Nobody waiting at Priority Queue");
        }

        if (controller.hasRegularQueueRider())
        {
            System.out.println("Person waiting at Regular Queue");
        }
        else
        {
            System.out.println("Nobody waiting at Regular Queue");
        }

        if (controller.hasCurrentRider())
        {
            System.out.println("You are currently helping a rider.");
        }
        else
        {
            System.out.println("You are not currently helping anyone.");
        }

        System.out.println("Player Rating: " + controller.getCurrentRating());
        System.out.println();
    }
}
