# Comp2042 DMS Coursework

## Candidate
Name: Nigel Choong Zi Wei 

Course: Year 2 Mathematics and Data Science 

## Table of Contents

1. [GitHub Repository](#github-repository)
2. [Compilation & Run Instructions](#compilation-and-run-instructions)
3. [Function Implemented](#function-implemented)
   - [Implemented and Working Properly](#implemented-and-working-properly)
   - [Implementation challenges](#implementation-challenges)
   - [Features Not Implemented](#features-not-implemented)
4. [Program Classes Modification Detail](#program-classes-modification-detail)
   - [Modified Classes Function](#modified-classes-function)
   - [New Java Classes](#new-java-classes)
   - [Additional Refactoring and Improvements](#additional-refactoring-and-improvements)
5. [Unexpected Problem](#unexpected-problem)

## Github Repository
→ https://github.com/ChoongNigel/Y2-Nigel-Choong-20700412-DMS-CW.git

## Compilation and Run Instructions
1. **Open the project in IntelliJ IDEA**
    - Start IntelliJ.
    - Click **File → Open…**.
    - Select the project folder (the one containing `pom.xml`) and click **OK**.
    - Let IntelliJ import it as a **Maven** project.


2. **Configure the JDK**
    - Go to **File → Project Structure → Project**.
    - Set **Project SDK** to a compatible JDK (e.g. _Oracle OpenJDK 25_).
    - Apply and close the dialog.


3. **Wait for Maven to import dependencies**
    - IntelliJ will automatically download dependencies (JavaFX, etc.).
    - Watch the bottom status bar until indexing and Maven import are finished.


3. **Create a Maven run configuration**
    - Open **Run → Edit Configurations…**.
    - Click the **+** button (Add New Configuration).
    - Select **Maven**.
    - In **Name**, enter something like: `Run JavaFX`.
    - In **Working directory**, make it point to `CW2025` directory
    - In the **Command line** field, type:
      ```text
      javafx:run
      ```
    - Click **Apply** and **OK**.


4. **Run the game**
    - Select the `Run JavaFX` Maven configuration in the run configuration dropdown (top‑right).
    - Click the green **Run** button (or press `Shift+F10`).
    - Maven will compile the project and start the JavaFX application.


## Function implemented
### Implemented and Working Properly

1. **Hold Function**
    - Pressing `C` or `Shift` holds the current tetromino and immediately spawns the next brick.
    - Pressing `C`/`Shift` again swaps the held brick with the currently falling brick.
    - Hold is limited to once per spawned piece (cannot spam hold on the same piece).


2. **Next Brick Preview**
    - A “Next Brick” panel shows the upcoming tetromino.
    - The preview updates correctly each time a new piece is spawned.


3. **Ghost Brick Preview**
   - A semi‑transparent “ghost” version of the current tetromino is shown on the board at the row where the piece would land if dropped straight down.
   - The ghost position updates correctly whenever the player moves or rotates the active piece.


4. **User Registration & Login with High Scores**
    - Players can register with a username and password, and then log in.
    - Each user has a personal high score; this is updated only when a new score is higher.
    - User data (username, password, high score) is persisted to `users.txt` and reloaded whenever the game starts, so scores are kept between runs.


5. **Main Menu Scene**
    - Main menu allows:
        - Playing as an anonymous guest.
        - Logging in as a registered user.
        - Viewing a leaderboard/ranking screen for current users.
    - Navigation between main menu, login/register, game, and leaderboard scenes works correctly.


6. **Playable Levels (Difficulty Progression)**
    - Three levels are implemented.
    - Level 1: starting level.
    - Level 2: unlocked at 2000+ points.
    - Level 3: unlocked at 5000+ points.
    - As the level increases, the automatic brick drop speed increases (game becomes harder).


7. **Sound Effects and Background Music**
    - Key actions (move, rotate, hard drop, line clear, level up) trigger appropriate sound effects.
    - Different scenes use different background music tracks (e.g. main menu vs. in‑game).
    - Level transitions in the game use different BGM, with smooth transitions (no abrupt cuts).


8. **Pause Functionality**
    - The game can be paused at any time (e.g. via `ESC` or pause button).
    - While paused:
        - The automatic drop timeline is stopped.
        - The pause panel is shown.
    - Resuming continues the game seamlessly from the exact previous state (no loss of progress).


9. **Dynamic Background (Infinite Cycle)**
    - The game scene uses an animated video background (`MediaView`) that loops continuously.
    - Background playback is independent of game state (pause/game over) and gives a dynamic visual effect.

### Implementation challenges

1. **Crashes and Visual Bugs When Adding Hard Drop, Hold, and Next Brick** *(Resolved)*
    - **Problem:**  
      When I first implemented the hard drop, hold, and next‑brick preview features, the entire program sometimes crashed or behaved incorrectly:
        - Score stopped updating correctly or jumped to unexpected values.
        - The active brick panel occasionally disappeared.
        - Sometimes the game would freeze or exit unexpectedly after a drop/hold.
    - **Cause:**  
      I initially modified only the obvious classes (e.g. `GuiController` and `GameController`) and overlooked the impact on:
        - `SimpleBoard` (game state and brick management),
        - `Score` (scoring rules and bindings),
        - `ViewData` (extra fields for next/ghost/held bricks).  
          Because several parts of the system depend on these core classes, small changes (e.g. in how a brick is spawned or merged) caused side effects elsewhere.
    - **How I Addressed It:**
        - Took a step back and traced flows starting from the **interfaces** (`Board`, `InputEventListener`, `GameUIControl`) to see what methods are used and where data flows.
        - Reviewed all related classes whenever adding a new feature, not just the controller:
            - Updated `SimpleBoard` correctly for hold/next/ghost.
            - Ensured `Score` and `ViewData` provided all necessary information without breaking existing bindings.
        - Introduced helper methods (e.g. for spawning bricks, recomputing speed/level) so that logic is centralized instead of duplicated.  
          After these restructurings, hard drop, hold, and next‑brick preview all work together without crashes, and scoring/visuals behave as expected.


2. **Inconsistent Background Video Display** *(Unresolved)*
    - **Problem:**  
      Occasionally, when starting the game, the animated background video does not appear:
        - On some runs, the background loads and loops correctly.
        - On others, the game starts with no background, but if I change scenes and come back, it suddenly starts working.
    - **Investigation & Possible Causes:**
        - Checked the `Background.ApplyGif(...)` method and confirmed it is called in the relevant controllers (e.g. `GuiController.initialize`, `User_Register.initialize`).
        - Verified resource paths and that the media file is in the expected location under `src/main/resources`.
        - Noticed that the issue is intermittent rather than deterministic, which suggests a timing or loading issue rather than a simple path error.
        - One suspicion is that the **media file size** or encoding is relatively large/heavy, which might cause delayed or failed loading in some runs of JavaFX’s media pipeline.
    - **Current Status:**
        - The game is fully playable even when the background sometimes fails to show, so this is a visual glitch rather than a functional blocker.
        - Due to limited time and the complexity of JavaFX media internals, I was not able to definitively find or fix the root cause.
        - As a workaround, I left the existing implementation in place; the background generally appears after a scene change even when it does not appear on the first load.


### Features not implemented
1. **Rising Garbage Rows at Higher Difficulty**
    - **Intended Feature:**  
      At higher levels, a “garbage” row would periodically appear at the bottom of the board with one empty cell. The player would then need to place pieces efficiently to clear these rows, increasing pressure and difficulty.
    - **Status:** Not implemented.
    - **Reason:**
        - This would require:
            - Injecting new rows into the bottom of `currentGameMatrix`.
            - Shifting all existing rows up.
            - Ensuring the current falling piece and ghost logic handle this correctly.
        - Due to time constraints and the complexity of safely modifying the board while a piece is falling, I prioritized core gameplay (movement, hold, ghost, levels, scoring) over this advanced mechanic.


2. **Z‑Spin and T‑Spin Scoring Mechanics**
    - **Intended Feature:**  
      In modern Tetris, special moves such as T‑Spins and Z‑Spins (where a piece is rotated/slid into a tight slot at the last moment to clear lines) grant higher score bonuses than normal line clears.  
      The plan was:
        - Detect specific spin patterns (e.g. T‑Spin with line clear).
        - Award a higher “spin bonus” score when these conditions are met.
    - **Status:** Not implemented.
    - **Reason:**
        - Correctly detecting T‑Spins/Z‑Spins requires:
            - Tracking the last movement as a rotation.
            - Checking piece orientation and surrounding blocks to confirm a “true” spin, not just a normal line clear.
            - Integrating this with the existing `Score` and `ClearRow` logic cleanly.
        - This adds significant complexity to the rotation and collision system. Given the time available, I focused on getting standard movement, line clearing, hold, levels, and basic scoring fully stable rather than adding advanced spin detection rules.


3. **Replay Function (Game Playback)**
    - **Intended Feature:**  
      After game over, the user would be able to replay the entire game, watching all moves and piece placements from the beginning to the end (like a “demo” of their run).
    - **Status:** Not implemented.
    - **Reason:**
        - Implementing replay properly requires recording either:
            - Every board state, or
            - Every input/event and random seed (to be able to deterministically reconstruct the game).
        - Both approaches have significant **memory and complexity costs**:
            - Storing all states can be large in memory and on disk for longer games.
            - Storing inputs requires deterministic replay and careful versioning of game logic.
        - Due to these space and implementation complexities, and given the limited time, I decided not to implement a full replay system.


4. **Power‑Up Mechanics**
    - **Intended Feature:**  
      After clearing a certain number of rows, the player would receive a “power up” they could activate to clear specific rows or otherwise assist gameplay (e.g. clearing a chosen row, slowing time, etc.).
    - **Status:** Not implemented.
    - **Reason:**
        - This would require:
            - Tracking progress toward a power‑up threshold (e.g. rows cleared).
            - Adding new UI elements to display available power‑ups and their state.
            - Extending the board logic to apply special effects (e.g. remove a particular row on demand) without breaking normal drop/clear logic.
        - Similar reasons as above, with focus on getting the core Tetris rules, level system, and user features stable, there was not enough time to design and balance a proper power‑up system and integrate it cleanly into the existing codebase.


## Program Classes Modification Detail
### Modified Classes Function

| Class                          | Function / Area                                 | Modification                                                                                         | Reason                                                                                                       |
|--------------------------------|-------------------------------------------------|------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| `GameController`               | `GameController()`                              | Added `viewGuiController.setBoard(board);` call.                                                    | Allows the `GuiController` to hold a reference to the `SimpleBoard`, making restarting and scene changes cleaner and more flexible. |
| `GameController`               | `onDownEvent(...)`                              | Extended to handle hard drop scoring (using `ToBottom()`, merge, `clearRows()`, and score updates). | Hard drop gives a different score bonus and is logically part of the “down” handling, so this function now processes both normal down and hard drop behaviour. |
| `ViewData`                     | Constructor / fields                            | Extended to include `nextBrickData`, `ghostBrickData` + `ghostX/Y`, and `heldBrickData` + `canHold`. | `ViewData` is the central data object the GUI uses. Adding next/ghost/hold data allows the GUI to render the next piece, ghost piece, and held piece, and to know whether holding is currently allowed. |
| `EventType` (enum)             | Enum values                                     | Added `HARD_DROP` and `HOLD` enum values.                                                           | Makes it easy and type‑safe to distinguish between normal DOWN, HARD_DROP and HOLD events in controllers and board logic. |
| `Brick`                        | Class definition                                | Changed from `interface` to `abstract class` and moved `deepCopyList(...)` from `MatrixOperations` into `Brick`. | `deepCopyList()` is only used by brick implementations. Moving it into `Brick` simplifies brick creation and keeps brick‑specific utilities closer to the brick classes. |
| `Brick`                        | Class definition                                | Changed from `interface` to `abstract class` and moved `deepCopyList(...)` from `MatrixOperations` into `Brick`. | `deepCopyList()` is only used by brick implementations. Moving it into `Brick` simplifies brick creation and keeps brick‑specific utilities close to the brick classes. |
| `GuiController`                | `refreshBrick(...)`                             | Added checks to hide brick cells when they are above the visible playfield (hidden spawn rows).     | Originally the brick panel showed the piece even when it was above the visible game area, which looked incorrect; hiding those cells gives a cleaner appearance. |
| `GuiController`                | `bindScoreAndLevel(...)` (formerly `bindScore`) | Extended binding to include both score and level properties.                                         | When score increases and triggers a level change, the UI now updates both score labels and the level text automatically via JavaFX property binding. |
| `GuiController`                | `pauseGame(...)`                                | Implemented pause/resume logic (pausing the `Timeline`, showing/hiding the pause panel, toggling `isPause`). | The original `pauseGame` was effectively empty; adding the necessary code allows the game to be paused and resumed correctly without losing progress. |


### Simplified / Add-on Function on Existing Class 
| Class           | Function / Area                     | Modification / Role                                                                  | Reason                                                                                                  |
|-----------------|-------------------------------------|--------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| `GuiController` | `BrickPanelLayout(...)`             | Centralised the logic to position the brick panel relative to the game panel.        | This layout is needed multiple times; having one helper keeps the brick panel consistently aligned.     |
| `GuiController` | `KeyControl(...)`                   | Processes key events and maps keys to game actions (move, rotate, hold, hard drop).  | Makes keyboard handling clearer and more maintainable than inline or scattered event handlers.          |
| `GuiController` | `levelIncrease(int score)`          | Handles level changes: updates level, adjusts brick speed, changes BGM, triggers FX. | Groups all level‑up side‑effects (speed, music, visuals) in one place, simplifying score listeners.     |
| `GuiController` | `initNextBrickPanel(...)`           | Initializes the next‑brick panel with the first upcoming brick.                      | Connects `ViewData` next‑brick data to the FXML panel at game start.                                    |
| `GuiController` | `initHoldBrickPanel(...)`           | Initializes the hold‑brick panel with the first held brick (if any).                 | Connects `ViewData` hold‑brick data to the FXML panel at game start.                                    |
| `GuiController` | `refreshNextBrick(...)`             | Updates the next‑brick panel whenever the next brick changes.                        | Keeps the preview panel in sync with the actual next piece in the game logic.                           |
| `GuiController` | `refreshHoldBrick(...)`             | Updates the hold‑brick panel whenever the held brick changes.                        | Keeps the hold panel in sync with the held piece and handles the case where nothing is held.            |
| `GuiController` | `restartTimelineWithNewSpeed()`     | Rebuilds and restarts the drop `Timeline` with the new brick speed.                  | Every time the player levels up, drop speed must be updated; centralising this avoids code duplication. |
| `GuiController` | `ClearRowWithScore(...)`            | Handles a DOWN/HARD_DROP tick: moves piece, clears rows, awards score, updates UI.   | Called from both soft and hard drop paths; extracting it avoids duplication and keeps logic consistent. |
| `GuiController` | `getGhostFillColor(int)`            | Returns a semi‑transparent color for ghost bricks based on the original color.       | Ensures ghost pieces use the same color as the normal piece but with reduced opacity.                   |
| `GuiController` | `drawGhost(ViewData)`               | Draws the ghost piece on the background grid using ghost data from `ViewData`.       | Visually shows where the current piece will land, improving player planning and UX.                     |
| `GuiController` | `shakePane(Node)`                   | Applies a brief shake animation to the pane (e.g. on hard drop or level up).         | Adds visual feedback and impact to hard drops / level transitions, improving user experience.           |
| `GuiController` | `showLevelUpAnnouncement()`         | Animates the “Level Up” panel (fade in from below, pause, fade out upwards).         | Gives clear visual feedback when the player levels up, making progression more satisfying.              |
| `GuiController` | `updateLevelPaneStyle(int)`         | Updates level pane and label style classes (`level-1/2/3`) based on current level.   | Lets CSS control colors per level; this function switches classes when level changes.                   |
| `GuiController` | `setBgmGame(BackgroundMusic)`       | Injects the current background music instance into the game GUI controller.          | Simplifies sharing and controlling the same `BackgroundMusic` across scenes and levels.                 |
| `GuiController` | `setGameController(GameController)` | Stores a reference to the `GameController` in the GUI controller.                    | Helper used by `SceneChanger` so the GUI can call game‑logic methods (e.g. level updates) easily.       |
| `ViewData`      | `getGhostBrickData()`               | Added accessor for the ghost piece’s shape matrix.                                   | Allows the GUI (`GuiController.drawGhost`) to retrieve and render the ghost brick data.                 |
| `ViewData`      | `getNextBrickData()`                | Added accessor for the next piece’s shape matrix.                                    | Lets the next‑brick panel read and display the upcoming tetromino.                                      |
| `ViewData`      | `getHeldBrickData()`                | Added accessor for the held piece’s shape matrix.                                    | Lets the hold‑brick panel display the currently held tetromino.                                         |
| `SimpleBoard`   | `computeGhostRow()`                 | Computes the row where the current piece would land (ghost Y).                       | Used by `getViewData()` to provide accurate ghost position for drawing in the GUI.                      |
| `SimpleBoard`   | `ToBottom()`                        | Moves the current piece straight down until collision and counts dropped cells.      | Used by `GameController` to implement hard drop logic and calculate hard‑drop score bonus.              |

### New Java Classes

| Class                                                    | Type          | Description                                                                                                                                                                                                |
|----------------------------------------------------------|---------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `com.comp2042.Game.GameUIControl`                        | Interface     | Defines the key methods the game GUI must implement (e.g. binding score/level, gameOver, newGame, pauseGame), so `GameController` can interact with the UI in a decoupled way.                             |
| `com.comp2042.GameInfo.UsersFunction`                    | Class         | Manages user registration and login, stores usernames/passwords/scores in memory, and handles persistence to and from `users.txt` (register, loginVerify, saveToFile, loadFromFile, currentUser tracking). |
| `com.comp2042.SceneController.BGControl.Background`      | Class         | Applies animated/video backgrounds to `MediaView` controls and configures looping playback for scenes.                                                                                                     |
| `com.comp2042.SceneController.BGControl.BackgroundMusic` | Class         | Wraps `MediaPlayer` to provide background music playback (play/pause/stop, looping, volume control), used globally across scenes.                                                                          |
| `com.comp2042.SceneController.BGControl.SceneChanger`    | Class         | Central scene manager: loads FXML scenes, switches between them, manages fade transitions, and coordinates shared background music (`BackgroundMusic`) between scenes.                                     |
| `com.comp2042.SceneController.BGControl.SoundEffect`     | Class         | Wraps `AudioClip`/`MediaPlayer` for short sound effects (movement, rotation, hard drop, line clear, level up), with configurable volume and simple `play()` API.                                           |
| `com.comp2042.SceneController.MainMenu.MainMenuControl`  | Controller    | JavaFX controller for `MainMenu.fxml`; handles main menu interactions such as “Play as anonymous”, “Log in as player”, navigating to leaderboard, and starting the game.                                   |
| `com.comp2042.SceneController.MainMenu.User_Register`    | Controller    | JavaFX controller for `ProfileRegistery.fxml`; manages Sign Up / Log In UI, validates input, calls `UsersFunction` (register/login/currentUserUpdate), and returns to main menu or game on success.        |

### Additional Refactoring and Improvements

1. **Project Structure / Packaging**
    - Grouped classes and resources into meaningful packages and folders, for example:
        - `com.comp2042.Game` – game controllers and core UI logic.
        - `com.comp2042.GameLogic` – board logic, scoring, brick handling.
        - `com.comp2042.GameInfo` – data classes (e.g. `ViewData`, `Score`, `UsersFunction`).
        - `com.comp2042.SceneController.BGControl` – background video, music, scene changing, sound effects.
        - `com.comp2042.SceneController.MainMenu` – main menu and user register/login controllers.
        - `com.comp2042.logic.bricks` – concrete brick definitions and brick generation (`Brick`, specific tetromino classes, `BrickGenerator`, `RandomBrickGenerator`).
        - `com.comp2042.GameInfo` – data/DTO classes (`ViewData`, `NextShapeInfo`, `Score`, `UsersFunction` and related view models).
    - This improves readability and makes it easier to locate related functionality.


2. **Fixed Window Size**
    - Set a fixed window size for the main game stage to prevent resizing.
    - This avoids unwanted blank space and layout distortion when the window is resized.
    - Ensures the game panel, brick panel, and background stay visually aligned.


3. **JUnit Tests for Core Logic**
    - Implemented unit tests using JUnit for key logic components:
        - `GameControllerTest` – tests down/hard drop behavior, delegations, and basic scoring logic.
        - `UsersFunctionTest` – tests user registration, login verification, high‑score updating, and current user handling.
        - `ClearRowTest` – tests the `ClearRow` data class behavior and matrix copying.
        - `SimpleBoardTest` – tests movement (left/right/down), newGame/reset, hold behavior, and basic drop logic.
    - These tests help ensure that core game rules and user logic work correctly and make future changes safer.


4. **Layout Refinements / Pane Usage**
    - Used appropriate containers to align the playfield and active brick:
        - Placed the `GridPane` `brickPanel` inside a parent `Pane` and used a helper (`BrickPanelLayout`) to align it with `gamePanel`.
    - This refactor makes it easier to keep the falling piece visually synchronized with the background grid, and simplifies layout adjustments.


## Unexpected Problem
1. **Understanding and Navigating the Existing Codebase**  
   One unexpected challenge was the **time it took to understand the existing code and how the classes relate to each other**. At the beginning:

    - It was not obvious which classes were responsible for what:
        - `SimpleBoard` vs. `GameController` vs. `GuiController`.
        - How `BrickRotator`, `ViewData`, `Score`, and the interfaces (`Board`, `InputEventListener`, `GameUIControl`) fit together.
    - Because of that, early changes (for example when adding hard drop, hold, and next‑brick features) sometimes caused crashes or strange behaviour in other parts of the program (score not updating, brick disappearing, etc.).

   After spending focused time reading through the classes, following method calls, and mapping responsibilities:

    - It became clear which parts were **game logic** and which were **UI / controller**.
    - I learned to start from the interfaces/public methods and work “downwards” through the logic before modifying anything.
    - Once this mental model was in place, adding new features (hold, ghost, level system, sound, user system) became **much faster and safer**, because I knew exactly where each change belonged and which classes would be affected.


2. **Pane / Layout Placement and Brick Panel Movement**  
   Another unexpected issue came from the **JavaFX layout and pane structure**:

    - I wanted the falling brick panel to move and align visually with the game grid. Initially, I used a `GridPane` alone for the brick panel.
    - Due to how `GridPane` positioning works, the brick panel was effectively “locked” into the grid’s cell layout and was not easily movable using layout transforms.
    - When I tried to adjust positions, I sometimes ended up moving the entire `gamePanel` instead of just the `brickPanel`, which caused the whole game area to shift unexpectedly.
    - The visual display of the active brick was often misaligned or not updating as intended because the pane hierarchy was not designed for that kind of movement.

   The fix was to **reconsider the pane structure**:

    - I placed the `brickPanel` (`GridPane`) inside a parent `Pane`, and then:
        - Used a dedicated helper (`BrickPanelLayout(...)`) in `GuiController` to control its `layoutX` / `layoutY` relative to `gamePanel`.
    - Once I separated responsibilities (grid for cells, pane for movement), the brick panel became movable and could be aligned correctly with the background grid.
    - This experience showed how important the **choice and placement of panes** is for the visual part of the application: even when the logic is correct, a small layout decision can make the UI appear broken or unresponsive.


3. **Brick Size and Tight Coupling to Layout**  
   I originally planned to **increase the brick size from 20 to 25 pixels** to make the game visually clearer. However, this change turned out to be more difficult than expected:

    - The brick size (`BRICK_SIZE = 20`) is used in several layout calculations, especially when aligning:
        - The `gamePanel` background grid.
        - The `brickPanel` that draws the active piece.
    - When I tried changing the size to 25, the foreground bricks no longer lined up with the background grid:
        - The falling piece overlapped or “collapsed” into the background blocks incorrectly.
        - Some positions and offsets became misaligned, because they implicitly assumed a 20‑pixel size.

   This exposed that the **brick size was hard‑coded in too many places** and tightly coupled to layout logic.

    - To fix it properly, I would need to refactor:
        - All layout formulas (`BrickPanelLayout`, grid spacing, offsets) to rely on a single source of truth for cell size.
        - Possibly centralise `BRICK_SIZE` and ensure background and foreground both use it consistently.

   Due to time constraints and the risk of breaking a working layout late in development, I decided **not to complete this change** and kept the brick size at 20.  
   In future projects, I want to improve the refactoring and design so that changing a single constant (like brick size) will automatically update both the background grid and the active brick drawing without manual fixes.


4. **Illness and Time Management**  
   In the final months of the submission deadline, I became quite ill for about two weeks. This had a noticeable impact on the project:

    - Several “ideal” or planned features (such as advanced mechanics and polish items) could not be implemented or finished.
    - Progress slowed significantly during a period when I had originally planned to focus on extra features and thorough testing.

   Looking back, this highlighted the importance of **time management and starting early**:

    - Critical features should be implemented and stabilised earlier in the schedule, leaving the last weeks for polishing and lower‑priority extras.
    - In future projects, I plan to front‑load more of the core work and leave more buffer time, so that unexpected personal issues (such as sickness) have less impact on the final result.


