# Papa's Ziplineria

# Model works
**Run the model**
- Run the test package to run all of our JUnit tests. All 28 JUnit tests should pass and test various behaviors including invalid values.

**Run the view**
- Run Main.java and the user should be able to see the basic rules, what and how to input, whether their inputs are valid or invalid, information about the rider at the top of the queue, and their rating.
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
  - We are still missing test cases for our View.
  
# Next steps
**What is left to finish the MVP?**
- Our MVP is actually almost already complete, just need to finish the test cases for the `View` and `bug fixes`

**Which stretch goals will you attempt?**
  - The priority queue should be pretty easy to implement
  - We will attempt to add timing the user so they feel rushed, and so they get varied rating, but this may be too difficult to implement.

**What tests will you need?**
- We will need tests for our `View` still and any stretch goals we add will also need tests.

**Who does what?**
Division of Work

| Class | Author |
| --- | --- |
| MainGameTest.java | Vihaan |
| MainTest.java | Hooriah |
| PriorityQueue.java | Vincent & Edward |
| Bug fixing & Connecting | Edward |

# Attribution
Division of work
| Class | Author | Work |
| --- | --- | --- |
| GameController.java | Vihaan | Wrote |
| MainGame.java | Vincent, Edward | Wrote, connect to GameController.java |
| Player.java | Edward | Wrote |
| Rider.java | Hooriah, Vincent | Wrote, connected to GameController.java and Rider.java |
| Queue.java | Hooriah, Vincent | Wrote, connected to GameController.java and Rider.java |
| Main.java | Edward | Wrote, connected to MainGame.java |
| GameControllerTest.java | Vihaan | Wrote |
| PlayerTest.java | Edward | Wrote |
| RiderTest.java | Vincent | Wrote |

# GenAI Note
We used GenAI to make sure our test cases tests every possible case, and to assist with coding.
