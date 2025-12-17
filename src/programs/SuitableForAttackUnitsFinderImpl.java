package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;
import java.util.*;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> result = new ArrayList<>();
        for (List<Unit> row : unitsByRow) {
            if (row == null) {
                continue;
            }
            for (int i = 0; i < row.size(); i++) {
                Unit u = row.get(i);
                if (u != null && u.isAlive()) {
                    boolean uncovered;
                    if (isLeftArmyTarget) {
                        uncovered = i == 0 || row.get(i - 1) == null || !row.get(i - 1).isAlive();
                    } else {
                        uncovered = i == row.size() - 1 || row.get(i + 1) == null || !row.get(i + 1).isAlive();
                    }
                    if (uncovered) {
                        result.add(u);
                    }
                }
            }
        }

        return result;
    }
}
