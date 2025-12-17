package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        if (unitsByRow == null) {
            return List.of();
        }

        List<Unit> suitableUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            List<Unit> aliveUnits = row.stream()
                    .filter(Unit::isAlive)
                    .toList();

            for (Unit unit : aliveUnits) {
                if (!isCovered(unit, aliveUnits, isLeftArmyTarget)) {
                    suitableUnits.add(unit);
                }
            }
        }

        return suitableUnits;
    }

    private boolean isCovered(Unit unit, List<Unit> aliveUnits, boolean isLeftArmyTarget) {
        int x = unit.getxCoordinate();

        return isLeftArmyTarget
                ? aliveUnits.stream().anyMatch(u -> u.getxCoordinate() < x)
                : aliveUnits.stream().anyMatch(u -> u.getxCoordinate() > x);
    }
}
