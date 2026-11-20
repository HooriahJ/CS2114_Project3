# Papa's Ziplineria

Project 3 — Deliverable 4: Second Prototype + Tests

A Java console game about managing two zipline queues. Inspect each rider's height, items, name, and priority pass, then accept or reject them. Correct decisions earn 5 rating points; incorrect decisions lose 10. Priority riders must be served before regular riders. Both lines use linked FIFO queues with constant-time enqueue, dequeue, and peek operations.

## Compile and run

Use a JDK (Java 8–17). The game itself has no external dependencies.

```sh
javac -d bin src/ziplineGame/*.java
java -cp bin ziplineGame.Main
```

In Eclipse, import this repository as an existing project and run `ziplineGame.Main` as a Java Application. Import the course `CS2-Support` project into the same workspace for the tests; the existing `.classpath` references it.

Menu choices 1–6 inspect or prepare the selected rider; 7 selects the priority line, 8 selects the regular line, 9 accepts, 10 rejects, 11 adds an arrival, and 12 exits. Selecting or inspecting a rider does not dequeue them or change the rating. A decision removes only that line's front rider. Empty lines and actions without a selected rider are handled safely.

For a repeatable demonstration:

```sh
java -cp bin ziplineGame.Main --demo
```

In Eclipse, put `--demo` in Run Configurations → Arguments → Program arguments. This starts with these fixed riders:

| Line | FIFO order | Scenario |
| --- | --- | --- |
| Priority | Avery Pass | 48 inches, no items, real pass; verification required |
| Priority | Blake Fake | Fake pass; reject |
| Priority | Casey NoPass | Missing pass; reject |
| Regular | Drew Regular | Eligible; accepting before priority riders are served loses points |
| Regular | Ellis Items | Remove the phone before accepting |
| Regular | Frankie Short | 47 inches; reject |

Normal startup still uses random arrivals. `--demo` changes the initial scenario, not the rules or controller logic. Unrecognized launch arguments print usage and exit.

## Tests

**74 JUnit tests pass**, using the course `student.TestCase` library and JDK 17. Run the entire `test` package as JUnit tests in Eclipse. `MainTest` is now implemented, and all eight test classes execute.

On macOS/Linux, run from the repository root:

```sh
CS2_SUPPORT="/path/to/eclipse-workspace/CS2-Support" ./scripts/test.sh
```

Set `JAVA_HOME` to your JDK directory if Java is not on your PATH. The script compiles into a temporary directory, runs all test classes, returns a failing exit code if any test fails, and cleans up its compiled output. The course library prints SecurityManager deprecation warnings on JDK 17; those are separate from test failures.

| Test file | Behavior covered |
| --- | --- |
| `QueueTest` | Null enqueue, empty peek/dequeue, FIFO order after interleaved operations, draining and reusing a line |
| `PriorityQueueTest` | The same FIFO and empty/null contracts for the priority line |
| `RiderTest` | Rider data, invalid identity, missing/blank item descriptions, real/fake/missing passes, verification state |
| `PlayerTest` | Positive, zero, negative, and fractional ratings; rejection of NaN and infinity without changing the score |
| `GameControllerTest` | Height boundary, item removal, bans, empty actions, menu validation, null dependencies, startup, scoring once, accept/reject, and FIFO preservation |
| `PriorityControllerTest` | Real/fake/missing passes, priority-first penalties for either decision, safety checks after verification, priority-only startup, empty selection, and fresh verification for returning visits |
| `MainGameTest` | Console whitespace, malformed input and overflow, EOF, unselected actions, item removal, acceptance, and rejection |
| `MainTest` | Actual application startup, EOF, launch validation, and the demo's priority decision path |

The first six classes exercise the controller/model directly without console input. The final two add integration checks for the console and entry point. Fixtures supply known riders and seeded randomness so important priority outcomes do not depend on luck.

## One non-trivial feature added

The D4 change feature is the priority queue planned in D3. It has its own FIFO line and requires checking a real priority pass before acceptance. A fake or missing pass makes a priority rider ineligible, and serving a regular rider while priority riders wait loses rating regardless of whether the regular rider is accepted or rejected. Pass verification never bypasses height, items, or the ban list.

The feature is reachable through menu choices 6 and 7, and the view shows whether each queue has waiting riders. `MainGame` sends actions to `GameController`; the controller owns selection, validation, scoring, and removal, while the view displays its results without keeping a copy of rider, queue, or rating state.

## Bad input survives

Blank/whitespace input, letters, the text `null`, decimals, negative numbers, out-of-range choices, and integers too large to parse all return to the menu without changing game state. End-of-input exits cleanly. Empty queues, repeated item removal, pass checks without a selection, and decisions without a selection are safe. The model rejects null queue entries, invalid constructor dependencies, invalid rider identities, and non-finite ratings. Each of these cases has a test.

## Reflection

**How did the design absorb the change?**

D3 named the priority queue as the next stretch feature and assigned its implementation to Vincent and Edward. That feature is now represented by `PriorityQueue`, with integration in `GameController`, pass state in `Rider`, and priority controls/status in `MainGame`. The queue addition matched the D3 plan. The view also needed new options and feedback, which was more work than simply adding a queue class. D3 did not give a complete class-by-class change prediction.

Vincent's additional D4 work strengthened `GameController` so startup respects either occupied queue, forged passes lead to bans, and a selected rider cannot be scored repeatedly. Returning riders now get a separate visit object so an earlier pass verification cannot carry over. `Rider` and `Player` gained validation, and `Main` gained a fixed demo scenario. Tests cover those changes independently of the view, alongside the existing tests. Both queue implementations and the current `MainGame` implementation were preserved during this testing pass.

**Did the change cross the model/view line?**

The view had to learn that there are two lines and a pass-verification action, so its displayed options changed. Eligibility, priority order, and rating decisions remain in the controller; linked nodes and mutable rider objects are not exposed to the view. The fixed scenario is assembled in `Main` and uses the same controller/view path as ordinary play. No scoring rules were copied into the console code.

**What changed from the D3 plan, and why?**

We completed the priority feature and deferred the timer. The test work expanded beyond the original view-test plan because priority behavior needed direct tests for forged passes, missing passes, skipping the priority line, and returning riders. `MainTest` is no longer a placeholder. An additional controller constructor accepts both queues and a random source, which lets the demo and tests start from known scenarios while retaining the existing constructors. Validation now rejects invalid rider identities and non-finite ratings before they can corrupt later game behavior.

**What does not work yet?**

The prototype is playable, and the current 74 tests pass. It still uses manual arrivals through choice 11 and has no finite-day completion or end-of-day summary. Difficulty selection is planned for the final build. Timer pressure, rush periods, and a graphical view are not implemented. The tests cover the documented scenarios, not every possible random play sequence.

**What design change would make the next feature cheaper?**

A small game-settings object could hold difficulty, arrival limits, and score adjustments. The controller could use it for decisions while the view only chooses and displays the settings. Keeping these rules in one place would make difficulty and day completion easier to add without duplicating conditions in the console menu.

## Next Steps

**Remaining work, in order, and owners**

| Order | Item | Owner |
| --- | --- | --- |
| 1 | Finalize finite-day completion and an end-of-day summary; review edge cases in game flow | Edward |
| 2 | Implement the planned difficulty selection and its controller rules | Hooriah |
| 3 | Add difficulty and day-boundary model tests | Vincent |
| 4 | Review startup/remaining console paths and rehearse the timed demo | Vihaan |

These are the proposed remaining assignments; completed work is recorded separately below. The existing difficulty and bug-fixing owners are retained, and the former `MainTest` task is now a review/demo task because startup tests are implemented.

**Stretch goals**

Keep the implemented priority queue and commit to difficulty selection for the final build. Drop the timer and rush-period ideas from the final-build plan so the team can finish day flow, validation, and testing.

**Three-minute demo plan**

Start with `--demo`. Each number below is a separate menu entry.

| Time budget | Actions | Expected result |
| --- | --- | --- |
| 0:00–0:25 | Explain both lines; enter `hello`, `-1`, and a very large integer | Invalid input returns to the menu; rating remains 5 |
| 0:25–0:45 | `8`, `9` | Drew is accepted out of order; rating becomes -5 |
| 0:45–1:15 | `7`, `1`, `2`, `6`, `9` | Avery is exactly 48 inches; verify the real pass and accept; rating becomes 0 |
| 1:15–1:40 | `7`, `6`, `10`, then `7`, `6`, `10` | Reject Blake's fake pass and Casey's missing pass; rating becomes 10 |
| 1:40–2:10 | `8`, `3`, `4`, `9` | Remove Ellis's phone and accept; rating becomes 15 |
| 2:10–2:30 | `8`, `2`, `10`, `5` | Reject Frankie at 47 inches; rating becomes 20; show the ban list |
| 2:30–3:00 | Show the passing JUnit run, then `12` | Explain FIFO/invalid-input coverage and exit |

This is a three-minute speaking budget, not a claim that the team has already rehearsed it.

## Attribution

Division of work across D3 and D4. Earlier authors remain credited; the added D4 entries describe the changes in this update.

| Class or test file | Contributors | Work |
| --- | --- | --- |
| `GameController.java` | Vihaan, Edward, Vincent | Vihaan: original controller; Edward: priority integration; Vincent: supplied-queue constructor, startup fix, once-per-rider scoring, fake-pass bans, and independent returning visits |
| `MainGame.java` | Vincent, Edward | Initial console implementation and controller connection; Vincent: earlier whitespace and EOF fixes; Edward: priority menu integration |
| `Player.java` | Edward, Vincent | Edward: original rating model; Vincent: finite-rating validation |
| `Rider.java` | Hooriah, Vincent, Edward | Hooriah and Vincent: original rider model/integration; Edward: priority-pass changes; Vincent: identity and blank-item validation |
| `Queue.java` | Hooriah, Vincent | Original linked FIFO queue and integration |
| `PriorityQueue.java` | Vincent, Edward | Linked priority-line implementation |
| `Main.java` | Edward, Vincent | Edward: original entry point; Vincent: repeatable demo scenario and launch-option validation |
| `GameControllerTest.java` | Vihaan, Edward, Vincent | Original controller tests and priority-era revisions; Vincent: invalid input, empty actions, height boundary, item/FIFO behavior, bans, startup, dependency validation, and duplicate-scoring regressions |
| `PriorityControllerTest.java` | Vincent | Added model-only priority scenarios for real/fake/missing passes, queue-order penalties, safety rules, startup, and returning visits |
| `MainGameTest.java` | Vincent, Edward | Earlier input regressions and priority-menu updates; Vincent: expanded malformed-input, no-selection, item-removal, and rejection coverage |
| `MainTest.java` | Vincent | Replaced placeholder with startup, EOF, demo integration, and invalid-launch tests |
| `PlayerTest.java` | Edward, Vincent | Edward: original rating tests; Vincent: invalid numeric rating tests |
| `RiderTest.java` | Vincent, Edward | Original rider tests and priority additions; Vincent: blank-item, invalid-identity, and missing-pass coverage |
| `QueueTest.java` | Edward, Vincent | Edward: original queue tests; Vincent: interleaved FIFO, null-enqueue preservation, emptying, and reuse coverage |
| `PriorityQueueTest.java` | Hooriah, Vihaan, Vincent | Earlier priority queue tests; Vincent: interleaved FIFO, null-enqueue preservation, emptying, and reuse coverage |

Vincent also added `scripts/test.sh` and updated the run instructions, test summary, reflection, and demo plan in this README.
