package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int MAX_UNITS_PER_TYPE = 11;

    private static final int MIN_X = 0;
    private static final int MAX_X = 2;
    private static final int MIN_Y = 0;
    private static final int MAX_Y = 20;

    private static final int MAX_PLACEMENT_ATTEMPTS = 50;

    private final Random random = new Random();

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        final List<Unit> assembledUnits = new ArrayList<>();
        final Map<String, Integer> unitsPerType = initTypeCounters(unitList);
        final Set<String> occupiedCells = new HashSet<>();

        final List<Unit> prototypes = new ArrayList<>(unitList);
        int totalPoints = 0;

        while (true) {
            Collections.shuffle(prototypes, random);
            boolean unitAddedThisRound = false;

            for (Unit prototype : prototypes) {
                final String type = prototype.getUnitType();
                final int currentCount = unitsPerType.get(type);

                if (currentCount >= MAX_UNITS_PER_TYPE) {
                    continue;
                }
                if (totalPoints + prototype.getCost() > maxPoints) {
                    continue;
                }

                final Optional<Cell> freeCell = findFreeCell(occupiedCells);
                if (freeCell.isEmpty()) {
                    continue;
                }

                final Unit newUnit = cloneUnitWithIndexAndPosition(prototype, currentCount + 1, freeCell.get());
                assembledUnits.add(newUnit);

                occupiedCells.add(freeCell.get().key());
                unitsPerType.put(type, currentCount + 1);
                totalPoints += prototype.getCost();

                unitAddedThisRound = true;
                break;
            }

            if (!unitAddedThisRound) {
                break;
            }
        }

        final Army army = new Army();
        army.setUnits(assembledUnits);
        army.setPoints(totalPoints);

        return army;
    }

    private Map<String, Integer> initTypeCounters(List<Unit> unitList) {
        final Map<String, Integer> counters = new HashMap<>();
        for (Unit unit : unitList) {
            counters.put(unit.getUnitType(), 0);
        }

        return counters;
    }

    private Optional<Cell> findFreeCell(Set<String> occupiedCells) {
        int attempts = 0;
        while (attempts < MAX_PLACEMENT_ATTEMPTS) {
            final int x = randomInRange(MIN_X, MAX_X);
            final int y = randomInRange(MIN_Y, MAX_Y);
            final Cell cell = new Cell(x, y);

            if (!occupiedCells.contains(cell.key())) {
                return Optional.of(cell);
            }
            attempts++;
        }

        return Optional.empty();
    }

    private int randomInRange(int minInclusive, int maxInclusive) {
        return random.nextInt(maxInclusive - minInclusive + 1) + minInclusive;
    }

    private Unit cloneUnitWithIndexAndPosition(Unit prototype, int index, Cell cell) {
        return new Unit(
                prototype.getUnitType() + " " + index,
                prototype.getUnitType(),
                prototype.getHealth(),
                prototype.getBaseAttack(),
                prototype.getCost(),
                prototype.getAttackType(),
                prototype.getAttackBonuses(),
                prototype.getDefenceBonuses(),
                cell.x(),
                cell.y()
        );
    }

    private static final class Cell {
        private final int x;
        private final int y;

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int x() {
            return x;
        }

        int y() {
            return y;
        }

        String key() {
            return x + ":" + y;
        }
    }
}
