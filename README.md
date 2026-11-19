# Papa's Ziplineria

# Model and tests
- import the project and the course `CS2-Support` project into the same eclipse workspace. `.classpath` depends on `CS2-Support` for `student.TestCase`.
- run the classes in `src/test` as JUnit tests. `MainTest.java` is still a placeholder.
- `MainGameTest` covers padded menu choices, invalid input, and end-of-input before and after a rider decision. tests restore the original input/output streams after each run.

**Run the game**
- run `ziplineGame.Main` as a java application. use choices 1–11 to inspect or manage riders, and 12 to exit.
- spaces around a menu choice are allowed. invalid choices show an error and return to the menu. end-of-input exits cleanly.
- to compile and run from the repo root with a JDK installed:

```text
javac -d bin src/ziplineGame/*.java
java -cp bin ziplineGame.Main
```
# One non-trivial feature added

We added a priority queue system. Riders can come from either the regular queue, or the priority queue. Riders from the priority queue must be served first, or else the player loses rating. Riders in the priority queue need to be verified that they have a priority pass to be allowed through the priority queue. If they do not, then they must be rejected.

# JUnit tests that run

We have 41 JUnit tests that pass.

# Bad input survives

Attempting to input "null" or anything that is not between 1-12 will tell the user their input was invalid, and to try again. Attempting to check information about a rider when there is no rider tells the user that there is no rider.

# Reflection
**How did your design absorb the change?**

We had to add `PriorityQueue.java` to handle priority queues, and we had to modify `GameController.java` to accomodate for `PriorityQueue.java`, and modify `Rider.java` for a boolean for if a rider is part of the priority queue or not. The only thing that we didn't write in Deliverable 3 that we did change for `PriorityQueue.java` is we had to also update `MainGame.java` (the view) so that the user could actually see the information about the priority queue.

**Did the change cross the model/view line?**

No, the change didn't have to cross the model/view line.

**What changed from own Deliverable 3 plan?**

We had to update `MainGame.java` to add `PriorityQueue.java` and some of our tests also had to be changed to accomodate for `PriorityQueue.java`. We planned to write `MainTest.java`, but for now it is still a placeholder. We also had some other potential ideas planned to be added like the timer, but we decided to push that off to the final product.

**What doesn't work yet?**

`MainTest.java` currently is just a placeholder.

# Next Steps

**What is left for the final build?**

We need to double check to make sure all of our current code works as intended and our tests cases do not fail to catch any cases.

**Which stretch goals are you committing to, and which are you dropping?**

We will drop the timer idea and instead allow the user to choose a difficulty when starting the game.

**What will your 3-minute demo show?**

It will show the user getting a rider, and picking the wrong choice first to show that picking the wrong choice results in losing rating. Then, we will get another rider and make the correct choice to show that picking the correct choice results in gaining rating. Then, we will show bad input cases like attempting to put in negative numbers, things that aren't numbers, and null. After that, we will show our priority queue working as intended by intentionally ignoring the priority queue and showing that you lose rating for ignoring the priority queue. Then, we will play the game normally until we reach 25 rating.

**Who does what?**

| Item | Author |
| --- | --- |
| MainTest.java | Vihaan |
| Difficulty | Hooriah |
| Difficulty Test | Vincent |
| Bugfixing | Edward |

# Attribution
Division of work (D3 & D4)
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
| PriorityQueue.java | Vincent & Edward | Wrote |
| PriorityQueueTest.java | Hooriah & Vihaan | Wrote |

# GenAI Note
**GenAI was used to help write code, and verify that our test cases tested every case.**
