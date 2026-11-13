# Papa's Ziplineria

# Model and tests
- import the project and the course `CS2-Support` project into the same eclipse workspace. `.classpath` depends on `CS2-Support` for `student.TestCase`.
- run the classes in `src/test` as JUnit tests. `MainTest.java` is still a placeholder.
- `MainGameTest` covers padded menu choices, invalid input, and end-of-input before and after a rider decision. tests restore the original input/output streams after each run.

**Run the game**
- run `ziplineGame.Main` as a java application. use choices 1–8 to inspect or manage riders, and 9 to exit.
- spaces around a menu choice are allowed. invalid choices show an error and return to the menu. end-of-input exits cleanly.
- to compile and run from the repo root with a JDK installed:

```text
javac -d bin src/ziplineGame/*.java
java -cp bin ziplineGame.Main
```
# Thin or stubbed view
Our game currently has a basic user interface that is able to tell the user what is happening.
# Runs without crashing on the MVP path
Game does not crash, even during bad input, and all MVP is met.
# Reflection
**What changed from your specification?**
- We realized that we needed a Main file to run the game, so we made one. Group decision.
- Added `getFrontRiderName`, `getFrontRiderHeight`, and `getFrontRiderItems` to `GameController.java` for ease of access. Without it, accessing this information is much more difficult. Group decision.
- Added a 20% chance for riders to come back. This is so the banned list can have a function since previously riders would never come back and so nobody on the ban list would ever come. Group decision.
- Added a initial list of banned riders. This is so the game doesn't start with a empty banned list. Group decision.

**Did the model really stand alone?**
  - Yes, we didn't have to make any changes to our Model/View/Controller system.

**What happened to your unknowns?**
- We experimented and figured out how much rating a player should get for getting it right and wrong. There are still some unknowns because they are part of our stretch goals.

**What doesn't work yet?**
- `MainTest.java` is still a placeholder. view tests cover input handling, but not every menu action or display branch.
  
# Next steps
**What is left to finish the MVP?**
- expand view coverage for the remaining menu actions and add a startup test for `Main`.

**Which stretch goals will you attempt?**
  - The priority queue should be pretty easy to implement. The priority queue would need `GameController.java` so that it could check if your choice is correct (correctChoice) with the priority queue and to help place things into the priority queue (getNewRider and generateRider), and `Rider.java` would need a new boolean in its constructor to indicate whether the rider is of priority queue or not. For example, Rider(String name, int height, String items, boolean priority).
  - Allow the user to choose a difficulty to play on. This would also need` GameController.java` to change how much rating you get per correct rider and how much rating you lose per wrong rider (calculateRatingChange), and would also make it more difficult by adding more people to the ban list (our initial banned riders consturctor will have more riders), and making the height closer to the minimum height (generateRider would generate heights closer to minimum height)
  - We will attempt to add timing the user so they feel rushed, and so they get varied rating, but this may be too difficult to implement. `GameController.java` would need new methods to calculate how long it takes for the user to make a decision (something like a timeUser method), and calculateRatingChange would calculate the change in rating based on time, and whether you are correct. 

**What tests will you need?**
- add view coverage for empty queues, item removal, and rejection. add tests for any stretch goals implemented later.

**Who does what?**
Division of Work

| Class | Author |
| --- | --- |
| MainGameTest.java | Vihaan (remaining menu coverage), Vincent (input regression tests) |
| MainTest.java | Hooriah |
| PriorityQueue.java | Vincent & Edward |
| Bug fixing & Connecting | Edward |

# Attribution
Division of work
| Class | Author | Work |
| --- | --- | --- |
| GameController.java | Vihaan | Wrote |
| MainGame.java | Vincent, Edward | initial implementation and controller connection; Vincent: padded input and end-of-input fixes |
| Player.java | Edward | Wrote |
| Rider.java | Hooriah, Vincent | Wrote, connected to GameController.java and Rider.java |
| Queue.java | Hooriah, Vincent | Wrote, connected to GameController.java and Rider.java |
| Main.java | Edward | Wrote, connected to MainGame.java |
| GameControllerTest.java | Vihaan | Wrote |
| PlayerTest.java | Edward | Wrote |
| RiderTest.java | Vincent | Wrote |
| MainGameTest.java | Vincent | input regression tests: whitespace, invalid choices, and end-of-input |
| Readme.md | Edward, Hooriah, Vihaan, Vincent | Wrote and revised different parts of Readme.md |

# GenAI Note
**GenAI was used to help write code, and verify that our test cases tested every case.**
