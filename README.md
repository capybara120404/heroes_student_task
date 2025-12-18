# Heroes Task

## Description
The **Heroes Task** project is a student's assignment module for the *Heroes Battle* game.  
This module implements four algorithms necessary for a turn-based strategy to work with a combat system.:

- Generation of the enemy's army (`GeneratePreset`)
- Simulation of turn-based combat (`SimulateBattle`)
- Identification of available targets for attack (`SuitableForAttackUnitsFinder')
- Search for the shortest path between units ('UnitTargetPathFinder')

All algorithms are connected to the main module of the game through the library `heroes_task_lib'.

---

## Project structure
- **Heroes** is the main module of the game (interface, visualization, combat).
- **heroes_task** is the student's assignment module. Here are the implementations of the interfaces.
- **heroes_task_lib** is a library with interfaces and base classes (`Unit`, `Army`, `Edge`).

---

## Implemented classes
- `GeneratePresetImpl`  
  Implementation of the `Army generate(List<Unit> unitList, int maxPoints) method`  
  Forms a computer army based on the following limitations:
- Less than 11 units of each type
    - < maxPoints (1500)
- Optimization for attack/cost and health/cost

- `SimulateBattleImpl`  
  Implementation of the `void simulate(Army playerArmy, Army computerArmy)` method  
  Simulates a fight:
    - Sort units in descending order of attack
    - Moves as long as there are live units
- Battle log via `PrintBattleLog`

- `SuitableForAttackUnitsFinderImpl`  
  Implementation of the `List<Unit>' method getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget)`  
  Defines a list of targets to attack:
- For the left army — the unit is not closed on the left
    - For the right army, the unit is not closed on the right

- `UnitTargetPathFinderImpl`  
  Implementation of the `List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList)` method  
  Finds the shortest path on the 27x21 field:
- Occupied cells are taken into account
- Diagonal movement is allowed
    - Returns a list of path coordinates

---

## Algorithmic Complexity

### GeneratePresetImpl
- Sorting the list of unit types: **O(T log T)**, where T is the number of types.
- The main cycle of adding units: in the worst case **O(T·M)**, where M is the maximum number of units in the army.
- Final complexity: **O(T log T + T·M) = O(T·M)** (since M is much larger than T).

### SimulateBattleImpl
- A list of live units is generated on each round: **O(N)**, where N is the total number of units.
- Sort by attack: **O(N log N)**.
- Go through the list and attack: **O(N)**.
- Total difficulty of one round: **O(N log N)**.
- For R rounds: **O(R·N log N)**. The TOR allows the same or better complexity.

### SuitableForAttackUnitsFinderImpl
- Iterate through all the rows and units in them: **O(R·C)**, where R is the number of rows (fixed = 3), C is the number of units in a row.
- Neighbor check - O(1).
- Final difficulty: **O(C)** (linear in the number of units in a row).

### UnitTargetPathFinderImpl
- The shortest path search algorithm (Dijkstra/BFS) is used.
- Each cell of the field (WIDTH × HEIGHT) is processed no more than once.
- WIDTH = 27, HEIGHT = 21 -> total 567 cells.
- Final difficulty: **O(WIDTH·HEIGHT) = O(567) = O(W·H)**.

---

## Cloning the repository
1. Clone the repository using the command:
   ```bash
   https://github.com/capybara120404/heroes_student_task.git
    ```

---

## Assembly
1. Navigate to the root folder of the project:
   ```bash
   cd heroes_student_task
   ```
2. Create a folder for the build and compile the source files.:
   ```bash
   mkdir -p out/classes
   javac -d out/classes -classpath libs/heroes_task_lib-1.0-SNAPSHOT.jar src/programs/*.java
   ```
3. Go to the build folder and create a JAR file:
   ```bash
   cd out/classes
   jar cf heroes_student_task.jar programs/*.class
   ```
---

## Launch
1. Copy the JAR file to the folder with the main module of the game `heroes`:
   ```bash
   cp out/classes/heroes_student_task.jar path/to/heroes/jars/obf.jar
   ```
2. Launch the game
    ```bash
   java -jar "Heroes Battle-1.0.0.jar"
    ```