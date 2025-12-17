package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;
import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int MAX_PER_TYPE = 11;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        List<Unit> types = new ArrayList<>(unitList);
        types.sort(Comparator.comparingDouble((Unit u) -> - (double) u.getBaseAttack() / u.getCost())
                .thenComparingDouble((Unit u) -> - (double) u.getHealth() / u.getCost()));

        Army army = new Army();
        army.setUnits(new ArrayList<>());
        army.setPoints(0);

        Map<String, Integer> count = new HashMap<>();
        for (Unit t : types) {
            count.put(t.getUnitType(), 0);
        }

        boolean added = true;
        while (added) {
            added = false;
            for (Unit type : types) {
                int cur = count.get(type.getUnitType());
                if (cur < MAX_PER_TYPE && army.getPoints() + type.getCost() <= maxPoints) {
                    Unit unit = new Unit(
                            type.getUnitType() + " " + (cur + 1),
                            type.getUnitType(),
                            type.getHealth(),
                            type.getBaseAttack(),
                            type.getCost(),
                            type.getAttackType(),
                            type.getAttackBonuses(),
                            type.getDefenceBonuses(),
                            0, 0
                    );
                    unit.setProgram(type.getProgram());
                    unit.setAlive(true);
                    army.getUnits().add(unit);
                    army.setPoints(army.getPoints() + type.getCost());
                    count.put(type.getUnitType(), cur + 1);
                    added = true;
                }
            }
        }

        return army;
    }
}
